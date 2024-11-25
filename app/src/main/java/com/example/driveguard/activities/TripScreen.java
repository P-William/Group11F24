package com.example.driveguard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.driveguard.ButtonDeck;
import com.example.driveguard.NetworkManager;
import com.example.driveguard.R;
import com.example.driveguard.objects.Credentials;
import com.example.driveguard.objects.Trip;

import java.io.IOException;

import okhttp3.Response;

public class TripScreen extends AppCompatActivity {

    private Credentials credentials;
    private Trip currentTrip;
    private final int START_TRIP_SUCCESS = 201;
    private final int STOP_TRIP_SUCCESS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_screen);

        //allows the UI thread to perform network calls. We could make them async if this causes issues
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Used to retrieve the driverID and login token from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            int driverID = extras.getInt("driverID");
            String token = extras.getString("token");
            credentials = new Credentials(driverID, token);
        } else {
            credentials = new Credentials();
        }

        //defines the toolbar used in the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toggle button that is used to stop and start trips
        ToggleButton startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonDeck.ToggleButtons(TripScreen.this);

                NetworkManager networkManager = new NetworkManager();

                //start trip here
                if (startButton.isChecked()){//for starting trip
                    // 201 means a trip was started successfully
                    Response response = networkManager.StartTrip(credentials, getApplicationContext());
                    if (response != null && response.code() == START_TRIP_SUCCESS){
                        //more will probably be needed here
                        //networkManager.dataCollector.startDataCollection();
                        Toast.makeText(TripScreen.this, "Trip Successfully started!", Toast.LENGTH_LONG).show();
                        try {
                            assert response.body() != null;
                            currentTrip =  networkManager.JsonToTrip(response.body().string());
                            //retrieve the trip ID sent by server
                            credentials.setTripId(currentTrip.getId());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        //BROOKE you can add all your trip stuff here

                    }
                    else{//set button back to unchecked. Add error message later
                        /*credentials.setTripId(1000);
                        Response response1 = networkManager.EndTrip(credentials, getApplicationContext());
                        if (response1.isSuccessful()){
                            Toast.makeText(TripScreen.this, "Trip successfully ended!", Toast.LENGTH_LONG).show();
                        }*/
                        startButton.setChecked(false);
                    }
                }

                else if(!startButton.isChecked()){
                    Response response = networkManager.EndTrip(credentials, getApplicationContext());
                    if (response != null && response.code() == STOP_TRIP_SUCCESS){
                        networkManager.dataCollector.stopDataCollection();
                        Toast.makeText(TripScreen.this, "Trip successfully ended!", Toast.LENGTH_LONG).show();
                        //display trip summary here which is withing response
                    }
                }

            }
        });

        ButtonDeck.SetUpButtons(this, credentials);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if (id == R.id.settings){
            Intent intent = new Intent(TripScreen.this, Settings.class);
            startActivity(intent);
        }
        return true;
    }


    }

