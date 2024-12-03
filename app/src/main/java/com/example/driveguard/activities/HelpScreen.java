package com.example.driveguard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.driveguard.ButtonDeck;
import com.example.driveguard.NetworkManager;
import com.example.driveguard.R;

public class HelpScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_help);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        NetworkManager networkManager = new NetworkManager(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButtonDeck.SetUpButtons(this);
        ButtonDeck.TintButton(this);

        TextView faq1Body = findViewById(R.id.faq1_body);
        TextView faq2Body = findViewById(R.id.faq2_body);
        TextView faq3Body = findViewById(R.id.faq3_body);
        TextView faq4Body = findViewById(R.id.faq4_body);

        ImageView faq1Button = findViewById(R.id.faq1_button);
        ImageView faq2Button = findViewById(R.id.faq2_button);
        ImageView faq3Button = findViewById(R.id.faq3_button);
        ImageView faq4Button = findViewById(R.id.faq4_button);

        faq1Button.setOnClickListener(v -> onClickFaq(faq1Body, faq1Button));
        faq2Button.setOnClickListener(v -> onClickFaq(faq2Body, faq2Button));
        faq3Button.setOnClickListener(v -> onClickFaq(faq3Body, faq3Button));
        faq4Button.setOnClickListener(v -> onClickFaq(faq4Body, faq4Button));
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
    private void onClickFaq(TextView body, ImageView imageView){
        if (body.getVisibility() == View.GONE){
            body.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.icon_arrow_up);
        } else {
            body.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.icon_arrow_down);
        }
    }
}
