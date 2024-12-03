package com.example.driveguard;

import static com.example.driveguard.GsonUtilities.JsonToCredentials;
import static com.example.driveguard.GsonUtilities.JsonToCompletedTripList;

import android.app.Activity;

import com.example.driveguard.objects.Account;
import com.example.driveguard.objects.CompletedTrip;
import com.example.driveguard.objects.Credentials;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public class GsonUtilsTests {
    @Test
    public void getListOfTrips() throws IOException {
        Account account = new Account("Connor", "Test", "ConnorTest", "Hello12345");
        Activity activity = new Activity();
        NetworkManager networkManager = new NetworkManager(activity.getApplicationContext());

        Response logInResponse = networkManager.Login(account);

        if (logInResponse.isSuccessful()) {
            System.out.println("Logged in");
            assert logInResponse.body() != null;
            Credentials credentials = JsonToCredentials(logInResponse.body().string());

            Response tripResponse = networkManager.getListOfTrips();

            if (tripResponse.isSuccessful()){
                System.out.println("Retrieved trips");
                assert tripResponse.body() != null;
                List<CompletedTrip> trips = JsonToCompletedTripList(tripResponse.body().string());

                for (CompletedTrip trip : trips) {
                    System.out.println(trip.toString());
                }
            }
        }
    }
    @Test
    public void CompletedTripAdapter(){

    }
}
