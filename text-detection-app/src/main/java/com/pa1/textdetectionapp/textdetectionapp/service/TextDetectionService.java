package com.pa1.textdetectionapp.textdetectionapp.service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectTextRequest;
import software.amazon.awssdk.services.rekognition.model.DetectTextResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.TextDetection;
import java.util.List;
import java.util.Map;

@Slf4j
public class TextDetectionService {

    // Client for interacting with AWS Rekognition
    private RekognitionClient rekognitionClient;

    // Constructor to initialize the Rekognition client
    public TextDetectionService() {
        this.rekognitionClient = RekognitionClient.builder().region(Region.US_EAST_1).build();
    }

    // Getter for Rekognition client
    public RekognitionClient getRekognitionClient() {
        return rekognitionClient;
    }

    // Method to detect text in an image and update the map with results
    public void detectTextInImage(RekognitionClient rekognitionClient, Image image, String imageKey, Map<String, String> textMap) {
        // Creating a request to detect text in the image
        DetectTextRequest textRequest = DetectTextRequest.builder().image(image).build();

        // Getting the response from Rekognition client
        DetectTextResponse textResponse = rekognitionClient.detectText(textRequest);

        // Extracting text detections from the response
        List<TextDetection> textDetections = textResponse.textDetections();
        log.info("Detected Text from the Image {}: {}", imageKey, textDetections.size());

        // Building a string with all detected text
        StringBuilder detectedTextBuilder = new StringBuilder();
        for (TextDetection text : textDetections) {
            detectedTextBuilder.append(text.detectedText()).append(" ");
        }

        // Storing the detected text in the map
        textMap.put(imageKey, detectedTextBuilder.toString());
    }
}
