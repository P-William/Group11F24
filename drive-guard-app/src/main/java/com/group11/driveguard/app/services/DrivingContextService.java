package com.group11.driveguard.app.services;

import com.group11.driveguard.api.map.Address;
import com.group11.driveguard.api.map.Location;
import com.group11.driveguard.api.map.LocationPair;
import com.group11.driveguard.api.map.Road;
import com.group11.driveguard.api.weather.CurrentWeather;
import com.group11.driveguard.api.weather.Weather;
import com.group11.driveguard.app.exceptions.UnexpectedMapApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrivingContextService {

    private final RestClient restClient;

    @Value("${config.map-api-url}")
    private String MAP_API_BASE_URL;

    private <T, U> U callApi(String uri, T body, Class<U> responseType) {
        final var response = restClient.post()
            .uri(uri)
            .body(body)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                (clientRequest, clientResponse) -> {
                    throw new UnexpectedMapApiException("Error getting address from coordinates. Status: %s\nStatus Text: %s".formatted(clientResponse.getStatusCode(), clientResponse.getStatusText()));
                }
            )
            .toEntity(responseType);

        return response.getBody();
    }

    public Address getAddressFromCoordinates(Location location) {
        String uri = MAP_API_BASE_URL + "/map/address";

        return callApi(uri, location, Address.class);
    }

    public Road getRoadFromCoordinates(Location location) {
        String uri = MAP_API_BASE_URL + "/map/road";

        return callApi(uri, location, Road.class);
    }

    public Weather getWeatherFromCoordinates(Location location) {
        String baseUrl = "https://api.open-meteo.com/v1/forecast";
        String locationQuery = "?latitude=%s&longitude=%s".formatted(location.latitude(), location.longitude());
        String currentWeatherQuery = String.join(",", List.of(
            "is_day",
            "weather_code"
        ));
        String timeZone = "&timezone=America/New_York";

        String uri = baseUrl + locationQuery + "&current=" + currentWeatherQuery + timeZone;

        final var response = restClient.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                (clientRequest, clientResponse) -> {
                    throw new UnexpectedMapApiException("Error getting weather from coordinates. Status: %s\nStatus Text: %s".formatted(clientResponse.getStatusCode(), clientResponse.getStatusText()));
                }
            )
            .body(CurrentWeather.class);

        if (response == null || response.current() == null) {
            throw new UnexpectedMapApiException("Error getting weather from coordinates. Current weather is null.");
        }

        return Weather.fromCurrentWeather(response);
    }

    public double getDistanceBetweenCoordinates(Location locationOne, Location locationTwo) {
        return getDistanceBetweenCoordinates(new LocationPair(locationOne, locationTwo));
    }

    public double getDistanceBetweenCoordinates(LocationPair locationPair) {
        String uri = MAP_API_BASE_URL + "/map/distance";

        return callApi(uri, locationPair, Double.class);
    }
}