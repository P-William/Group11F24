package com.example.driveguard.objects;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Trip implements Serializable {

    private int id;
    private int driverId;
    private String startTime;
    private String endTime;
    private ServerLocation startLocation;
    private ServerLocation endLocation;
    private TripStatus status;
    private int score;
    private double distance;
    private List<DrivingEvent> drivingEvents;

    public Trip(int id, int driverId,String startTime, String endTime,
                ServerLocation startLocation, ServerLocation endLocation,
                TripStatus status, int score, int distance, List<DrivingEvent> events){
        this.id = id;
        this.driverId = driverId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.status = status;
        this.score = score;
        this.distance = distance;
        this.drivingEvents = events;

    }
}
