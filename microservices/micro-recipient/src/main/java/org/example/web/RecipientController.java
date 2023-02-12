package org.example.web;

import lombok.extern.slf4j.Slf4j;
import org.example.app.Notification;
import org.example.app.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class RecipientController {
    @Autowired
    private Receiver receiver;

    @GetMapping("/message")
    public List<Notification> getMessages() {
        var messages = receiver.getMessages();
        log.info("Getting collected messages {}", messages);
        receiver.clearMessages();
        return messages;
    }
}
