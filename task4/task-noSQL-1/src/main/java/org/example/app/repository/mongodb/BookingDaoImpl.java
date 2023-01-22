package org.example.app.repository.mongodb;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.app.db.MongoClient;
import org.example.app.entity.mongodb.Ticket;
import org.example.app.entity.mongodb.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.pull;
import static com.mongodb.client.model.Updates.set;

@Repository
public class BookingDaoImpl implements BookingDao {
    @Value("${db.name}")
    private String dbName;

    @Override
    public void addTicket(String email, Ticket newTicket) {
        MongoDatabase db = MongoClient.getMongoClient().getDatabase(dbName);
        MongoCollection<User> usersCollection = db.getCollection("users", org.example.app.entity.mongodb.User.class);

        Bson projection = Projections.fields(Projections.include("tickets"), Projections.excludeId());
        FindIterable<User> userExistingTicketsProjection = usersCollection
                .find(eq("email", email))
                .projection(projection);
        List<Ticket> existingTickets = new ArrayList<>();
        for (User user : userExistingTicketsProjection) {
            existingTickets.addAll(user.getTickets());
        }
        existingTickets.add(newTicket);
        Bson query = eq("email", email);
        Bson updates = set("tickets", existingTickets);
        usersCollection.updateOne(query, updates);
    }

    @Override
    public void deleteTicket(String email, Ticket ticket) {
        MongoDatabase db = MongoClient.getMongoClient().getDatabase(dbName);
        MongoCollection<User> usersCollection = db.getCollection("users", org.example.app.entity.mongodb.User.class);
        Bson query = eq("email", email);
        Bson updates = pull("tickets", ticket);
        usersCollection.updateMany(query, updates);
    }

    @Override
    public List<String> showEventsAttendance() {
        MongoDatabase db = MongoClient.getMongoClient().getDatabase(dbName);
        MongoCollection<Document> usersCollection = db.getCollection("users");
        List<String> result = new ArrayList<>();
        AggregateIterable<Document> aggregateIterable = usersCollection.aggregate(
                List.of(Aggregates.unwind("$tickets"),
                        Aggregates.group("$tickets.event",
                        Accumulators.sum("count", 1)))
        );
        for (Document doc : aggregateIterable) {
            result.add(doc.toJson());
        }
        return result;
    }
}
