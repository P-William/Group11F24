package com.example.driveguard.objects;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.time.Duration;

public class CompletedTripAdapter extends TypeAdapter<CompletedTrip> {
    @Override
    public void write(JsonWriter out, CompletedTrip value) throws IOException {

    }

    @Override
    public CompletedTrip read(JsonReader in) throws IOException {
        CompletedTrip trip = new CompletedTrip();
        in.beginObject();

        while (in.hasNext()){
            switch (in.nextName()){
                case "id":
                    trip.setId(in.nextInt());
                    break;
                case "driverId":
                    trip.setDriverId(in.nextInt());
                    break;
                case "score":
                    trip.setScore(in.nextInt());
                    break;
                case "distanceKM":
                    trip.setDistanceKM(in.nextDouble());
                    break;
                case "duration":
                    trip.setDuration(readDuration(in));
                    break;
            }
        }
        in.endObject();
        return trip;
    }

    private TripLength readDuration(JsonReader in) throws IOException{
       String duration = in.nextString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Duration durationParsed = Duration.parse(duration);
            return new TripLength((int) durationParsed.getSeconds());
        }
        else {
            return new TripLength();
        }
    }

}


