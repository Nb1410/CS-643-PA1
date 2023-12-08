package com.pa1.textdetectionapp.textdetectionapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class TextDetectionAppApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(TextDetectionAppApplication.class, args);

		// Initialize services for S3, SQS, and text detection
		S3Service s3Service = new S3Service();
		SQSService sqsService = new SQSService();
		SqsClient sqsClient = sqsService.getSqsClient();
		String queueUrl = sqsService.fetchQueueUrl(sqsClient);

		TextDetectionService textDetectionService = new TextDetectionService();
		RekognitionClient rekognitionClient = textDetectionService.getRekognitionClient();

		// Logging the queue URL
		log.info("queueUrl: {}", queueUrl);
		Map<String, String> detectedTextMap = new HashMap<>();
		while (true) {
			// Receiving messages from SQS
			Message message = sqsService.receiveMessageFromQueue(sqsClient, queueUrl);
			if (message == null) {
				Thread.sleep(1000);
				continue;
			}

			// Deleting the processed message from the queue
			DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
					.queueUrl(queueUrl)
					.receiptHandle(message.receiptHandle())
					.build();
			sqsClient.deleteMessage(deleteMessageRequest);

			// Check for termination signal
			if (message.body().equals("-1")) {
				break;
			} else {
				// Fetch image from S3 and detect text
				Image image = s3Service.fetchImageFromS3(message.body());
				textDetectionService.detectTextFromImage(rekognitionClient, image, message.body(), detectedTextMap);
			}
		}

		// Writing detected text to a file
		FileWriteService fileWriteService = new FileWriteService();
		fileWriteService.writeMapToFile(detectedTextMap);
	}
}
