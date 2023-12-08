package com.pa1.carrecognitionapp.service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

public class AWSS3 {
    // Static bucket name for AWS S3 operations
    private static final String S3_BUCKET_NAME = "njit-cs-643";

    // Client for interacting with AWS S3
    private S3Client s3Client;

    // Constructor to initialize the S3 client
    public AWSS3() {
        this.s3Client = S3Client.builder().region(Region.US_EAST_1).build();
    }

    // Getter for S3 client
    public S3Client getS3Client() {
        return s3Client;
    }

    // Method to fetch data from S3 bucket
    public List<S3Object> fetchObjectsFromS3(S3Client s3Client) {
        try {
            // Request to list objects in the specified S3 bucket
            ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(S3_BUCKET_NAME).build();

            // Returning the list of objects from the S3 bucket
            return s3Client.listObjects(listObjectsRequest).contents();
        } catch (S3Exception e) {
            // Handling S3 exceptions and logging error message
            System.err.println("S3 Error: " + e.awsErrorDetails().errorMessage());
            return null;
        }
    }
}
