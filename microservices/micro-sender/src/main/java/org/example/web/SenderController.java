package org.example.web;


import lombok.extern.slf4j.Slf4j;
import org.example.app.Notification;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@Slf4j
public class SenderController {
    static final String topicExchangeName = "spring.boot.exchange";

    @Autowired
    private AmqpTemplate rabbitTemplate1;


    @PostMapping("/")
    public ResponseEntity<Notification> sendNotification(@RequestBody Notification notification) {
        log.info("Sending notification");
        rabbitTemplate1.convertAndSend(topicExchangeName, "foo.bar.baz", notification);
        return null;
    }
}
