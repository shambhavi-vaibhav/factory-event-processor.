package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
public class Event {
    @Id
    private String eventId;
    private ZonedDateTime eventTime;
    private ZonedDateTime receivedTime;
    private String machineId;
    private long durationMs;
    private int defectCount;

    // Default Constructor
    public Event() {}

    // All-Args Constructor
    public Event(String eventId, ZonedDateTime eventTime, ZonedDateTime receivedTime, String machineId, long durationMs, int defectCount) {
        this.eventId = eventId;
        this.eventTime = eventTime;
        this.receivedTime = receivedTime;
        this.machineId = machineId;
        this.durationMs = durationMs;
        this.defectCount = defectCount;
    }

    // MANUAL GETTERS AND SETTERS (This fixes the "Symbol Not Found" errors)
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public ZonedDateTime getEventTime() { return eventTime; }
    public void setEventTime(ZonedDateTime eventTime) { this.eventTime = eventTime; }

    public ZonedDateTime getReceivedTime() { return receivedTime; }
    public void setReceivedTime(ZonedDateTime receivedTime) { this.receivedTime = receivedTime; }

    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }

    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }

    public int getDefectCount() { return defectCount; }
    public void setDefectCount(int defectCount) { this.defectCount = defectCount; }

    // Helper for Deduplication Rule
    public boolean hasSamePayloadAs(Event other) {
        if (other == null) return false;
        return Objects.equals(this.machineId, other.machineId) &&
                this.durationMs == other.durationMs &&
                this.defectCount == other.defectCount &&
                Objects.equals(this.eventTime, other.eventTime);
    }
}