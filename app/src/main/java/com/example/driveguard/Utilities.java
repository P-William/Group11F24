package com.example.driveguard;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.driveguard.objects.Credentials;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class Utilities {
    public static Credentials LoadCredentials(@NonNull Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        return new Credentials(preferences.getInt("driverId", -1), preferences.getString("token", ""), preferences.getInt("tripId", -1));
    }
    public static void SaveCredentials(@NonNull Context context, @NonNull Credentials credentials){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("driverId", credentials.getDriverId());
        editor.putString("token", credentials.getToken());
        if (credentials.getTripId() != -1){
            editor.putInt("tripId", credentials.getTripId());
        }
        editor.apply();
    }
    public static void SaveTripID(@NonNull Context context, int tripId){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("tripId", tripId);
        editor.apply();
    }
    public static int LoadTripID(@NonNull Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        return preferences.getInt("tripId", -1);
    }
    public static void ResetTripId(@NonNull Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("tripId", -1);
        editor.apply();
    }
    public static boolean CheckNotifications(@NonNull Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        return preferences.getBoolean("notifications", false);
    }
    public static boolean CheckLoggedIn(@NonNull Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        int id = preferences.getInt("driverId", -1);
        String token = preferences.getString("token", "");
        if (id == -1 || token.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean CheckDataCollection(@NonNull Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        return preferences.getBoolean("dataCollection", true);
    }
    public static void DeleteCredentials(@NonNull Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("driverId");
        editor.remove("token");
        editor.remove("tripId");
        editor.apply();
    }
    public static void applyDarkModeIfNeeded(SharedPreferences preferences) {
        boolean darkMode = preferences.getBoolean("darkMode", false);
        int currentMode = AppCompatDelegate.getDefaultNightMode();

        if (darkMode && currentMode != AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (!darkMode && currentMode != AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    public static boolean checkConnection(@NonNull Activity activity){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null){
            return false;
        }
        else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            return networkInfo.isConnectedOrConnecting();
        }
        else {
            return false;
        }
    }
    public static String formatTime(String time) {
        try {
            // Parse the input string to LocalDateTime
            LocalDateTime timeDate = LocalDateTime.parse(time);

            // Formatter to exclude milliseconds
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm:ss a", Locale.CANADA);

            return timeDate.format(formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format: " + time, e);
        }
    }
}
