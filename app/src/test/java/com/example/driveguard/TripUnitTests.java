package com.example.driveguard;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import com.example.driveguard.objects.Account;
import com.example.driveguard.objects.Trip;
import com.example.driveguard.objects.TripStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;

public class TripUnitTests {

    @Test
    public void TripToJson(){
        Trip trip = new Trip(1, 1, "1:30", "2:30",
        null, null, TripStatus.IN_PROGRESS, 1, 1, null);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String output = gson.toJson(trip);
        System.out.println(output);
        assertNotEquals(output, null);
    }
    @Test
    public void JsonToTrip(){
        Trip trip = new Trip(1, 1, "1:30", "2:30",
                null, null, TripStatus.IN_PROGRESS, 1, 1, null);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String output = gson.toJson(trip);

        Trip trip1 = gson.fromJson(output, Trip.class);

        assertNotEquals(trip1, null);
    }
    @Test
    public void AccountToJson() {
        Account account = new Account("dan", "boogen", "dbboog", "1234");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        final String scheme = "http";
        final String baseUrl = "drive-guard-api.the-hero.dev";
        final String tripUrl = "trip";
        final String authUrl = "auth";
        final String driverUrl = "driver";
        final String drivingContextUrl = "driving-context";

        System.out.println(gson.toJson(account));

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(authUrl)
                .addPathSegment("signup")
                .build();

        System.out.println(url.toString());


    }
}
