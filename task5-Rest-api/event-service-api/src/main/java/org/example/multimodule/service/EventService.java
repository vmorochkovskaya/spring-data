package org.example.multimodule.service;

import org.example.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    Event createEvent(Event event);

    int updateEvent(Event event);

    Optional<Event> getEvent(String id);

    void deleteEvent(String id);

    List<Event> getAllEvents();

    List<Event> getAllEventsByTitle(String title);
}
