package org.example.app.repository;

import org.example.app.entity.Event;
import org.example.app.entity.Ticket;
import org.example.app.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

@Repository
public interface BookingRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findAllByUser(User user, Pageable pageable);

    @QueryHints(@QueryHint(name = HINT_CACHEABLE, value = "true"))
    List<Ticket> findAllByEvent(Event event, Pageable pageable);

}
