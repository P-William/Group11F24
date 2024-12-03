package com.example.driveguard.objects;

import android.location.Location;

/* Class Name: HardAccelerateEvent
 * Class Author: Brooke Cronin
 * Date: November 14, 2024
 * Course: SENG71000
 *
 * Description: This class represents a hard acceleration event, characterized by a high g-force.
 *              It includes methods to check if the acceleration is harsh, calculate point deductions,
 *              and log event details.
 */
public class HardAccelerateEvent extends Event
{
    private float gForce;

    /* Method Name: HardAccelerateEvent
     * Method Author: Brooke Cronin
     * Description: Constructor for the HardAccelerateEvent class. Initializes g-force.
     * Parameters: float gForce (the g-force recorded during acceleration), long timestamp (the time of the event),
     *             Location location (the location of the event)
     * Returns: N/A
     */
    public HardAccelerateEvent(float gForce, String timestamp, android.location.Location location, Weather weather)
    {
        super(timestamp, location, weather);
        this.gForce = gForce;
    }

    /* Method Name: getGForce
     * Method Author: Brooke Cronin
     * Description: Returns the g-force recorded during the event.
     * Parameters: N/A
     * Returns: float (the g-force in m/s^2)
     */
    public float getGForce()
    {
        return this.gForce;
    }

    /* Method Name: isHarshAcceleration
     * Method Author: Brooke Cronin
     * Description: Checks if the acceleration is considered harsh (g-force greater than 1.5).
     * Parameters: N/A
     * Returns: boolean (true if g-force is above 1.5, indicating harsh acceleration)
     */
    public boolean isHarshAcceleration()
    {
        return this.getGForce() > 1.5;
    }

    /* Method Name: deductPoints
     * Method Author: Brooke Cronin
     * Description: Calculates points to be deducted based on the g-force of harsh acceleration.
     * Parameters: N/A
     * Returns: int (points deducted based on the g-force)
     */
    @Override
    public int deductPoints()
    {
        if (isHarshAcceleration())
        {
            return (int) (this.gForce * 4 + this.getWeatherDeduction());
        }
        return 0;
    }
}
