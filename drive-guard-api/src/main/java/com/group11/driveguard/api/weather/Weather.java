package com.group11.driveguard.api.weather;

import com.group11.driveguard.api.trip.event.WeatherType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Weather {
    @NonNull
    private final LocalDateTime timestamp;
    @NonNull
    private final Boolean isDay;
    @NonNull
    private final WeatherType weatherType;
    @NonNull
    private final WeatherType.WeatherSeverity weatherSeverity;

    public static Weather fromCurrentWeather(CurrentWeather currentWeather) {
        CurrentWeather.Current current = currentWeather.current();

        WeatherType weatherType = WeatherType.fromCode(current.weatherCode());
        WeatherType.WeatherSeverity weatherSeverity = WeatherType.WeatherSeverity.fromCode(current.weatherCode());
        return new Weather(LocalDateTime.parse(current.time()), current.isDay(), weatherType, weatherSeverity);
    }
}
