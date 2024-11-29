package com.example.driveguard.activities;

import static com.example.driveguard.activities.TripScreen.getCredentials;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.driveguard.ButtonDeck;
import com.example.driveguard.R;
import com.example.driveguard.objects.Credentials;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //Used to retrieve the driverID and login token from the previous activity
        Bundle extras = getIntent().getExtras();
        Credentials credentials = getCredentials(extras);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButtonDeck.SetUpButtons(this, credentials);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}