package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("events")
public class Event {
    private String id;
    private String title;
    private String place;
    private String speaker;
    private String eventType;
    private LocalDate dateTime;
}
