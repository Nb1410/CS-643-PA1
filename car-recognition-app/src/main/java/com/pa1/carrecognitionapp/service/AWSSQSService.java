package com.pa1.carrecognitionapp.service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.Map;

@Slf4j
public class AWSSQSService {
    // Client for interacting with Amazon SQS
    private SqsClient sqsClient;

    // Static queue name for SQS operations
    private static final String QUEUE_NAME = "carsinformation.fifo";

    // Constructor to initialize the SQS client
    public AWSSQSService() {
        this.sqsClient = SqsClient.builder().region(Region.US_EAST_1).build();
    }

    // Getter for SQS client
    public SqsClient getSqsClient() {
        return sqsClient;
    }

    // Method to get the URL of the queue
    public String fetchQueueUrl(SqsClient sqsClient) {
        String queueUrl;
        GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder().queueName(QUEUE_NAME).build();

        try {
            queueUrl = sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl();
        } catch (QueueDoesNotExistException e) {
            // Creating the queue if it does not exist
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                    .attributesWithStrings(Map.of("FifoQueue", "true", "ContentBasedDeduplication", "true"))
                    .queueName(QUEUE_NAME)
                    .build();
            sqsClient.createQueue(createQueueRequest);

            // Fetching the newly created queue URL
            GetQueueUrlRequest newQueueUrlRequest = GetQueueUrlRequest.builder().queueName(QUEUE_NAME).build();
            queueUrl = sqsClient.getQueueUrl(newQueueUrlRequest).queueUrl();
        }
        return queueUrl;
    }

    // Method to push a message to the SQS queue
    public boolean sendMessageToQueue(SqsClient sqsClient, String imageKey, String queueUrl) {
        try {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageGroupId("CarText")
                    .messageBody(imageKey)
                    .build();
            String messageId = sqsClient.sendMessage(sendMessageRequest).sequenceNumber();
            log.info("Sequence number of the message: {}", messageId);
            return true;
        } catch (Exception e) {
            log.info("Error sending message: " + e);
            return false;
        }
    }
}
