package com.example.demo.repository;

import com.example.demo.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    // Finds events for a specific machine within a time window [cite: 73, 74]
    List<Event> findByMachineIdAndEventTimeBetween(String machineId, ZonedDateTime start, ZonedDateTime end);
}