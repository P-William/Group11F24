package com.example.driveguard.activities;

import android.content.Intent;
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
import com.example.driveguard.Utilities;
import com.example.driveguard.objects.Account;
import com.example.driveguard.objects.Credentials;
import com.google.android.material.textfield.TextInputEditText;
import com.example.driveguard.R;
import com.google.gson.Gson;

import java.util.Objects;

import lombok.SneakyThrows;
import okhttp3.Response;

public class SignUpScreen extends AppCompatActivity {
    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_signup);

        networkManager = new NetworkManager(getApplicationContext());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButtonDeck.SetUpButtons(this);

        Button signUpButton = findViewById(R.id.buttonSignUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {

            @SneakyThrows
            @Override
            public void onClick(View v) {

                String firstName = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.editTextFirstName)).getText()).toString();
                String lastName = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.editTextLastName)).getText()).toString();
                String username = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.editTextUsername)).getText()).toString();
                String password = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.editTextPassword)).getText()).toString();
                String rePassword = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.editTextRePassword)).getText()).toString();

                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                        TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpScreen.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(rePassword)) {
                    Toast.makeText(SignUpScreen.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Account account = new Account(firstName, lastName, username, password);
                networkManager = new NetworkManager(getApplicationContext());

                Response response;
                try {

                    response = networkManager.SignUp(account);

                } catch (Exception e) {
                    Toast.makeText(SignUpScreen.this, "An error occurred", Toast.LENGTH_LONG).show();

                    throw new Exception(e);
                }

                if (response.isSuccessful()) {

                    Toast.makeText(SignUpScreen.this, "Sign up successful.", Toast.LENGTH_SHORT).show();

                    Response loginResponse;

                    try {
                        loginResponse = networkManager.Login(account);
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }

                    if (loginResponse.isSuccessful()) {
                        Toast.makeText(SignUpScreen.this, "Log in successful.", Toast.LENGTH_SHORT).show();

                        Gson gson = new Gson();
                        String responseBody = loginResponse.body().string();

                        Credentials credentials = gson.fromJson(responseBody, Credentials.class);

                        Utilities.SaveCredentials(getApplicationContext(), credentials);

                        Intent intent = new Intent(SignUpScreen.this, HomeScreen.class);
                        startActivity(intent);
                        finish();
                    }

                } else if (response.code() == 409) {
                    Toast.makeText(SignUpScreen.this, "Username Already Taken", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(SignUpScreen.this, "Invalid Fields Provided", Toast.LENGTH_SHORT).show();

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
