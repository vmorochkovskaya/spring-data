package org.example.app.service;


import org.example.app.entity.Event;
import org.example.app.entity.Ticket;
import org.example.app.entity.User;
import org.example.app.entity.UserAccount;
import org.example.app.repository.BookingRepository;
import org.example.app.repository.EventRepository;
import org.example.app.repository.UserAccountRepository;
import org.example.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingFacadeImpl implements BookingFacade {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public Event getEventById(int eventId) {
        return eventRepository.getById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventRepository.findAllByTitleContaining(title, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
        return eventRepository.findAllByDate(day, PageRequest.of(pageNum, pageSize));
    }

    @Override
    @Transactional
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public boolean deleteEvent(int eventId) {
        return eventRepository.deleteById(eventId);
    }

    @Override
    public User getUserById(int userId) {
        return userRepository.getById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userRepository.findAllByNameContaining(name, PageRequest.of(pageNum, pageSize));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean deleteUser(int userId) {
        return userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public Ticket bookTicket(int userId, int eventId, int place, Ticket.Category category) {
        User user = userRepository.findById(userId).orElseThrow(UnsupportedOperationException::new);
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        Event event = eventRepository.findById(eventId).orElseThrow(UnsupportedOperationException::new);
        ticket.setEvent(event);
        ticket.setPlace(place);
        ticket.setCategory(category);
        return bookingRepository.save(ticket);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return bookingRepository.findAllByUser(user,
                PageRequest.of(pageNum, pageSize, Sort.by("event.date").descending()));
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return bookingRepository.findAllByEvent(event,
                PageRequest.of(pageNum, pageSize, Sort.by("user.email").ascending()));
    }

    @Override
    @Transactional
    public boolean cancelTicket(int ticketId) {
        return false;
    }

    @Override
    @Transactional
    public UserAccount withdrawMoney(int eventId, int userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(UnsupportedOperationException::new);
        UserAccount userAccount = userAccountRepository.findByUserId(userId).orElseThrow(UnsupportedOperationException::new);
        userAccount.setAmount(userAccount.getAmount().subtract(event.getTicketPrice()));
        return userAccountRepository.save(userAccount);
    }
}
