package org.example.app.entity.sql;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "user_id",
            columnDefinition = "INT NOT NULL")
    @ManyToOne
    private User user;

    @JoinColumn(name = "event_id",
            columnDefinition = "INT NOT NULL")
    @OneToOne
    private Event event;
    private int place;
}
