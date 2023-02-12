package org.example.app;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="message", url = "http://micro-recipient:8800")
public interface FeignRecipient {
    @GetMapping("/message")
    public List<Notification> getMessages();
}
