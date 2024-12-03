package com.example.driveguard.objects;

import android.location.Location;

/* Class Name: HardBrakeEvent
 * Class Author: Brooke Cronin
 * Date: November 14, 2024
 * Course: SENG71000
 *
 * Description: This class represents a hard braking event, characterized by a high deceleration or g-force.
 *              It includes methods to check if the braking is harsh, calculate point deductions,
 *              and log event details.
 */
public class HardBrakeEvent extends Event
{
    private float gForce;
    private float brakingRate;

    /* Method Name: HardBrakeEvent
     * Method Author: Brooke Cronin
     * Description: Constructor for the HardBrakeEvent class. Initializes g-force.
     * Parameters: float gForce (the g-force recorded during braking), long timestamp (the time of the event),
     *             Location location (the location of the event)
     * Returns: N/A
     */
    public HardBrakeEvent(float brakingRate, String timestamp, android.location.Location location, Weather weather)
    {
        super(timestamp, location, weather);
        this.brakingRate = brakingRate;
    }

    /* Method Name: getGForce
     * Method Author: Brooke Cronin
     * Description: Returns the g-force recorded during the braking event.
     * Parameters: N/A
     * Returns: float (the g-force in m/s^2)
     */
    public float getGForce()
    {
        return this.gForce;
    }
    public float getBrakingRate () {return this.brakingRate;}

    /* Method Name: isHarshBraking
     * Method Author: Brooke Cronin
     * Description: Checks if the braking is considered harsh (g-force greater than 1.0).
     * Parameters: N/A
     * Returns: boolean (true if g-force is less than 0.4, indicating harsh braking)
     */
    public boolean isHarshBraking()
    {
        return this.brakingRate <= -0.4;
    }

    /* Method Name: deductPoints
     * Method Author: Brooke Cronin
     * Description: Calculates points to be deducted based on the g-force of harsh braking.
     * Parameters: N/A
     * Returns: int (points deducted based on the g-force)
     */
    @Override
    public int deductPoints()
    {
        if (isHarshBraking())
        {
            return (int) (this.brakingRate * -10 + this.getWeatherDeduction());
        }
        return 0;
    }

}
