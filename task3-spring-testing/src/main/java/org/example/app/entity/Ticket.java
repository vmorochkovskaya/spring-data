package org.example.app.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;

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
