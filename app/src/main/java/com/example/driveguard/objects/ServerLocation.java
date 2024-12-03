package com.example.driveguard.objects;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerLocation implements Serializable {
private double latitude;
private double longitude;
public ServerLocation(double latitude, double longitude){
    this.latitude = latitude;
    this.longitude = longitude;
}
}
