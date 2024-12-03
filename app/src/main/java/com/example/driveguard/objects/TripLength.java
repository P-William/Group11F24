package com.example.driveguard.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TripLength {
    int seconds;
    int minutes;
    int hours;

    public TripLength(){
        this.seconds = 0;
        this.minutes = 0;
        this.hours = 0;
    }
    public TripLength(int seconds, int minutes, int hours){
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
    }
    public TripLength(int seconds){
        if (seconds < 0) {
            this.seconds = 0;
            this.minutes = 0;
            this.hours = 0;
        }
        this.seconds = seconds;
        this.hours = (int) (seconds / 3600);
        this.minutes = (int) ((seconds % 3600) / 60);
    }
    public String getFormattedTime(){
        if (this.hours != 0) {
            return String.valueOf(this.hours) + " Hours, " + String.valueOf(this.minutes) + " Minutes, and " + (this.seconds % 60) + " Seconds";
        } else if (this.minutes != 0){
            return String.valueOf(this.minutes) + " Minutes, and " + (this.seconds % 60) + " Seconds";
        } else {
            return (this.seconds % 60) + " Seconds";
        }
    }

}
