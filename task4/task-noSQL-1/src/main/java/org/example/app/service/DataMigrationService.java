package org.example.app.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.example.app.db.MongoClient;
import org.example.app.entity.mongodb.Event;
import org.example.app.entity.sql.Ticket;
import org.example.app.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DataMigrationService {
    @Autowired
    private BookingRepository bookingRepository;

    @Value("${db.name}")
    private String dbName;

    public void migrateDataToMongoDb() {
        List<Ticket> tickets = bookingRepository.findAll();
        List<org.example.app.entity.mongodb.User> users = new ArrayList<>();
        for (Ticket ticket : tickets) {
            users.add(org.example.app.entity.mongodb.User.builder()
                    .email(ticket.getUser().getEmail())
                    .tickets(List.of(org.example.app.entity.mongodb.Ticket.builder()
                            .place(ticket.getPlace())
                            .event(Event.builder()
                                    .date(ticket.getEvent().getDate().toString())
                                    .title(ticket.getEvent().getTitle()).build()).build())).build());
        }

        MongoDatabase db = MongoClient.getMongoClient().getDatabase(dbName);
        MongoCollection<org.example.app.entity.mongodb.User> usersCollection = db.getCollection("users", org.example.app.entity.mongodb.User.class);
        usersCollection.insertMany(users);
    }
}
