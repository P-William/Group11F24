package com.example.driveguard.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Credentials {
    private int driverId;
    private String token;
    private int tripId;
    public Credentials(int driverID, String token, int tripID){
        this.driverId = driverID;
        this.token = token;
        this.tripId = tripID;
    }
    public Credentials(int driverID, String token){
        this.driverId = driverID;
        this.token = token;
        this.tripId = -1;
    }
    public Credentials(){
        this.driverId = -1;
        this.token = "";
        this.tripId = -1;
    }
}
