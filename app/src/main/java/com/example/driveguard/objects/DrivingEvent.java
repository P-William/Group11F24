package com.example.driveguard.objects;

import lombok.Getter;

@Getter
public class DrivingEvent {
    private String eventTime;
    private ServerLocation location;
    private EventType eventType;
    private EventSeverity severity;
    private int pointsDeducted;

    public DrivingEvent(String eventTime, ServerLocation location, EventType eventType, EventSeverity severity, int pointsDeducted)
    {
        this.eventTime = eventTime;
        this.location = location;
        this.eventType = eventType;
        this.severity = severity;
        this.pointsDeducted = pointsDeducted;
    }

    public int getPointsDeducted() {
        return pointsDeducted;
    }
}
