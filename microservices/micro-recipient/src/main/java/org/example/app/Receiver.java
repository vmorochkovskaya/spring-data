package org.example.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ReceiveAndReplyCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class Receiver {
    @Value("${rabbitmq.queue}")
    private String queue;
    private static List<Notification> messages = new ArrayList<>();
    @Autowired
    private AmqpTemplate rabbitTemplate1;

    @Scheduled(cron = "*/10 * * * * *")
    public void receiveMessage() {
        log.info("Read from the queue");
        boolean received =
                this.rabbitTemplate1.receiveAndReply(queue, (ReceiveAndReplyCallback<Notification, Notification>) order -> {
                    messages.add(order);
                    return order;
                });
        if (received) {
            log.info("We received a message!: " + messages);
        }
    }

    public List<Notification> getMessages() {
        return new ArrayList<>(messages);
    }

    public void clearMessages() {
        messages.clear();
    }
}
