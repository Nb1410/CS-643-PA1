package com.pa1.carrecognitionapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.List;

@SpringBootApplication
@Slf4j
public class CarRecognition {
	private static final String S3_BUCKET_NAME = "njit-cs-643";

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(CarRecognition.class, args);

		// Initialize S3 service
		AWSS3 s3Service = new AWSS3();
		S3Client s3Client = s3Service.getS3Client();

		// Fetch images from S3
		List<S3Object> images = s3Service.fetchObjectsFromS3(s3Client);

		// Initialize Rekognition service
		AWSRekognition rekognitionService = new AWSRekognition();
		RekognitionClient rekognitionClient = rekognitionService.getRekognitionClient();

		// Initialize SQS service
		AWSSQSService sqsService = new AWSSQSService();
		SqsClient sqsClient = sqsService.getSqsClient();
		String queueUrl = sqsService.fetchQueueUrl(sqsClient);

		log.info("Queue URL: {}", queueUrl);

		// Process each image for car recognition
		for (S3Object image : images) {
			if (rekognitionService.detectCarInImage(rekognitionClient, image, S3_BUCKET_NAME)) {
				log.info("Car detected in image: {}", image.key());

				// Send message to SQS queue if car is detected
				if (sqsService.sendMessageToQueue(sqsClient, image.key(), queueUrl))
					log.info("Message sent to queue for image: {}", image.key());
				else
					log.info("Failed to send message to queue for image: {}", image.key());
			} else {
				log.info("No car detected in image: {}", image.key());
			}
		}

		// Send end signal to SQS queue
		sqsService.sendMessageToQueue(sqsClient, "-1", queueUrl);
		log.info("End signal sent to queue: -1");
	}
}
