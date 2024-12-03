package com.example.driveguard.objects;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weather implements Serializable {
    private String timeStamp;
    private boolean isDay;
    private WeatherType weatherType;
    private WeatherSeverity weatherSeverity;
    private String iconUrl;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isDay() {
        return isDay;
    }

    public void setDay(boolean isDay) {
        this.isDay = isDay;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(WeatherType weatherType) {
        this.weatherType = weatherType;
    }

    public WeatherSeverity getWeatherSeverity() {
        return weatherSeverity;
    }

    public void setWeatherSeverity(WeatherSeverity weatherSeverity) {
        this.weatherSeverity = weatherSeverity;
    }
}
