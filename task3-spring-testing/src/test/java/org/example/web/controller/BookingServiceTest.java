package org.example.web.controller;

import org.example.app.entity.Event;
import org.example.app.entity.Ticket;
import org.example.app.entity.User;
import org.example.app.repository.BookingRepository;
import org.example.app.service.BookingService;
import org.example.config.DemoApplicationTestPropertyValues;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = { BookingServiceTest.Initializer.class })
@Testcontainers
public class BookingServiceTest {
    @Container
    public static GenericContainer<?> activeMqContainer
            = new GenericContainer<>(DockerImageName.parse("rmohr/activemq:5.14.3")).withExposedPorts(61616);

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            DemoApplicationTestPropertyValues.using(postgreSQLContainer, activeMqContainer)
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Value("${artemis.queue}")
    private String queue;


    @Test
    public void sendMessageToGivenQueue() {
        User user = new User();
        user.setId(1);
        Event event = new Event();
        event.setId(1);
        Ticket ticketToBook = new Ticket();
        ticketToBook.setUser(user);
        ticketToBook.setEvent(event);
        ticketToBook.setId(134);
        ticketToBook.setPlace(5);

        jmsTemplate.convertAndSend(queue, ticketToBook);
        Ticket actualTicket = bookingRepository.findTicketByPlace(5);

        assertNotNull(actualTicket);
    }
}