package com.pa1.textdetectionapp.textdetectionapp.service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Slf4j
public class S3Service {
    // Bucket name for S3 operations
    private static final String BUCKET_NAME = "njit-cs-643";

    // S3 client for interacting with AWS S3
    private S3Client s3Client;

    // Constructor to initialize the S3 client
    public S3Service() {
        this.s3Client = S3Client.builder().region(Region.US_EAST_1).build();
    }

    // Getter for S3 client
    public S3Client getS3Client() {
        return s3Client;
    }

    // Method to fetch an image from S3 by its key
    public Image fetchImageFromS3(String imageKey){
        try {
            // Building and returning the Image object from S3
            return Image.builder()
                    .s3Object(S3Object.builder()
                            .bucket(BUCKET_NAME)
                            .name(imageKey)
                            .build())
                    .build();
        } catch (S3Exception e) {
            // Logging the error message in case of an exception
            log.info(e.awsErrorDetails().errorMessage());
            return null;
        }
    }
}
