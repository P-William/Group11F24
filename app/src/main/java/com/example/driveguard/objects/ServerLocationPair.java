package com.example.driveguard.objects;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerLocationPair implements Serializable {
    private ServerLocation locationOne;
    private ServerLocation locationTwo;
    public ServerLocationPair(ServerLocation locationOne, ServerLocation locationTwo){
        this.locationOne = locationOne;
        this.locationTwo = locationTwo;
    }
}
