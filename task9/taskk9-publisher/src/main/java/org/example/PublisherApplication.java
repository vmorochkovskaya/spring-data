package org.example;

import com.google.gson.Gson;
import org.example.app.entity.CountableOrder;
import org.example.app.entity.LiquidOrder;
import org.example.app.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class PublisherApplication implements CommandLineRunner {
    @Autowired
    private SnsClient snsClient;
    @Autowired
    private CreateTopicRequest topicRequest;

    @Value("${topicName}")
    private String topicName;
    @Value("${sns.arn}")
    private String topicArn;

    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class, args);
    }


    @Override
    public void run(String... args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Order order;
            String attributeValue;
            System.out.println("Enter user:");
            String user = scanner.next();
            System.out.println("Enter type (liquid/countable):");
            String type = scanner.next();
            if ("countable".equals(type)) {
                System.out.println("Number of items:");
                int count = scanner.nextInt();
                order = CountableOrder.builder()
                        .count(count)
                        .userName(user)
                        .build();
                attributeValue = "countable";
            } else {
                System.out.println("Volume:");
                int volume = scanner.nextInt();
                order = LiquidOrder.builder()
                        .volume(volume)
                        .userName(user)
                        .build();
                attributeValue = "liquid";
            }

            //attributes
            String subject = "Put order";
            String attributeName = "type";
            MessageAttributeValue msgAttValue = MessageAttributeValue.builder()
                    .dataType("String")
                    .stringValue(attributeValue)
                    .build();
            Map<String, MessageAttributeValue> attributes = new HashMap<>();
            attributes.put(attributeName, msgAttValue);

            //publish
            PublishRequest pubRequest = PublishRequest.builder()
                    .topicArn(topicArn)
                    .subject(subject)
                    .message(new Gson().toJson(order))
                    .messageAttributes(attributes)
                    .build();
            snsClient.publish(pubRequest);
            System.out.println("Message was published to " + topicArn);

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
