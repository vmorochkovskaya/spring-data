package org.example.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.example.app.entity.Ticket;


@RestController
@Slf4j
public class BookingController {

    @PostMapping("/booking/save")
    public Ticket saveBookReview(@RequestBody Ticket ticket) {
        return ticket;
    }

}
