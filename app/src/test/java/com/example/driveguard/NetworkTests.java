package com.example.driveguard;

import static com.example.driveguard.GsonUtilities.JsonToCredentials;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import android.app.Activity;

import com.example.driveguard.objects.Account;
import com.example.driveguard.objects.Credentials;
import com.example.driveguard.objects.Trip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Response;

public class NetworkTests {


    @Test
    public void TestSignUp() throws IOException {
        Account account = new Account("bob", "burger", "helloo", "Password!5");
        Activity activity = new Activity();
        NetworkManager networkManager = new NetworkManager(activity.getApplicationContext());

        Response response = networkManager.SignUp(account);

        //Gson gson = new Gson();
        //ErrorReport errorReport = gson.fromJson(response.body().string(), ErrorReport.class);

        System.out.println(response.code());
        System.out.println(response.body().string());

        assertTrue(response.isSuccessful());

    }

    @Test
    public void TestLogin() throws IOException {
        Account account = new Account("bob", "burger", "helloo", "Password!5");

        Activity activity = new Activity();
        NetworkManager networkManager = new NetworkManager(activity.getApplicationContext());

        Response response = networkManager.Login(account);

        System.out.println(response.code());
        System.out.println(response.body().string());

        assertTrue(response.isSuccessful());
    }
    @Test
    public void TestLogout() throws IOException{
        Account account = new Account("bob", "burger", "helloo", "Password!5");

        Activity activity = new Activity();
        NetworkManager networkManager = new NetworkManager(activity.getApplicationContext());

        Response response = networkManager.Login(account);

        Credentials credentials = JsonToCredentials(response.body().string());

        if (response.isSuccessful()){
            Response logoutResponse = networkManager.Logout();

            assertTrue(logoutResponse.isSuccessful());
        }
    }
    @Test
    public void TestStartTrip() throws IOException {
        Account account = new Account("bob", "burger", "helloo", "Password!5");

        Activity activity = new Activity();
        NetworkManager networkManager = new NetworkManager(activity.getApplicationContext());

        Response response = networkManager.Login(account);

        DataCollector dataCollector = new DataCollector(activity.getApplicationContext());

        if(response.isSuccessful()){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Credentials credentials = gson.fromJson(response.body().string(), Credentials.class);

            Response response1 = networkManager.StartTrip(dataCollector.getStartingLocation());

            System.out.println(response1.code());
            System.out.println(response1.body().string());

            assertTrue(response1.isSuccessful());
        }


    }
    @Test
    public void getTrips() throws IOException {
        Account account = new Account("Connor", "Miller", "Millerforce", "Hello123");
        Activity activity = mock(Activity.class);
        NetworkManager networkManager = new NetworkManager(activity.getApplicationContext());

        Response response1 = networkManager.Login(account);
        Credentials credentials;

        Response response = networkManager.getTripSummary(1067);

        if (response.isSuccessful()){

            assert response.body() != null;
            Trip trip = GsonUtilities.JsonToTrip(response.body().string());

            trip.toString();
        }
    }
}
