package com.example.demo.service;

import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public Map<String, Object> processBatch(List<Event> events) {
        int accepted = 0;
        int deduped = 0;
        int updated = 0;
        List<Event> toSave = new ArrayList<>();
        List<Map<String, String>> rejections = new ArrayList<>();

        ZonedDateTime now = ZonedDateTime.now();

        for (Event event : events) {
            // 1. Validation [cite: 45, 46]
            if (event.getDurationMs() < 0 || event.getDurationMs() > 21600000) {
                rejections.add(Map.of("eventId", event.getEventId(), "reason", "INVALID_DURATION"));
                continue;
            }
            if (event.getEventTime().isAfter(now.plusMinutes(15))) {
                rejections.add(Map.of("eventId", event.getEventId(), "reason", "FUTURE_EVENT"));
                continue;
            }

            // 2. Deduplication Logic
            event.setReceivedTime(now); // Set server received time [cite: 40]
            Optional<Event> existing = eventRepository.findById(event.getEventId());

            if (existing.isPresent()) {
                if (existing.get().hasSamePayloadAs(event)) {
                    deduped++; // Ignore identical [cite: 43]
                } else {
                    toSave.add(event); // Treat as update [cite: 44]
                    updated++;
                }
            } else {
                toSave.add(event);
                accepted++;
            }
        }

        eventRepository.saveAll(toSave); // Batch save for performance [cite: 71]

        return Map.of(
                "accepted", accepted,
                "deduped", deduped,
                "updated", updated,
                "rejected", rejections.size(),
                "rejections", rejections
        );
    }

    public Map<String, Object> getStats(String machineId, ZonedDateTime start, ZonedDateTime end) {
        List<Event> events = eventRepository.findByMachineIdAndEventTimeBetween(machineId, start, end);

        // Filter out -1 for defect calculations [cite: 47, 77]
        long totalDefects = events.stream()
                .filter(e -> e.getDefectCount() != -1)
                .mapToInt(Event::getDefectCount)
                .sum();

        // Calculate hours in window [cite: 80]
        double windowHours = Duration.between(start, end).toSeconds() / 3600.0;
        double avgDefectRate = (windowHours > 0) ? totalDefects / windowHours : 0;

        String status = (avgDefectRate < 2.0) ? "Healthy" : "Warning"; // [cite: 81-83]

        return Map.of(
                "machineId", machineId,
                "eventsCount", events.size(),
                "defectsCount", totalDefects,
                "avgDefectRate", Math.round(avgDefectRate * 100.0) / 100.0,
                "status", status
        );
    }
}