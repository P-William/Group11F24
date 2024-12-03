package com.example.driveguard;

import com.example.driveguard.objects.Credentials;
import com.example.driveguard.objects.DrivingEvent;
import com.example.driveguard.objects.EventSeverity;
import com.example.driveguard.objects.EventType;
import com.example.driveguard.objects.HardAccelerateEvent;
import com.example.driveguard.objects.HardBrakeEvent;
import com.example.driveguard.objects.HardCorneringEvent;
import com.example.driveguard.objects.ServerLocation;
import com.example.driveguard.objects.SpeedingEvent;
import com.example.driveguard.objects.Weather;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/* Class Name: DataClassifier
 * Class Author: Brooke Cronin
 * Date: November 14, 2024
 * Course: SENG71000
 *
 * Description: This class is responsible for classifying driving events based on data
 *              inputs such as speed, g-force, and turning rate, and categorizing them
 *              into speed, acceleration, braking, and turning events.
 */
public class DataClassifier
{

    private final float postedSpeedLimit;

    /* Method Name: DataClassifier
     * Method Author: Brooke Cronin
     * Description: Constructor for the DataClassifier class. Initializes event lists and sets posted speed limit.
     * Parameters: float postedSpeedLimit (the speed limit to be used for speed classification)
     * Returns: N/A
     */
    public DataClassifier(float postedSpeedLimit)
    {
        this.postedSpeedLimit = postedSpeedLimit;
    }

    /* Method Name: classifyData
     * Method Author: Brooke Cronin
     * Description: Classifies data based on speed, g-force, and turning rate. Adds events to respective lists if criteria are met.
     * Parameters: float speed (current speed), float gForce (current g-force), float turningRate (current turning rate),
     *             long timestamp (time of event)
     * Returns: N/A
     */
    public Map<String, Boolean> classifyData(float speed, float gForce, float turningRate, String timestamp, NetworkManager networkManager, android.location.Location location, Weather currentWeather, int postedSpeedLimit, Map<String, Boolean> eventHasBeenDetected, float decelerationRate)
    {
        // Classify and handle Speeding Event
        if (speed > this.postedSpeedLimit) {
            SpeedingEvent speedEvent = new SpeedingEvent(speed, timestamp, location, networkManager, currentWeather, postedSpeedLimit);
            if (speedEvent.isSpeeding() && Boolean.FALSE.equals(eventHasBeenDetected.get("speed"))) {
                eventHasBeenDetected.put("speed", true);
                Response eventResponse = networkManager.addEventToTrip(
                        new DrivingEvent(
                                timestamp,
                                new ServerLocation(location.getLatitude(), location.getLongitude()),
                                EventType.SPEEDING,
                                EventSeverity.LOW,
                                speedEvent.deductPoints()
                        )
                );
                try {
                    if (!eventResponse.isSuccessful()) {
                        System.err.println("Failed to add Speeding Event. HTTP Code: " + eventResponse.code());
                    }
                } finally {
                    eventResponse.close(); // Ensure response resources are released
                }
            }
        }

        // Classify and handle Hard Acceleration Event
        HardAccelerateEvent accelerateEvent = new HardAccelerateEvent(gForce, timestamp, location, currentWeather);
        if (accelerateEvent.isHarshAcceleration() && Boolean.FALSE.equals(eventHasBeenDetected.get("accelerate"))) {
            eventHasBeenDetected.put("accelerate", true);
            Response eventResponse = networkManager.addEventToTrip(
                    new DrivingEvent(
                            timestamp,
                            new ServerLocation(location.getLatitude(), location.getLongitude()),
                            EventType.HARD_ACCELERATION,
                            EventSeverity.LOW,
                            accelerateEvent.deductPoints()
                    )
            );
            try {
                if (!eventResponse.isSuccessful()) {
                    System.err.println("Failed to add Hard Acceleration Event. HTTP Code: " + eventResponse.code());
                }
            } finally {
                eventResponse.close(); // Ensure response resources are released
            }
        }

        // Classify and handle Hard Braking Event
        HardBrakeEvent brakeEvent = new HardBrakeEvent(decelerationRate, timestamp, location, currentWeather);
        if (brakeEvent.isHarshBraking() && Boolean.FALSE.equals(eventHasBeenDetected.get("brake"))) {
            eventHasBeenDetected.put("brake", true);
            Response eventResponse = networkManager.addEventToTrip(
                    new DrivingEvent(
                            timestamp,
                            new ServerLocation(location.getLatitude(), location.getLongitude()),
                            EventType.HARD_BRAKING,
                            EventSeverity.LOW,
                            brakeEvent.deductPoints()
                    )
            );
            try {
                if (!eventResponse.isSuccessful()) {
                    System.err.println("Failed to add Hard Braking Event. HTTP Code: " + eventResponse.code());
                }
            } finally {
                eventResponse.close(); // Ensure response resources are released
            }
        }

        // Classify and handle Hard Turning Event
        HardCorneringEvent turningEvent = new HardCorneringEvent(turningRate, timestamp, location, currentWeather);
        if (turningEvent.isAggressiveTurn() && Boolean.FALSE.equals(eventHasBeenDetected.get("turn"))) {
            eventHasBeenDetected.put("turn", true);
            Response eventResponse = networkManager.addEventToTrip(
                    new DrivingEvent(
                            timestamp,
                            new ServerLocation(location.getLatitude(), location.getLongitude()),
                            EventType.HARD_CORNERING,
                            EventSeverity.LOW,
                            turningEvent.deductPoints()
                    )
            );
            try {
                if (!eventResponse.isSuccessful()) {
                    System.err.println("Failed to add Hard Turning Event. HTTP Code: " + eventResponse.code());
                }
            } finally {
                eventResponse.close(); // Ensure response resources are released
            }
        }
        return eventHasBeenDetected;
    }
}
