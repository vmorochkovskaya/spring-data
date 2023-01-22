package org.example.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.mongodb.Ticket;
import org.example.app.repository.mongodb.BookingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class BookingController {
    @Autowired
    private BookingDao bookingDao;


    @PostMapping("/booking/save")
    public void saveTicket(@RequestParam String email, @RequestBody Ticket ticket) {
        bookingDao.addTicket(email, ticket);
    }

    @DeleteMapping("/booking")
    public void deleteTicket(@RequestParam String email, @RequestBody Ticket ticket) {
        bookingDao.deleteTicket(email, ticket);
    }

    @GetMapping("/booking")
    public ResponseEntity getBookedEvents() {
       return ResponseEntity.ok(bookingDao.showEventsAttendance());
    }

}
