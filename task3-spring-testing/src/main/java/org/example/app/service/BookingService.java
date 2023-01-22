package org.example.app.service;

import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.Ticket;
import org.example.app.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @JmsListener(destination = "${artemis.queue}")
    public void receiveOrder(Ticket ticket) {
        log.info("Ticket {} received", ticket);
        bookingRepository.save(ticket);
    }
}
