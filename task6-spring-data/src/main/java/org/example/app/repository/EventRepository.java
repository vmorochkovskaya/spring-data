package org.example.app.repository;

import org.example.app.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByTitleContaining(String title, Pageable pageable);
    List<Event> findAllByDate(LocalDate date, Pageable pageable);
    boolean deleteById(int eventId);

}
