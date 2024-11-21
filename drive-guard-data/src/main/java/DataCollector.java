import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class DataCollector {

    private LocationManager locationManager;
    private SensorManager sensorManager;
    private Location lastLocation;
    private float currentSpeed;
    private float currentGForce;
    private float turningRate;

    public DataCollector(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public Location getStartingLocation()
    {
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    // Initialize location and sensor listeners
    public void startDataCollection() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopDataCollection() {
        locationManager.removeUpdates(locationListener);
        sensorManager.unregisterListener(sensorEventListener);
    }

    // Retrieve the current speed from GPS
    public float getSpeed() {
        return currentSpeed;
    }

    // Retrieve the current g-force from accelerometer data
    public float getAcceleration() {
        return currentGForce;
    }

    // Retrieve the current turning rate from gyroscope data
    public float getTurningRate() {
        return turningRate;
    }

    // Location listener to calculate speed
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (lastLocation != null) {
                float distance = location.distanceTo(lastLocation);
                float timeElapsed = (location.getTime() - lastLocation.getTime()) / 1000.0f; // seconds
                currentSpeed = distance / timeElapsed; // speed in m/s
            }
            lastLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    // Sensor listener to calculate g-force and turning rate
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                currentGForce = (float) Math.sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH;
            } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                float angularSpeedZ = event.values[2]; // rotation rate around the Z axis (yaw)
                turningRate = Math.abs(angularSpeedZ); // taking the absolute value to reflect turn rate
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };
}
