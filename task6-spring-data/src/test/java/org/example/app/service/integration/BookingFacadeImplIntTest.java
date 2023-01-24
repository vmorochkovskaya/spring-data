package org.example.app.service.integration;

import org.example.app.entity.Event;
import org.example.app.entity.Ticket;
import org.example.app.entity.User;
import org.example.app.repository.BookingRepository;
import org.example.app.repository.EventRepository;
import org.example.app.repository.UserRepository;
import org.example.app.service.BookingFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class BookingFacadeImplIntTest {
    @Autowired
    private BookingFacade bookingFacade;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void getEventById() {
        Event event = new Event();
        event.setDate(LocalDate.of(2023, 10, 10));
        event.setTitle("Ev");
        event.setTicketPrice(new BigDecimal("10.00"));
        Ticket ticket = new Ticket();
        ticket.setCategory(Ticket.Category.DISCOUNTED);
        ticket.setPlace(2);
        User user = new User();
        user.setId(1234);
        user.setEmail("test@abc");
        ticket.setUser(user);
        ticket.setEvent(event);
        Event ev = eventRepository.save(event);
        userRepository.save(user);
        bookingFacade.bookTicket(1234, ev.getId(), 2, Ticket.Category.DISCOUNTED);
        var expectedTickets = List.of(ticket);

        var actualTicketsByEvent = bookingFacade.getBookedTickets(event, 5, 0);

        assertThat(actualTicketsByEvent, is(expectedTickets));
    }

    @Test
    void getEventsByTitleWithExistingTitle() {
        Event event = new Event();
        event.setDate(LocalDate.of(2023, 10, 10));
        event.setTitle("Event-1");
        event.setTicketPrice(new BigDecimal("10.00"));
        Event event2 = new Event();
        event2.setDate(LocalDate.of(2023, 10, 10));
        event2.setTitle("ABC");
        event2.setTicketPrice(new BigDecimal("10.00"));
        Event event3 = new Event();
        event3.setDate(LocalDate.of(2023, 10, 10));
        event3.setTitle("Event-2");
        event3.setTicketPrice(new BigDecimal("10.00"));
        Event event4 = new Event();
        event4.setDate(LocalDate.of(2023, 10, 10));
        event4.setTitle("Event-3");
        event4.setTicketPrice(new BigDecimal("10.00"));
        List.of(event, event2, event3, event4).forEach(ev -> {
            eventRepository.save(ev);
        });

        var actualEvents = bookingFacade.getEventsByTitle("ven", 2, 0);

        assertThat(actualEvents, is(List.of(event, event3)));
    }
}