package com.example.driveguard.activities;

import static com.example.driveguard.GsonUtilities.JsonToWeather;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.driveguard.ButtonDeck;
import com.example.driveguard.DataCollector;
import com.example.driveguard.GsonUtilities;
//import com.example.driveguard.Manifest;
import com.example.driveguard.NetworkManager;
import com.example.driveguard.R;
import com.example.driveguard.Utilities;
import com.example.driveguard.objects.Driver;
import com.example.driveguard.objects.Weather;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;

import lombok.SneakyThrows;
import okhttp3.Response;

public class HomeScreen extends AppCompatActivity {
    private ImageView weatherIcon;
    public static String CHANNEL_ID = "Primary_Channel";
    private TextView welcomeMessage;
    private TextView username;
    private TextView score;
    private TextView scoreMessage;
    private NetworkManager networkManager;
    private DataCollector dataCollector;
    private Weather weather;
    private Driver driver;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.screen_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //allows the UI thread to perform network calls. We could make them async if this causes issues
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        createNotificationChannel();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
        boolean darkMode = preferences.getBoolean("darkMode", false);

        if (darkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            SaveDefaultDarkMode(preferences);
        }

        dataCollector = new DataCollector(getApplicationContext());
        networkManager = new NetworkManager(getApplicationContext());

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LoadWeatherIcon(networkManager);
        }

        //Display welcome message based on system clock
        LoadTimeMessage();
        LoadDriver(networkManager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButtonDeck.SetUpButtons(HomeScreen.this);
        ButtonDeck.TintButton(this);
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
            Intent intent = new Intent(HomeScreen.this, SettingsScreen.class);
            startActivity(intent);
        }
        else if (id == R.id.profile){
            Intent intent;
            intent = new Intent(HomeScreen.this, ProfileScreen.class);
            startActivity(intent);
        }
        else if (id == R.id.notifications) {
            Intent intent;
            intent = new Intent(HomeScreen.this, SuggestionScreen.class);
            startActivity(intent);
        }
        return true;
    }
    @SneakyThrows
    public void LoadWeatherIcon(@NonNull NetworkManager networkManager) {
        weatherIcon = findViewById(R.id.weather);
        if (Utilities.checkConnection(this)) {
            Location location = dataCollector.getStartingLocation();
            if (location == null) {
                return;
            }
            Response weatherRes = networkManager.getWeatherFromLocation(dataCollector.getStartingLocation());
            if (weatherRes != null && weatherRes.isSuccessful()) {
                assert weatherRes.body() != null;
                weather = JsonToWeather(weatherRes.body().string());
                Picasso.get()
                        .load(weather.getIconUrl())
                        .placeholder(R.drawable.icon_sun)
                        .into(weatherIcon);
            }
        }
    }
    public void LoadTimeMessage(){
        welcomeMessage = findViewById(R.id.greeting);
        LocalDateTime current = LocalDateTime.now();
        if (current.getHour() < 4){
            welcomeMessage.setText(R.string.go_sleep);
        } else if (current.getHour() < 12) {
            welcomeMessage.setText(R.string.good_morning);
        } else if (current.getHour() < 18) {
            welcomeMessage.setText(R.string.good_afternoon);
        } else {
            welcomeMessage.setText(R.string.good_evening);
        }
    }
    @SneakyThrows
    public void LoadDriver(@NonNull NetworkManager networkManager) {
        username = findViewById(R.id.username);
        score = findViewById(R.id.score);
        scoreMessage = findViewById(R.id.score_message);
        if (Utilities.checkConnection(this)) {
            Response response = networkManager.getDriver();
            if (response != null && response.isSuccessful()) {
                assert response.body() != null;
                Driver driver = GsonUtilities.JsonToDriver(response.body().string());
                if (driver != null) {
                    username.setText(driver.getUsername());
                    score.setText(String.valueOf(driver.getOverallScore()));
                }
            } else {
                username.setText(R.string.guest);
                score.setVisibility(View.INVISIBLE);
                scoreMessage.setVisibility(View.INVISIBLE);
            }
        }
        else {
            username.setText(R.string.guest);
            score.setVisibility(View.INVISIBLE);
            scoreMessage.setVisibility(View.INVISIBLE);
        }
    }
    public void SaveDefaultDarkMode(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("darkMode", false);
        editor.apply();
    }
    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                Toast.makeText(this, "Location permission approved", Toast.LENGTH_SHORT).show();
                LoadWeatherIcon(networkManager);
            } else {
                // Permission was denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Functionality will be limited", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        CharSequence name = "DriveGuard Notificaion Channel";
        String description = "DriveGuards Notification Channel for sending notification related to trips";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this.
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}