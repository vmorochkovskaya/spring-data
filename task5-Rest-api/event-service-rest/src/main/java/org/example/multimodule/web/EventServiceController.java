package org.example.multimodule.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.example.Event;
import org.example.multimodule.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/events")
@Api(value = "Event Management", tags = {"Event Management Tag"})
public class EventServiceController {
    @Autowired
    private EventService eventService;

    @ApiOperation(value = "Get event by id")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 404, message = "Event not found"),
            @ApiResponse(code = 200, message = "Successful retrieval")})
    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEvent(@PathVariable("eventId") String id) {
        var eventOptional = eventService.getEvent(id);
        var event = eventOptional.orElseThrow(EventNotFoundException::new);
        return ResponseEntity.ok(new EventDto(event));
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Object> handleEventNotFoundException(EventNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cant find event by specified id");
    }

    @ApiOperation(value = "Get all events")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successful retrieval")})
    @GetMapping("/")
    public CollectionModel<EventDto> getAllEvents(@RequestParam(value = "title", required = false) String title) {
        List<Event> allEvents;
        if (title != null) {
            allEvents = eventService.getAllEventsByTitle(title);
        } else {
            allEvents = eventService.getAllEvents();
        }
        var eventDtos = new ArrayList<EventDto>();
        allEvents.forEach(ev -> eventDtos.add(new EventDto(ev)));
        eventDtos.forEach(ev -> {
            Link getEventByIdLink = linkTo(methodOn(EventServiceController.class).getEvent(ev.getId())).withSelfRel();
            ev.add(getEventByIdLink);
        });
        var link = linkTo(EventServiceController.class).withSelfRel();
        return CollectionModel.of(eventDtos, link);
    }

    @ApiOperation(value = "Create new event")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 201, message = "Successfully created", response = Event.class)})
    @PostMapping("/")
    public ResponseEntity<EventDto> createEvent(@RequestBody Event event) {
        var createdEvent = eventService.createEvent(event);
        var eventDto = new EventDto(createdEvent);
        eventDto.add(linkTo(methodOn(EventServiceController.class)
                .getAllEvents(createdEvent.getTitle()))
                .withRel(IanaLinkRelations.COLLECTION));
        eventDto.add(linkTo(methodOn(EventServiceController.class)
                .getEvent(createdEvent.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(eventDto);

    }

    @ApiOperation(value = "Update existing event or create new event")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 200, message = "Successfully updated")})
    @PutMapping("/")
    public ResponseEntity<String> updateEvent(@RequestBody Event event) {
        var result = eventService.updateEvent(event);
        var eventDto = new EventDto(event);
        var uri = linkTo(methodOn(EventServiceController.class).getEvent(eventDto.getId())).toUri();
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.OK).location(uri).body("Successfully updated");
        } else {
            return ResponseEntity.created(uri).body("Successfully created");
        }

    }

    @ApiOperation(value = "Delete event")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully deleted")})
    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventId") String eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
    }
}
