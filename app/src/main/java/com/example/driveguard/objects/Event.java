package com.example.driveguard.objects;

import android.location.Location;


/* Class Name: Event
 * Class Author: Brooke Cronin
 * Date: November 14, 2024
 * Course: SENG71000
 *
 * Description: This abstract class represents a generic event with a timestamp and location.
 *              It provides methods for getting and setting these attributes, and abstract methods
 *              for deducting points and logging events, which should be implemented by subclasses.
 */
public abstract class Event
{
    private String eventTime;
    private ServerLocation location;
    private EventType eventType;
    private EventSeverity severity;
    private Weather weather;
    private int weatherDeduction;


    /* Method Name: Event
     * Method Author: Brooke Cronin
     * Description: Constructor for the Event class. Initializes the timestamp and location.
     * Parameters: long timestamp (the time of the event), Location location (the location of the event)
     * Returns: N/A
     */
    public Event(String timestamp, android.location.Location location, Weather weather)
    {
        this.eventTime = timestamp;
        this.location = new ServerLocation(location.getLatitude(), location.getLongitude());
        this.weather = weather;
        int weatherDeduction = this.getWeather().getWeatherSeverity().ordinal() + 1;
        if (this.getWeather().getWeatherType() == WeatherType.CLEAR || this.getWeather().getWeatherType() == WeatherType.UNKNOWN)
        {
            weatherDeduction = 0;
        }
        else if (this.getWeather().getWeatherType() == WeatherType.FOG || this.getWeather().getWeatherType() == WeatherType.DRIZZLE || this.getWeather().getWeatherType() == WeatherType.SNOW)
        {
            weatherDeduction += 1;
        }
        else if (this.getWeather().getWeatherType() == WeatherType.RAIN)
        {
            weatherDeduction += 2;
        }
        else if (this.getWeather().getWeatherType() == WeatherType.FREEZING_RAIN || this.getWeather().getWeatherType() == WeatherType.FREEZING_DRIZZLE || this.getWeather().getWeatherType() == WeatherType.THUNDERSTORM)
        {
            weatherDeduction += 4;
        }
        this.weatherDeduction = weatherDeduction;

    }

    /* Method Name: getTimestamp
     * Method Author: Brooke Cronin
     * Description: Returns the timestamp of the event.
     * Parameters: N/A
     * Returns: long (the timestamp of the event)
     */
    public String getTimestamp()
    {
        return this.eventTime;
    }

    /* Method Name: setTimestamp
     * Method Author: Brooke Cronin
     * Description: Sets the timestamp of the event.
     * Parameters: long timestamp (the new timestamp of the event)
     * Returns: N/A
     */
    public void setTimestamp(String timestamp)
    {
        this.eventTime = timestamp;
    }

    /* Method Name: getLocation
     * Method Author: Brooke Cronin
     * Description: Returns the location of the event.
     * Parameters: N/A
     * Returns: Location (the location of the event)
     */
    public ServerLocation getLocation()
    {
        return this.location;
    }

    /* Method Name: setLocation
     * Method Author: Brooke Cronin
     * Description: Sets the location of the event.
     * Parameters: Location location (the new location of the event)
     * Returns: N/A
     */
    public void setLocation(android.location.Location location)
    {
        this.location = new ServerLocation(location.getLatitude(), location.getLongitude());
    }

    public Weather getWeather() {
        return this.weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public int getWeatherDeduction() {
        return this.weatherDeduction;
    }

    /* Method Name: deductPoints
     * Method Author: Brooke Cronin
     * Description: Abstract method to deduct points based on the event type.
     *              Must be implemented by subclasses.
     * Parameters: N/A
     * Returns: int (the points deducted based on event type)
     */
    public abstract int deductPoints();
}
