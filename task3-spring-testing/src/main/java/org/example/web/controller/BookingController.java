package org.example.web.controller;

import org.example.app.entity.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.jms.core.JmsTemplate;


@RestController
@Slf4j
public class BookingController {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Value("${artemis.queue}")
    private String queue;

    @PostMapping("/booking/save")
    public Ticket saveBookReview(@RequestBody Ticket ticket) {
        jmsTemplate.convertAndSend(queue, ticket);
        return ticket;
    }

}
