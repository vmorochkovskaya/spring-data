package org.example.multimodule.repository;

import org.example.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findAll();
    List<Event> findAllByTitle(String title);
}
