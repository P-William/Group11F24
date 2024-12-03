import java.util.ArrayList;
import java.util.List;

/* Class Name: DataClassifier
 * Class Author: Brooke Cronin
 * Date: November 14, 2024
 * Course: SENG71000
 *
 * Description: This class is responsible for classifying driving events based on data
 *              inputs such as speed, g-force, and turning rate, and categorizing them
 *              into speed, acceleration, braking, and turning events.
 */
public class DataClassifier
{

    private float postedSpeedLimit;
    private List<SpeedEvent> speedEvents;
    private List<AccelerateEvent> accelerateEvents;
    private List<BrakeEvent> brakeEvents;
    private List<TurningEvent> turningEvents;

    /* Method Name: DataClassifier
     * Method Author: Brooke Cronin
     * Description: Constructor for the DataClassifier class. Initializes event lists and sets posted speed limit.
     * Parameters: float postedSpeedLimit (the speed limit to be used for speed classification)
     * Returns: N/A
     */
    public DataClassifier(float postedSpeedLimit)
    {
        this.postedSpeedLimit = postedSpeedLimit;
        this.speedEvents = new ArrayList<>();
        this.accelerateEvents = new ArrayList<>();
        this.brakeEvents = new ArrayList<>();
        this.turningEvents = new ArrayList<>();
    }

    /* Method Name: classifyData
     * Method Author: Brooke Cronin
     * Description: Classifies data based on speed, g-force, and turning rate. Adds events to respective lists if criteria are met.
     * Parameters: float speed (current speed), float gForce (current g-force), float turningRate (current turning rate),
     *             long timestamp (time of event)
     * Returns: N/A
     */
    public void classifyData(float speed, float gForce, float turningRate, long timestamp)
    {
        // Classify Speed Event
        if (speed > this.postedSpeedLimit)
        {
            SpeedEvent speedEvent = new SpeedEvent(speed, timestamp, this.postedSpeedLimit);
            if (speedEvent.isSpeeding())
            {
                this.speedEvents.add(speedEvent);
            }
        }

        // Classify Acceleration Event
        AccelerateEvent accelerateEvent = new AccelerateEvent(gForce, timestamp);
        if (accelerateEvent.isHarshAcceleration())
        {
            this.accelerateEvents.add(accelerateEvent);
        }

        // Classify Braking Event
        BrakeEvent brakeEvent = new BrakeEvent(gForce, timestamp);
        if (brakeEvent.isHarshBraking())
        {
            this.brakeEvents.add(brakeEvent);
        }

        // Classify Turning Event
        TurningEvent turningEvent = new TurningEvent(turningRate, timestamp);
        if (turningEvent.isAggressiveTurn())
        {
            this.turningEvents.add(turningEvent);
        }
    }

    /* Method Name: getSpeedEvents
     * Method Author: Brooke Cronin
     * Description: Returns the list of speed events.
     * Parameters: N/A
     * Returns: List<SpeedEvent> (the list of speed events)
     */
    public List<SpeedEvent> getSpeedEvents()
    {
        return this.speedEvents;
    }

    /* Method Name: getAccelerateEvents
     * Method Author: Brooke Cronin
     * Description: Returns the list of acceleration events.
     * Parameters: N/A
     * Returns: List<AccelerateEvent> (the list of acceleration events)
     */
    public List<AccelerateEvent> getAccelerateEvents()
    {
        return this.accelerateEvents;
    }

    /* Method Name: getBrakeEvents
     * Method Author: Brooke Cronin
     * Description: Returns the list of braking events.
     * Parameters: N/A
     * Returns: List<BrakeEvent> (the list of braking events)
     */
    public List<BrakeEvent> getBrakeEvents()
    {
        return this.brakeEvents;
    }

    /* Method Name: getTurningEvents
     * Method Author: Brooke Cronin
     * Description: Returns the list of turning events.
     * Parameters: N/A
     * Returns: List<TurningEvent> (the list of turning events)
     */
    public List<TurningEvent> getTurningEvents()
    {
        return this.turningEvents;
    }
}
