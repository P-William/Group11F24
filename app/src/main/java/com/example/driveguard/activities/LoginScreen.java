package com.example.driveguard.activities;

import static com.example.driveguard.GsonUtilities.JsonToCredentials;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.driveguard.ButtonDeck;
import com.example.driveguard.NetworkManager;
import com.example.driveguard.R;
import com.example.driveguard.objects.Account;
import com.example.driveguard.objects.Credentials;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import lombok.SneakyThrows;
import okhttp3.Response;

public class LoginScreen extends AppCompatActivity {
    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        networkManager = new NetworkManager(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButtonDeck.SetUpButtons(this);

        Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @SneakyThrows
            @Override
            public void onClick(View v) {

                String username = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.editTextUsernameLogin)).getText()).toString();
                String password = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.editTextPasswordLogin)).getText()).toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginScreen.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(LoginScreen.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Account account = new Account(null, null, username, password);

                loginButton.setEnabled(false);

                Response response;

                try {
                    response = networkManager.Login(account);

                } catch (Exception e){

                    Toast.makeText(LoginScreen.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();

                    throw new Exception(e);
                } finally {

                    loginButton.setEnabled(true);

                }

                if (response.isSuccessful()) {
                    Toast.makeText(LoginScreen.this, "Login successful.", Toast.LENGTH_SHORT).show();

                    assert response.body() != null;
                    Credentials credentials = JsonToCredentials(response.body().string());

                    SaveCredentials(credentials);

                    Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                    startActivity(intent);
                    finish();

                } else if (response.code() == 401) {
                    Toast.makeText(LoginScreen.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(LoginScreen.this, "No Matching Account Found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginScreen.this, "System Error", Toast.LENGTH_SHORT).show();
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
public void SaveCredentials(@NonNull Credentials credentials){
    SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("driverId", credentials.getDriverId());
            editor.putString("token", credentials.getToken());
            editor.apply();
}
}
