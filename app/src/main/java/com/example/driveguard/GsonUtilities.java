package com.example.driveguard;

import android.location.Location;

import androidx.annotation.NonNull;

import com.example.driveguard.objects.Address;
import com.example.driveguard.objects.CompletedTrip;
import com.example.driveguard.objects.CompletedTripAdapter;
import com.example.driveguard.objects.Credentials;
import com.example.driveguard.objects.Driver;
import com.example.driveguard.objects.ErrorReport;
import com.example.driveguard.objects.Road;
import com.example.driveguard.objects.ServerLocation;
import com.example.driveguard.objects.ServerLocationPair;
import com.example.driveguard.objects.Trip;
import com.example.driveguard.objects.Weather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GsonUtilities {
    public static Trip JsonToTrip(String responseBody){//add error handling
        Gson gson = new Gson();
        return gson.fromJson(responseBody, Trip.class);
    }
    public static String TripToJson(Trip trip){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(trip);
    }
    public static List<CompletedTrip> JsonToCompletedTripList(String responseBody){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CompletedTrip.class, new CompletedTripAdapter())
                .create();

        Type listType = new TypeToken<List<CompletedTrip>>() {}.getType();

        return gson.fromJson(responseBody, listType);
    }
    public static String LocationToServerLocationJson(@NonNull Location location){
        Gson gson = new Gson();
        ServerLocation serverLocation = new ServerLocation(location.getLatitude(), location.getLongitude());
        return gson.toJson(serverLocation);
    }
    public static String ServerLocationToJson(@NonNull ServerLocation serverLocation){
        Gson gson = new Gson();
        return gson.toJson(serverLocation);
    }
    public static String ServerLocationPairToJson(@NonNull ServerLocationPair serverLocationPair){
        Gson gson = new Gson();
        return gson.toJson(serverLocationPair);
    }
    public static ServerLocation JsonToServerLocation(String responseBody){
        Gson gson = new Gson();
        return gson.fromJson(responseBody, ServerLocation.class);
    }
    public static ErrorReport JsonToErrorReport(String responseBody){
        Gson gson = new Gson();
        return gson.fromJson(responseBody, ErrorReport.class);
    }
    public static Credentials JsonToCredentials(String responseBody){
        Gson gson = new Gson();
        return gson.fromJson(responseBody, Credentials.class);
    }
    public static Road JsonToRoad(String responseBody){
        Gson gson = new Gson();
        return gson.fromJson(responseBody, Road.class);
    }
    public static Weather JsonToWeather(String responseBody){
        Gson gson = new Gson();
        return gson.fromJson(responseBody, Weather.class);
    }
    public static Address JsonToAddress(String responseBody){
        Gson gson = new Gson();
        return gson.fromJson(responseBody, Address.class);
    }
    public static Driver JsonToDriver(String responseBody){
        Gson gson = new Gson();
        return gson.fromJson(responseBody, Driver.class);
    }
}
