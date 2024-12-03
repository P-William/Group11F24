package com.example.driveguard.objects;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompletedTrip implements Serializable {
    private int id;
    private int driverId;
    private int score;
    private double distanceKM;
    private TripLength duration;
}
