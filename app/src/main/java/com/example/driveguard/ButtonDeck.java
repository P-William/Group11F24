package com.example.driveguard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.driveguard.activities.HelpScreen;
import com.example.driveguard.activities.HistoryScreen;
import com.example.driveguard.activities.HomeScreen;
import com.example.driveguard.activities.TripScreen;
import com.example.driveguard.objects.TripHistoryAdapter;

public class ButtonDeck {
public static void SetUpButtons(@NonNull Activity activity){

        ImageButton homeButton = activity.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.getClass() != HomeScreen.class) {
                    Intent intent = new Intent(activity, HomeScreen.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });

        final ImageButton tripButton = activity.findViewById(R.id.trip);
        tripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.getClass() != TripScreen.class) {
                    Intent intent = new Intent(activity, TripScreen.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });

        final ImageButton historyButton = activity.findViewById(R.id.previousTrips);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.getClass() != HistoryScreen.class) {
                    Intent intent = new Intent(activity, HistoryScreen.class);
                    activity.startActivity(intent);
                }
            }
        });

        final ImageButton helpButton = activity.findViewById(R.id.help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.getClass() != HelpScreen.class) {
                    Intent intent = new Intent(activity, HelpScreen.class);
                    activity.startActivity(intent);
                }
            }
        });

}
    public static void ToggleButtons(@NonNull Activity activity){
        ImageButton home = activity.findViewById(R.id.homeButton);
        ImageButton previousTrips = activity.findViewById(R.id.previousTrips);
        ImageButton trip = activity.findViewById(R.id.trip);
        ImageButton help = activity.findViewById(R.id.help);
        home.setEnabled(!home.isEnabled());
        previousTrips.setEnabled(!previousTrips.isEnabled());
        trip.setEnabled(!trip.isEnabled());
        help.setEnabled(!help.isEnabled());
    }
    public static void TintButton(@NonNull Activity activity){
        ImageButton home = activity.findViewById(R.id.homeButton);
        ImageButton previousTrips = activity.findViewById(R.id.previousTrips);
        ImageButton trip = activity.findViewById(R.id.trip);
        ImageButton help = activity.findViewById(R.id.help);
        if (activity.getClass() == HomeScreen.class){
            home.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP);
        }
        else if (activity.getClass() == TripScreen.class){
            trip.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP);
        }
        else if (activity.getClass() == HistoryScreen.class){
            previousTrips.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP);
        }
        else if (activity.getClass() == HelpScreen.class){
            help.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP);
        }

    }
}
