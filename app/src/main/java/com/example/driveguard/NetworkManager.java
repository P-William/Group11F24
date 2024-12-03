package com.example.driveguard;

import static com.example.driveguard.GsonUtilities.LocationToServerLocationJson;
import static com.example.driveguard.GsonUtilities.ServerLocationPairToJson;
import static com.example.driveguard.GsonUtilities.ServerLocationToJson;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.example.driveguard.objects.Account;
import com.example.driveguard.objects.ServerLocation;
import com.example.driveguard.objects.ServerLocationPair;
import com.example.driveguard.objects.Credentials;
import com.google.gson.Gson;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.example.driveguard.objects.DrivingEvent;

@Getter
@Setter
public class NetworkManager {

    /*
    Note: every method in this class simply returns the servers response.
    The response code and body can be handled in the receiving activity(GsonUtilities for the body) or
    we can a handler for them if we have time.
     */

    private final OkHttpClient client;
    private final Context context;
    private final String scheme = "https";
    private final String baseUrl = "drive-guard-api.the-hero.dev";
    private final String tripUrl = "trip";
    private final String authUrl = "auth";
    private final String driverUrl = "driver";
    private final String drivingContextUrl = "driving-context";
    private DrivingEvent lastEvent;
    public NetworkManager(Context context){
        client = new OkHttpClient();
        this.context = context;
    }

    /**
     * Method that starts a new trip with the server
     * @param credentials for the drivers id and token
     * @param location takes android location which is formatted for the server in ServerLocation
     * @return if successful body contains a partial Trip object to be parsed
     */
    public Response StartTrip(@NonNull Location location) {
        Credentials credentials = Utilities.LoadCredentials(context);
        if (credentials.getDriverId() == -1){
            return null;
        }

      String jsonBody = LocationToServerLocationJson(location);
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
    /**
     * Method that ends an existing trip with the server
    * @param credentials for the drivers id, token, and trip id
     * @param location location takes android location which is formatted for the server in ServerLocation
     * @return if successful will return the full Trip object to be parsed
     */
    public Response EndTrip(@NonNull Location location){
        Credentials credentials = Utilities.LoadCredentials(context);

        String jsonBody = LocationToServerLocationJson(location);

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
    /**
     * Method that adds an event to the server
     * @param event an event that needs to follow the servers schema
     * @param credentials for the drivers drivers id, token, and trip id
     * @return if successful body is empty but code will be successful
     */
    public Response addEventToTrip(@NonNull DrivingEvent event){
        Credentials credentials = Utilities.LoadCredentials(context);
        lastEvent = event;
        Gson gson = new Gson();
        String jsonBody = gson.toJson(event);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(tripUrl)
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addPathSegment(String.valueOf(credentials.getTripId()))
                .addQueryParameter("token", credentials.getToken())
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

    /**
     * retrieves the drivers current trip. fails if they are not on a trip
     * @param credentials for the drivers drivers id, token, and trip id
     * @return if successful returns the current trip in the Trip class schema
     */
    public Response getCurrentTrip(){
        Credentials credentials = Utilities.LoadCredentials(context);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(tripUrl)
                .addPathSegment("current")
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", credentials.getToken())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .get()
                .build();

        Call call = client.newCall(request);

        try {
           return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * retrieves a particular trips summary
     * @param credentials for the drivers drivers id, token, and trip id
     * @param tripId the id for the trip in question
     * @return if successful will contain a full Trip object to be parsed
     */
    public Response getTripSummary(int tripId){//used for the summary of a particular trip
        Credentials credentials = Utilities.LoadCredentials(context);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(tripUrl)
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addPathSegment(String.valueOf(tripId))
                .addQueryParameter("token", credentials.getToken())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * retrieves a drivers list of previous trips
     * @param credentials for the drivers id, and token
     * @return if successful the body will contain a list of CompletedTrip objects to be parsed
     */
    public Response getListOfTrips(){
        Credentials credentials = Utilities.LoadCredentials(context);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(tripUrl)
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", credentials.getToken())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .get()
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Response ClearTripHistory(){
        Credentials credentials = Utilities.LoadCredentials(context);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(tripUrl)
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", credentials.getToken())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .delete(RequestBody.create("", null))
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public Response SignUp(@NonNull Account account){
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
    public Response Logout(){
        Credentials credentials = Utilities.LoadCredentials(context);
        if (credentials.getDriverId() == -1){
            return null;
        }

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(authUrl)
                .addPathSegment("logout")
                .addQueryParameter("driverId", String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", String.valueOf(credentials.getToken()))
                .build();

        RequestBody emptyBody = RequestBody.create(null, new byte[0]);

        Request request = new Request.Builder()
                .url(url)
                .post(emptyBody)
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public Response UpdatePassword(@NonNull String oldPassword, @NonNull String newPassword){
        Credentials credentials = Utilities.LoadCredentials(context);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(authUrl)
                .addPathSegment("password")
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", credentials.getToken())
                .addQueryParameter("oldPassword", oldPassword)
                .addQueryParameter("newPassword", newPassword)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .patch(RequestBody.create("", null))
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public Response RecoverAccount(@NonNull Account account){

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(driverUrl)
                .addPathSegment("recover")
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
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public Response UpdateUsername(@NonNull String username){

        Credentials credentials = Utilities.LoadCredentials(context);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(driverUrl)
                .addPathSegment("username")
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", credentials.getToken())
                .addQueryParameter("username", username)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .patch(RequestBody.create("", null))
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public Response UpdateName(@NonNull String firstName,@NonNull String lastName){

        Credentials credentials = Utilities.LoadCredentials(context);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(driverUrl)
                .addPathSegment("name")
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", credentials.getToken())
                .addQueryParameter("firstName", firstName)
                .addQueryParameter("lastName", lastName)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .patch(RequestBody.create("", null))
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public Response getDriver(){
        Credentials credentials = Utilities.LoadCredentials(context);
        if (credentials.getDriverId() == -1){
            return null;
        }

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(driverUrl)
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", credentials.getToken())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .get()
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public Response DeleteAccount(@NonNull String password){

        Credentials credentials = Utilities.LoadCredentials(context);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(driverUrl)
                .addPathSegment(String.valueOf(credentials.getDriverId()))
                .addQueryParameter("token", credentials.getToken())
                .addQueryParameter("password", password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .delete(RequestBody.create("", null))
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public Response getWeatherFromLocation(@NonNull Location location){

        String serverLocation = LocationToServerLocationJson(location);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(drivingContextUrl)
                .addPathSegment("weather")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(serverLocation, MediaType.parse("application/json")))
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
   }
    public Response getRoadFromLocation(@NonNull Location location){

        String serverLocation = LocationToServerLocationJson(location);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(drivingContextUrl)
                .addPathSegment("road")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(serverLocation, MediaType.parse("application/json")))
                .build();

        Call call = client.newCall(request);

        try{
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
   }
    public Response getEstimatedDistance(@NonNull ServerLocation locationOne, @NonNull ServerLocation locationTwo){

       ServerLocationPair serverLocationPair = new ServerLocationPair(locationOne,locationTwo);
       String pair = ServerLocationPairToJson(serverLocationPair);

       HttpUrl url = new HttpUrl.Builder()
               .scheme(scheme)
               .host(baseUrl)
               .addPathSegment(drivingContextUrl)
               .addPathSegment("distance")
               .build();

       Request request = new Request.Builder()
               .url(url)
               .addHeader("accept", "*/*")
               .addHeader("Content-Type", "application/json")
               .post(RequestBody.create(pair, MediaType.parse("application/json")))
               .build();

       Call call = client.newCall(request);

       try {
           return call.execute();
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }
    public Response getAddress(@NonNull Location location){

        String serverLocation = LocationToServerLocationJson(location);

    HttpUrl url = new HttpUrl.Builder()
            .scheme(scheme)
            .host(baseUrl)
            .addPathSegment(drivingContextUrl)
            .addPathSegment("address")
            .build();

    Request request = new Request.Builder()
            .url(url)
            .addHeader("accept", "*/*")
            .addHeader("Content-Type", "application/json")
            .post(RequestBody.create(serverLocation, MediaType.parse("application/json")))
            .build();

    Call call = client.newCall(request);

    try {
        return call.execute();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
    public Response getAddress(@NonNull ServerLocation location){

        String serverLocation = ServerLocationToJson(location);

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(baseUrl)
                .addPathSegment(drivingContextUrl)
                .addPathSegment("address")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(serverLocation, MediaType.parse("application/json")))
                .build();

        Call call = client.newCall(request);

        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
