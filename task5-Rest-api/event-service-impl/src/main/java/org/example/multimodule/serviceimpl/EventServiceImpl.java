package org.example.multimodule.serviceimpl;

import org.example.Event;
import org.example.multimodule.repository.EventRepository;
import org.example.multimodule.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public int updateEvent(Event event) {
        Optional<Event> eventToUpdate = eventRepository.findById(event.getId());
        eventRepository.save(event);
        return eventToUpdate.isPresent() ? 1 : 0;
    }

    @Override
    public Optional<Event> getEvent(String id) {
        return eventRepository.findById(id);
    }

    @Override
    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getAllEventsByTitle(String title) {
        return eventRepository.findAllByTitle(title);
    }
}
