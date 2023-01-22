package org.example.multimodule.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Event;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto extends RepresentationModel<EventDto> {
    private String id;
    private String title;
    private String place;
    private String speaker;
    private String eventType;
    private LocalDate dateTime;

    public EventDto(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.place = event.getPlace();
        this.speaker = event.getSpeaker();
        this.eventType = event.getEventType();
        this.dateTime = event.getDateTime();
    }
}
