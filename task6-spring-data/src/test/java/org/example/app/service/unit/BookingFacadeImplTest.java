package org.example.app.service.unit;

import org.example.app.entity.Event;
import org.example.app.entity.Ticket;
import org.example.app.entity.User;
import org.example.app.entity.UserAccount;
import org.example.app.repository.BookingRepository;
import org.example.app.repository.EventRepository;
import org.example.app.repository.UserAccountRepository;
import org.example.app.service.BookingFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles(profiles = {"test"})
class BookingFacadeImplTest {
    @Autowired
    private BookingFacade bookingFacade;
    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private UserAccountRepository userAccountRepository;

    @Test
    void getEventById() {
        Event event = new Event();
        event.setId(12345);
        event.setDate(LocalDate.of(2023, 10, 10));
        event.setTitle("Ev");
        event.setTicketPrice(new BigDecimal(10));
        when(eventRepository.getById(123)).thenReturn(event);

        var actualEvent = bookingFacade.getEventById(123);

        assertThat(actualEvent, is(event));
    }

    @Test
    void getEventsByTitleWithExistingTitle() {
        Event event = new Event();
        event.setId(12345);
        event.setTitle("Event");
        Event event2 = new Event();
        event2.setId(1234);
        event2.setTitle("Event");
        List<Event> eventList = List.of(event, event2);
        when(eventRepository.findAllByTitleContaining("ven", PageRequest.of(1, 5))).thenReturn(eventList);

        var actualEvents = bookingFacade.getEventsByTitle("ven", 5, 1);

        assertThat(actualEvents, is(eventList));
        verify(eventRepository).findAllByTitleContaining("ven", PageRequest.of(1, 5));
    }

    @Test
    void getEventsByTitleWithNotExistingTitle() {
        List<Event> eventList = List.of();
        when(eventRepository.findAllByTitleContaining("ven", PageRequest.of(1, 5))).thenReturn(eventList);

        var actualEvents = bookingFacade.getEventsByTitle("ven", 5, 1);

        assertThat(actualEvents, is(eventList));
        verify(eventRepository).findAllByTitleContaining("ven", PageRequest.of(1, 5));
    }

    @Test
    void getEventsForDay() {
        LocalDate searchLocalDate = LocalDate.of(2023, 10, 10);
        Event event = new Event();
        event.setId(12345);
        event.setDate(searchLocalDate);
        Event event2 = new Event();
        event2.setId(1234);
        event2.setDate(searchLocalDate);
        List<Event> eventList = List.of(event, event2);
        when(eventRepository.findAllByDate(searchLocalDate, PageRequest.of(1, 5))).thenReturn(eventList);

        var actualEvents = bookingFacade.getEventsForDay(searchLocalDate, 5, 1);

        assertThat(actualEvents, is(eventList));
        verify(eventRepository).findAllByDate(searchLocalDate, PageRequest.of(1, 5));
    }

    @Test
    void getBookedTicketsByUser() {
        Ticket ticket = new Ticket();
        ticket.setCategory(Ticket.Category.DISCOUNTED);
        ticket.setPlace(2);
        User user = new User();
        user.setId(1234);
        Event event = new Event();
        event.setId(12345);
        event.setDate(LocalDate.of(2023, 10, 10));
        ticket.setEvent(event);
        ticket.setUser(user);
        List<Ticket> tickets = List.of(ticket);
        when(bookingRepository.findAllByUser(
                user,
                PageRequest.of(1, 5, Sort.by("event.date").descending())))
                .thenReturn(tickets);

        var actualTicketsForUser = bookingFacade.getBookedTickets(user, 5, 1);

        assertThat(actualTicketsForUser, is(tickets));
        verify(bookingRepository).findAllByUser(
                user,
                PageRequest.of(1, 5, Sort.by("event.date").descending()));
    }

    @Test
    void withdrawMoneyWithExistingEventAndUserId() {
        Event event = new Event();
        event.setId(12345);
        event.setDate(LocalDate.of(2023, 10, 10));
        event.setTicketPrice(new BigDecimal(10));
        User user = new User();
        user.setId(123);
        UserAccount userAccount = new UserAccount();
        userAccount.setAmount(new BigDecimal(20));
        userAccount.setUser(user);

        when(eventRepository.findById(12345)).thenReturn(Optional.of(event));
        when(userAccountRepository.findByUserId(123)).thenReturn(Optional.of(userAccount));
        userAccount.setAmount(new BigDecimal(10));
        when(userAccountRepository.save(any())).thenReturn(userAccount);

        var actualUserAccount = bookingFacade.withdrawMoney(12345, 123);

        assertThat(actualUserAccount, is(userAccount));
    }
}