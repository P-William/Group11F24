package com.example.driveguard.objects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveguard.GsonUtilities;
import com.example.driveguard.NetworkManager;
import com.example.driveguard.R;
import com.example.driveguard.activities.ScoreDialog;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.TripHistoryViewHolder> {

    private final List<CompletedTrip> completedTrips;
    private final Context context;

    private NetworkManager networkManager;

    public interface OnTripClickListener {
        void onTripClick(CompletedTrip trip);

    }

    public TripHistoryAdapter(List<CompletedTrip> completedTrips, Context context) {

        this.completedTrips = completedTrips;
        this.context = context;

    }

    @NonNull
    @Override
    public TripHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_completed_trip_button, parent, false);
        return new TripHistoryViewHolder(view);

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull TripHistoryViewHolder holder, int position) {

        CompletedTrip completedTrip = completedTrips.get(position);

        holder.tripSummaryButton.setText("Duration: " + completedTrip.getDuration().getFormattedTime() + "\nScore: " + completedTrip.getScore() + "\nDistance: " + String.format("%.2f", completedTrip.getDistanceKM()) + " km");

        networkManager = new NetworkManager(context.getApplicationContext());

        holder.tripSummaryButton.setOnClickListener(v -> {

            Response response = networkManager.getTripSummary(completedTrip.getId());

            if(response.isSuccessful()) {

                Trip trip;

                try {
                    trip = GsonUtilities.JsonToTrip(response.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if(trip != null) {

                    ScoreDialog dialog = new ScoreDialog();
                    dialog.setTrip(trip);
                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "TrpSummaryDialog");

                }

            }

        });

    }


    @Override
    public int getItemCount(){

        return completedTrips.size();

    }

    public static class TripHistoryViewHolder extends RecyclerView.ViewHolder {

        Button tripSummaryButton;
        public TripHistoryViewHolder(@NonNull View itemView) {

            super(itemView);
            tripSummaryButton = itemView.findViewById(R.id.btnTripSummary);

        }

    }

}
