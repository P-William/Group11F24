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
    private long timestamp;
    private Location location;

    /* Method Name: Event
     * Method Author: Brooke Cronin
     * Description: Constructor for the Event class. Initializes the timestamp and location.
     * Parameters: long timestamp (the time of the event), Location location (the location of the event)
     * Returns: N/A
     */
    public Event(long timestamp, Location location)
    {
        this.timestamp = timestamp;
        this.location = location;
    }

    /* Method Name: getTimestamp
     * Method Author: Brooke Cronin
     * Description: Returns the timestamp of the event.
     * Parameters: N/A
     * Returns: long (the timestamp of the event)
     */
    public long getTimestamp()
    {
        return this.timestamp;
    }

    /* Method Name: setTimestamp
     * Method Author: Brooke Cronin
     * Description: Sets the timestamp of the event.
     * Parameters: long timestamp (the new timestamp of the event)
     * Returns: N/A
     */
    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    /* Method Name: getLocation
     * Method Author: Brooke Cronin
     * Description: Returns the location of the event.
     * Parameters: N/A
     * Returns: Location (the location of the event)
     */
    public Location getLocation()
    {
        return this.location;
    }

    /* Method Name: setLocation
     * Method Author: Brooke Cronin
     * Description: Sets the location of the event.
     * Parameters: Location location (the new location of the event)
     * Returns: N/A
     */
    public void setLocation(Location location)
    {
        this.location = location;
    }

    /* Method Name: deductPoints
     * Method Author: Brooke Cronin
     * Description: Abstract method to deduct points based on the event type.
     *              Must be implemented by subclasses.
     * Parameters: N/A
     * Returns: int (the points deducted based on event type)
     */
    public abstract int deductPoints();

    /* Method Name: logEvent
     * Method Author: Brooke Cronin
     * Description: Abstract method to log the event details. Must be implemented by subclasses.
     * Parameters: N/A
     * Returns: String (the log details of the event)
     */
    public abstract String logEvent();
}
