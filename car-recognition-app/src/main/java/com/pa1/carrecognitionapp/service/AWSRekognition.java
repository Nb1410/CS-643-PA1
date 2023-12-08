package com.pa1.carrecognitionapp.service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

@Slf4j
public class AWSRekognition {
    // Client for interacting with AWS Rekognition
    private RekognitionClient rekognitionClient;

    // Constructor to initialize the Rekognition client
    public AWSRekognition() {
        this.rekognitionClient = RekognitionClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    // Getter for Rekognition client
    public RekognitionClient getRekognitionClient() {
        return rekognitionClient;
    }

    // Method to recognize cars in an image
    public boolean detectCarInImage(RekognitionClient rekognitionClient, S3Object s3Object, String s3BucketName) {
        try {
            // Building the Image object from the S3 object
            Image image = Image.builder()
                    .s3Object(software.amazon.awssdk.services.rekognition.model.S3Object.builder()
                            .bucket(s3BucketName).name(s3Object.key()).build())
                    .build();

            // Creating a request to detect labels in the image
            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                    .image(image).minConfidence(90.0f)
                    .build();

            // Sending the request to Rekognition client and getting the response
            DetectLabelsResponse detectLabelsResponse = rekognitionClient.detectLabels(detectLabelsRequest);
            List<Label> labels = detectLabelsResponse.labels();

            // Checking if any label is 'Car'
            for (Label label : labels) {
                if (label.name().equals("Car")) {
                    return true; // Car detected in the image
                }
            }
        } catch (RekognitionException e) {
            // Logging in case of an exception
            log.info("Error during Rekognition: " + e.getMessage());
        }
        return false; // No car detected in the image
    }
}
