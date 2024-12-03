package com.example.driveguard.objects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveguard.R;
import com.example.driveguard.Utilities;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DrivingEventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_EVENT = 1;
    private final List<DrivingEvent> drivingEvents;
    private final Context context;

    public DrivingEventsAdapter(List<DrivingEvent> drivingEvents, Context context) {

        if(drivingEvents == null) {

            this.drivingEvents = new ArrayList<>(1);

        } else {

            this.drivingEvents = drivingEvents;

        }

        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {

        return drivingEvents.isEmpty() ? VIEW_TYPE_EMPTY : VIEW_TYPE_EVENT;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_empty_deduction, parent, false);
            return new EmptyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_driving_event, parent, false);
            return new EventViewHolder(view);
        }

    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == VIEW_TYPE_EVENT) {
            DrivingEvent event = drivingEvents.get(position);
            EventViewHolder eventHolder = (EventViewHolder) holder;
            eventHolder.eventTypeTextView.setText("Type: " + formatType(event.getEventType()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                eventHolder.eventTimeTextView.setText("Time: " + Utilities.formatTime(event.getEventTime()));
            }
            eventHolder.eventPointTextView.setText("Points deducted: " + event.getPointsDeducted());
        }

    }

    @Override
    public int getItemCount() {
        return drivingEvents != null ? drivingEvents.size() : 0;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventTypeTextView;
        TextView eventPointTextView;
        TextView eventTimeTextView;

        public EventViewHolder (@NonNull View itemView) {

            super(itemView);
            eventTypeTextView = itemView.findViewById(R.id.eventTypeTextView);
            eventPointTextView = itemView.findViewById(R.id.eventPointTextView);
            eventTimeTextView = itemView.findViewById(R.id.eventTimeTextView);

        }

    }
    static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(@NonNull View itemView) {

            super(itemView);

        }

    }

    public String formatType(EventType eventType) {

        if (eventType == null) {

            return "Unknown event";

        }

        switch (eventType) {

            case SPEEDING:
                return "Speeding";
            case HARD_BRAKING:
                return "Hard Braking";
            case HARD_ACCELERATION:
                return "Hard Acceleration";
            case HARD_CORNERING:
                return "Hard Cornering";
            default:
                return "Unknown Event";
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
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

}
