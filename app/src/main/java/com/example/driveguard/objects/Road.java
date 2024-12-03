package com.example.driveguard.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Road {
    private ServerLocation serverLocation;
    private String type;
    private String name;
    private int speedLimit;

    public ServerLocation getServerLocation() {
        return serverLocation;
    }

    public void setServerLocation(ServerLocation serverLocation) {
        this.serverLocation = serverLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }
}
