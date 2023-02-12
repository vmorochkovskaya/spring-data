package org.example.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class Collector {
    @Autowired
    private FeignRecipient feignRecipient;

    @Scheduled(cron = "*/20 * * * * *")
    public void collectMessage() {
        log.info("Collect messages from external call");
        List<Notification> messages = feignRecipient.getMessages();
        log.info("Collected messages from external call: {}", messages);
    }
}
