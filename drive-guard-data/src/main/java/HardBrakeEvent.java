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

    /* Method Name: HardBrakeEvent
     * Method Author: Brooke Cronin
     * Description: Constructor for the HardBrakeEvent class. Initializes g-force.
     * Parameters: float gForce (the g-force recorded during braking), long timestamp (the time of the event),
     *             Location location (the location of the event)
     * Returns: N/A
     */
    public HardBrakeEvent(float gForce, long timestamp, Location location)
    {
        super(timestamp, location);
        this.gForce = gForce;
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

    /* Method Name: isHarshBraking
     * Method Author: Brooke Cronin
     * Description: Checks if the braking is considered harsh (g-force greater than 1.0).
     * Parameters: N/A
     * Returns: boolean (true if g-force is above 1.0, indicating harsh braking)
     */
    public boolean isHarshBraking()
    {
        return this.gForce > 1.0;
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
            return (int) (this.getGForce() * 10);
        }
        return 0;
    }

    /* Method Name: logEvent
     * Method Author: Brooke Cronin
     * Description: Logs the details of the hard braking event, including timestamp, location, and g-force.
     * Parameters: N/A
     * Returns: String (formatted details of the hard braking event)
     */
    @Override
    public String logEvent()
    {
        return "Event Type: Hard Braking" + "\nTimestamp: " + this.getTimestamp() +
                "\nLocation: " + this.getLocation() +
                "\nNumber of Points Deducted: " + this.deductPoints() +
                "\nCar G-Force (m/s^2): " + this.getGForce();
    }
}
