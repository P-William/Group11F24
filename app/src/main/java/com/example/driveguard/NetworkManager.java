package com.example.driveguard;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.example.driveguard.objects.Account;
import com.example.driveguard.objects.ServerLocation;
import com.example.driveguard.objects.Trip;
import com.example.driveguard.objects.Credentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import trip_data.DataCollector;
import trip_data.Event;

public class NetworkManager {
    private OkHttpClient client;
    public DataCollector dataCollector;
    private final String scheme = "https";
    private final String baseUrl = "drive-guard-api.the-hero.dev";
    private final String tripUrl = "trip";
    private final String authUrl = "auth";
    private final String driverUrl = "driver";
    private final String drivingContextUrl = "driving-context";
    public NetworkManager(){ client = new OkHttpClient();}
    public Response StartTrip(@NonNull Credentials credentials, Context context) {
        dataCollector = new DataCollector(context);
        dataCollector.startDataCollection();
        Gson gson = new Gson();
        Location location = dataCollector.getStartingLocation();
        ServerLocation serverLocation = new ServerLocation(location.getLatitude(), location.getLongitude());
        String jsonBody = gson.toJson(serverLocation); // give gson the current location
        //url for the request
        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(tripUrl)
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", credentials.getToken())
                .build();
        //building the request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();

        //Synchronous request to server
        Call call = client.newCall(request);
        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Response EndTrip(@NonNull Credentials credentials, Context context){
        dataCollector = new DataCollector(context);
        dataCollector.startDataCollection();
        Gson gson = new Gson();
        Location location = dataCollector.getStartingLocation();
        ServerLocation serverLocation = new ServerLocation(location.getLatitude(), location.getLongitude());
        String jsonBody = gson.toJson(serverLocation); // give gson the current location

        HttpUrl url =  new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(tripUrl)
                .addPathSegments(String.valueOf(credentials.getDriverId()))
                .addPathSegments(String.valueOf(credentials.getTripId()))
                .addQueryParameter("token", String.valueOf(credentials.getToken()))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .patch(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();

        Call call = client.newCall(request);
        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Response addEventToTrip(Event event, @NonNull Credentials credentials){
        Gson gson = new Gson();
        String jsonBody = gson.toJson(event);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(tripUrl)
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addPathSegment(String.valueOf(credentials.getToken()))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();

        Call call = client.newCall(request);
        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Trip getCurrentTrip(){

        return null;
    }
    public void getTripSummary(Credentials credentials){

    }
    public Response SignUp(Account account){
        Gson gson = new Gson();
        String jsonBody = gson.toJson(account);

        System.out.println(jsonBody);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(authUrl)
                .addPathSegment("signup")
                .build();

        System.out.println(url.toString());

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();
        System.out.println(request.toString());

        Call call = client.newCall(request);

        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Response Login(@NonNull Account account) {

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(authUrl)
                .addPathSegment("login")
                .addQueryParameter("username", account.getUsername())
                .addQueryParameter("password", account.getPassword())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .post(RequestBody.create("", null))
                .build();

        Call call = client.newCall(request);

        try {
             return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Response Logout(@NonNull Credentials credentials){
        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(authUrl)
                .addPathSegment("logout")
                .addQueryParameter("driverId", String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", String.valueOf(credentials.getToken()))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", null))
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public Trip JsonToTrip(String responseBody){//add error handling
        Gson gson = new Gson();
        return gson.fromJson(responseBody, Trip.class);
    }
    public String TripToJson(Trip trip){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(trip);
    }
}
