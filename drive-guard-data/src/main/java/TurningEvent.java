import android.location.Location;

/* Class Name: TurningEvent
 * Class Author: Brooke Cronin
 * Date: November 14, 2024
 * Course: SENG71000
 *
 * Description: This class represents a turning event, characterized by the rate of turning.
 *              It includes methods to check if the turn is aggressive, calculate point deductions,
 *              and log event details.
 */
public class TurningEvent extends Event
{
    private float turningRate;

    /* Method Name: TurningEvent
     * Method Author: Brooke Cronin
     * Description: Constructor for the TurningEvent class. Initializes turning rate.
     * Parameters: float turningRate (the rate of turning in degrees per second), long timestamp (the time of the event),
     *             Location location (the location of the event)
     * Returns: N/A
     */
    public TurningEvent(float turningRate, long timestamp, Location location)
    {
        super(timestamp, location);
        this.turningRate = turningRate;
    }

    /* Method Name: getTurningRate
     * Method Author: Brooke Cronin
     * Description: Returns the turning rate recorded during the event.
     * Parameters: N/A
     * Returns: float (the turning rate in degrees per second)
     */
    public float getTurningRate()
    {
        return this.turningRate;
    }

    /* Method Name: isAggressiveTurn
     * Method Author: Brooke Cronin
     * Description: Checks if the turn is considered aggressive (turning rate greater than 30.0 degrees per second).
     * Parameters: N/A
     * Returns: boolean (true if turning rate is above 30.0, indicating an aggressive turn)
     */
    public boolean isAggressiveTurn()
    {
        return this.turningRate > 30.0;
    }

    /* Method Name: deductPoints
     * Method Author: Brooke Cronin
     * Description: Calculates points to be deducted based on the rate of turning if the turn is aggressive.
     * Parameters: N/A
     * Returns: int (points deducted based on the turning rate)
     */
    @Override
    public int deductPoints()
    {
        if (isAggressiveTurn())
        {
            return (int) (this.turningRate * 0.5);
        }
        return 0;
    }

    /* Method Name: logEvent
     * Method Author: Brooke Cronin
     * Description: Logs the details of the turning event, including timestamp, location, and turning rate.
     * Parameters: N/A
     * Returns: String (formatted details of the turning event)
     */
    @Override
    public String logEvent()
    {
        return "Event Type: Hard Turning" + "\nTimestamp: " + this.getTimestamp() +
                "\nLocation: " + this.getLocation() +
                "\nNumber of Points Deducted: " + this.deductPoints() +
                "\nCar Turning Rate (degrees/s): " + this.getTurningRate();
    }
}
