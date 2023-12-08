package com.pa1.textdetectionapp.textdetectionapp.service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.Map;

@Slf4j
public class SQSService {
    // Client for interacting with Amazon SQS
    private SqsClient sqsClient;

    // Constructor to initialize the SQS client
    public SQSService() {
        this.sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    // Getter for SQS client
    public SqsClient getSqsClient() {
        return sqsClient;
    }

    // Method to get the URL of the queue, creating it if it doesn't exist
    public String fetchQueueUrl(SqsClient sqsClient) {
        final String QUEUE_NAME = "carsinformation.fifo";
        String queueUrl;

        GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder().queueName(QUEUE_NAME).build();

        try {
            queueUrl = sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl();
        } catch (QueueDoesNotExistException e) {
            // Create a new FIFO queue if it does not exist
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                    .attributesWithStrings(Map.of("FifoQueue", "true", "ContentBasedDeduplication", "true"))
                    .queueName(QUEUE_NAME)
                    .build();
            sqsClient.createQueue(createQueueRequest);

            // Fetch the newly created queue URL
            GetQueueUrlRequest newQueueUrlRequest = GetQueueUrlRequest.builder().queueName(QUEUE_NAME).build();
            queueUrl = sqsClient.getQueueUrl(newQueueUrlRequest).queueUrl();
        }
        return queueUrl;
    }

    // Method to receive a message from the queue
    public Message receiveMessageFromQueue(SqsClient sqsClient, String queueUrl) throws InterruptedException {
        Message message = null;
        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(1)
                    .build();

            try {
                message = sqsClient.receiveMessage(receiveMessageRequest).messages().get(0);
            } catch (IndexOutOfBoundsException e) {
                // Log if the queue is empty
                log.info("Queue is empty, waiting for the message.");
            }
            return message;
        } catch (Exception e) {
            log.info(String.valueOf(e));
        }
        return message;
    }
}
