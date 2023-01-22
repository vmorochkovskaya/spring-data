package org.example.app.repository.mongodb;

import org.example.app.entity.mongodb.Ticket;

import java.util.List;


public interface BookingDao {
    void addTicket(String email, Ticket ticket);
    void deleteTicket(String email, Ticket ticket);
    List<String> showEventsAttendance();

}
