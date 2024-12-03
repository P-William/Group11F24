package com.example.driveguard.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.driveguard.ButtonDeck;
import com.example.driveguard.R;

public class SettingsScreen extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_settings);

        preferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
        SwitchCompat switchCompat = findViewById(R.id.darkMode);
        SwitchCompat dataSwitch = findViewById(R.id.data_switch);
        SwitchCompat notificationsSwitch = findViewById(R.id.notification_switch);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButtonDeck.SetUpButtons(this);

        boolean darkMode = preferences.getBoolean("darkMode", false);
        switchCompat.setChecked(darkMode);

        boolean notifications = preferences.getBoolean("notifications", false);
        notificationsSwitch.setChecked(notifications);

        boolean dataCollection = preferences.getBoolean("dataCollection", true);
        dataSwitch.setChecked(dataCollection);

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = preferences.edit();
                editor.putBoolean("darkMode", switchCompat.isChecked());
                editor.apply();
                if (switchCompat.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        notificationsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = preferences.edit();
                editor.putBoolean("notifications", notificationsSwitch.isChecked());
                editor.apply();
                if (notificationsSwitch.isChecked()){
                    if (ActivityCompat.checkSelfPermission(SettingsScreen.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SettingsScreen.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                    }
                }
            }
        });

        dataSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = preferences.edit();
                editor.putBoolean("dataCollection", dataSwitch.isChecked());
                editor.apply();
                if (!dataSwitch.isChecked()){
                    Toast.makeText(SettingsScreen.this, "Data Collection Disabled", Toast.LENGTH_SHORT).show();
                    Toast.makeText(SettingsScreen.this, "Trip Tracking Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            Intent intent = new Intent(this, SettingsScreen.class);
            startActivity(intent);
        }
        else if (id == R.id.profile){
            Intent intent = new Intent(this, ProfileScreen.class);
            startActivity(intent);
        }
        else if (id == R.id.notifications){
            Intent intent = new Intent(this, SuggestionScreen.class);
            startActivity(intent);
        }
        return true;
    }
}
