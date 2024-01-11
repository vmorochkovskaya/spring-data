package org.example;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.google.gson.Gson;
import org.example.app.entity.LiquidOrder;
import org.example.app.entity.Log;
import org.example.app.entity.MessageResponse;
import org.example.app.entity.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SetSubscriptionAttributesRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SqsException;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class SubscriberApplication implements CommandLineRunner {
    private List<MessageResponse> messagesList = new ArrayList<>();

    @Autowired
    private SqsClient sqsClient;
    @Autowired
    private ReceiveMessageRequest receiveMessageRequest;
    @Autowired
    private SnsClient snsClient;
    @Autowired
    private Gson gson;

    @Value("${queue.url}")
    private String queueUrl;
    @Value("${sns.arn}")
    private String topicArn;
    @Value("${queue.arn}")
    private String queueArn;
    @Value("${filter.policy}")
    private String filterPolicy;
    @Value("${logQueue.name}")
    private String logQueueName;
    @Value("${liquid.threshold}")
    private int liquidThreshold;

    public static void main(String[] args) {
        SpringApplication.run(SubscriberApplication.class, args);
    }

    @Override
    public void run(String... args) {
        //subscribe
        SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .topicArn(topicArn)
                .endpoint(queueArn)
                .protocol("sqs")
                .build();
        SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);
        String subscriptionArn = subscribeResponse.subscriptionArn();

        SetSubscriptionAttributesRequest request = SetSubscriptionAttributesRequest.builder()
                .subscriptionArn(subscriptionArn)
                .attributeName("FilterPolicy")
                .attributeValue("{\"type\": [\"" + filterPolicy + "\"]}")
                .build();
        snsClient.setSubscriptionAttributes(request);

        boolean flag = true;
        while (flag) {
            try {
                List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
                messages.forEach(message -> {
                    System.out.println("=============Message body=============");
                    System.out.println(message.body());
                    MessageResponse messageResponse = gson.fromJson(message.body(), MessageResponse.class);
                    messagesList.add(messageResponse);

                    //delete
                    String messageReceiptHandle = message.receiptHandle();
                    sqsClient.deleteMessage(DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(messageReceiptHandle)
                            .build());
                });
                if (messages.isEmpty()) {
                    flag = false;
                }

            } catch (SqsException e) {
                System.err.println(e.awsErrorDetails().errorMessage());
                System.exit(1);
            }
        }
        List<Log> logOrders = fillLogEntries();
        log(logOrders);
    }

    private List<Log> fillLogEntries() {
        List<Log> logOrders = new ArrayList<>();
        messagesList.sort(Comparator.comparing(MessageResponse::getTimestamp));
        int sum = 0;
        messagesList.forEach(() -> {
//            for (MessageResponse messageResponse : messagesList) {
                LiquidOrder order = gson.fromJson(messageResponse.getOrder(), LiquidOrder.class);
                sum += order.getVolume();
                if (sum <= liquidThreshold) {
                    logOrders.add(new Log(Status.ACCEPTED, order));
                } else {
                    logOrders.add(new Log(Status.REJECTED, order));
                }
//            }
        });
        return logOrders;
    }

    private void log(List<Log> logEntries) {
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        CreateQueueResult createQueueResult = null;
        try {
            createQueueResult = sqs.createQueue(logQueueName);
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                throw e;
            }
        }
        SendMessageBatchRequest sendBatchRequest = new SendMessageBatchRequest()
                .withQueueUrl(createQueueResult.getQueueUrl())
                .withEntries(logEntries.stream()
                        .map(entry -> new SendMessageBatchRequestEntry(UUID.randomUUID().toString(), gson.toJson(entry)))
                        .toArray(SendMessageBatchRequestEntry[]::new));
        sqs.sendMessageBatch(sendBatchRequest);
    }
}

