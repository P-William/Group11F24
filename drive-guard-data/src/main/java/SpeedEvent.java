import android.location.Location;

/* Class Name: SpeedEvent
 * Class Author: Brooke Cronin
 * Date: November 14, 2024
 * Course: SENG71000
 *
 * Description: This class represents a speed-related event that tracks the speed of a vehicle
 *              and checks if it is exceeding the posted speed limit. It includes methods to
 *              calculate point deductions and log event details.
 */
public class SpeedEvent extends Event
{
    private float speed;
    private float postedSpeedLimit;

    /* Method Name: SpeedEvent
     * Method Author: Brooke Cronin
     * Description: Constructor for the SpeedEvent class. Initializes speed and posted speed limit.
     * Parameters: float speed (the recorded speed), long timestamp (the time of the event),
     *             float postedSpeedLimit (the speed limit for the event location), Location location (the event location)
     * Returns: N/A
     */
    public SpeedEvent(float speed, long timestamp, float postedSpeedLimit, Location location)
    {
        super(timestamp, location);
        this.speed = speed;
        this.postedSpeedLimit = postedSpeedLimit;
        this.postedSpeedLimitAllowance = this.postedSpeedLimit + 10;
    }

    /* Method Name: getSpeed
     * Method Author: Brooke Cronin
     * Description: Returns the speed recorded during the event.
     * Parameters: N/A
     * Returns: float (the speed of the vehicle in km/h)
     */
    public float getSpeed()
    {
        return this.speed;
    }

    /* Method Name: getPostedSpeedLimit
     * Method Author: Brooke Cronin
     * Description: Returns the posted speed limit for the event.
     * Parameters: N/A
     * Returns: float (the posted speed limit in km/h)
     */
    public float getPostedSpeedLimit()
    {
        return this.postedSpeedLimit;
    }

    /* Method Name: getPostedSpeedLimitAllowance
     * Method Author: Brooke Cronin
     * Description: Returns the posted speed limit + 10 for the event.
     * Parameters: N/A
     * Returns: float (the posted speed limit allowance in km/h)
     */
    public float getPostedSpeedLimitAllowance()
    {
        return this.postedSpeedLimitAllowance;
    }

    /* Method Name: isSpeeding
     * Method Author: Brooke Cronin
     * Description: Checks if the speed recorded exceeds the posted speed limit.
     * Parameters: N/A
     * Returns: boolean (true if speed is over the limit, false otherwise)
     */
    public boolean isSpeeding()
    {
        return this.getSpeed() > this.getPostedSpeedLimitAllowance();
    }

    /* Method Name: deductPoints
     * Method Author: Brooke Cronin
     * Description: Calculates points to be deducted based on the speed over the posted limit allowance.
     * Parameters: N/A
     * Returns: int (points to be deducted based on the level of speeding)
     */
    @Override
    public int deductPoints()
    {
        if (isSpeeding())
        {
            return (int) ((this.getSpeed() - this.getPostedSpeedLimitAllowance()) / 5);
        }
        return 0;
    }

    /* Method Name: logEvent
     * Method Author: Brooke Cronin
     * Description: Logs the details of the speed event, including timestamp, location, speed, and posted speed limit.
     * Parameters: N/A
     * Returns: String (formatted details of the speed event)
     */
    @Override
    public String logEvent()
    {
        return "Event Type: Speeding" + "\nTimestamp: " + this.getTimestamp() +
                "\nLocation: " + this.getLocation() +
                "\nNumber of Points Deducted: " + this.deductPoints() +
                "\nCar Speed (km/h): " + this.getSpeed() +
                "\nPosted Speed Limit (km/h): " + this.getPostedSpeedLimit();
    }
}
