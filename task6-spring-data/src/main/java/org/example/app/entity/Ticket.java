package org.example.app.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ticket")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ticket {
    @Id
    private int id;

    @JoinColumn(name = "user_id",
            columnDefinition = "INT NOT NULL")
    @ManyToOne(cascade = {CascadeType.MERGE})
    private User user;

    @JoinColumn(name = "event_id",
            columnDefinition = "INT NOT NULL")
    @OneToOne
    private Event event;
    private int place;
    @Enumerated(EnumType.STRING)
    private Category category;

    public enum Category {
        VIP,
        DISCOUNTED
    }
}
