package com.example.driveguard.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveguard.GsonUtilities;
import com.example.driveguard.NetworkManager;
import com.example.driveguard.R;
import com.example.driveguard.Utilities;
import com.example.driveguard.objects.Address;
import com.example.driveguard.objects.DrivingEventsAdapter;
import com.example.driveguard.objects.ServerLocation;
import com.example.driveguard.objects.Trip;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Response;

// once trip is finished, call the scoring from trip.java
// call the score as a dialogue box, once clicked it disappears
// that trip will be passed to the trip history which then displays it as an item (includes start time, end time, start location and end location)

public class ScoreDialog extends DialogFragment {
    private Trip trip;
    private DrivingEventsAdapter drivingEventsAdapter;

    private NetworkManager networkManager;

    public void setTrip(Trip trip) {

       this.trip = trip;

    }

    @SuppressLint({"SetTextI18n", "NewApi", "DefaultLocale"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View dialogView = inflater.inflate(R.layout.dialog_trip_summary, (ViewGroup) container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        networkManager = new NetworkManager(getActivity());

        TextView scoreTextView = dialogView.findViewById(R.id.textViewDriverScore);
        TextView startTimeTextView = dialogView.findViewById(R.id.startTime);
        TextView endTimeTextView = dialogView.findViewById(R.id.endTime);
        TextView startLocationTextView = dialogView.findViewById(R.id.startLocation);
        TextView endLocationTextView = dialogView.findViewById(R.id.endLocation);
        TextView distanceTextView = dialogView.findViewById(R.id.distance);

        if(trip != null) {

            scoreTextView.setText(String.valueOf(trip.getScore()));

            if(trip.getStartTime() != null) {
                startTimeTextView.setText("Started: " + Utilities.formatTime(trip.getStartTime()));
            }

            if (trip.getEndTime() != null) {
                endTimeTextView.setText("Ended: " + Utilities.formatTime(trip.getEndTime()));
            }

            if(trip.getStartLocation() != null) {
                startLocationTextView.setText(formatLocation(trip.getStartLocation()));

            } else {
                startLocationTextView.setText("N/A");

            }

            if(trip.getEndLocation() != null) {
                endLocationTextView.setText(formatLocation(trip.getEndLocation()));

            } else {
                endLocationTextView.setText("N/A");

            }

            distanceTextView.setText(String.format("%.2f", trip.getDistance()) + " km");

            // adding deductions dynamically (some people drive perfectly)

            RecyclerView recyclerViewTripDeductions = dialogView.findViewById(R.id.recyclerViewTripDeductions);
            recyclerViewTripDeductions.setLayoutManager(new LinearLayoutManager(getActivity()));

            if(trip.getDrivingEvents() != null && !trip.getDrivingEvents().isEmpty()) {

                drivingEventsAdapter = new DrivingEventsAdapter(trip.getDrivingEvents(), getActivity());

            } else {

                drivingEventsAdapter = new DrivingEventsAdapter(new ArrayList<>(1), getActivity());

            }
            recyclerViewTripDeductions.setAdapter(drivingEventsAdapter);
        }
        return dialogView;
    }

    public String formatLocation(@NonNull ServerLocation serverLocation) {

        networkManager = new NetworkManager(getActivity());

        try {

            Response response = networkManager.getAddress(serverLocation);

            if(response != null && response.isSuccessful() && response.body() != null) {

                String responseBody = response.body().string();
                Address address = GsonUtilities.JsonToAddress(responseBody);

                if(address != null) {

                    return address.getStreet() + ", " + address.getCity() + ", " + address.getState();

                } else {

                    return "Invalid address data.";

                }
            } else {

                return "Unable to retrieve address";

            }

        } catch (IOException e) {

            return "Error getting address";

        }

    }

    public String formatDate(String time){
        String formattedDate = time;

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSS]");

            // Parse the string into a LocalDateTime
            LocalDateTime parsedDate = LocalDateTime.parse(formattedDate, formatter);

            // Format it back into a standard string (or any other desired format)
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formattedDate = parsedDate.format(outputFormatter);


        } catch (DateTimeException e) {

            formattedDate = "Invalid date format";

        }
        return formattedDate;
    }

   @Override
    public void onStart() {

       super.onStart();
       if(getDialog() != null) {

           Objects.requireNonNull(getDialog().getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

       }

   }

}
