package blackboard.test2;

import java.util.TimeZone;
import java.util.concurrent.Semaphore;

public class BlackBoard {
    //api keys
    public static String OPEN_WEATHER_MAPS_APIKEY = "2743b78ac50d9fda94e0d4f735740196";
    //broadcast update strings
    //public static final String BLACKBOARD_UPDATED = "bbtest.BLACKBOARD_UPDATED";
    public static final String TIMEZONE_UPDATED = "bbtest.TIMEZONE_UPDATED";
    public static final String NETWORK_STATUS_UPDATED = "bbtest.NETWORK_STATUS_UPDATED";
    public static final String PRESSURE_UPDATED = "bbtest.PRESSURE_UPDATED";
    public static final String MAGNETOMETER_UPDATED = "bbtest.MAGNETOMETER_UPDATED";
    public static final String ACCELEROMETER_UPDATED = "bbtest.MOTION_SENSOR_UPDATED";
    public static final String CITY_UPDATED = "bbtest.CITY_UPDATED";
    public static final String ELEVATION_UPDATED = "bbtest.ELEVATION_UPDATED";
    //types
    public static final String NETWORK_STATUS_NONE = "None";
    public static final String NETWORK_STATUS_CELLULAR = "Cellular";
    public static final String NETWORK_STATUS_WIFI = "WiFi";
    //update stuff
    public static final int PRESSURE_UPDATE_TIME = 1000;
    public static final int PRESSURE_UPDATE_WAIT = 9000;
    public static final int PRESSURE_ARRAY_SIZE = 10; //can't get more than this many pressure sensor updates in PRESSURE_UPDATE_TIME
    //public static final int MAGNETOMETER_ARRAY_SIZE = 50;
    //public static final int ACCELEROMETER_ARRAY_SIZE = 50;
    public static int pressureCounter = 0;
    //public static int magnetometerCounter = 0;
    //public static int accelerometerCounter = 0;
    //raw data
    public static TimeZone timeZone = null;
    public static String networkStatus = NETWORK_STATUS_NONE;
    public static String lastIP = null;
    public static float[] pressureArray = new float[PRESSURE_ARRAY_SIZE];
    //public static float[][] magnetArray = new float[MAGNETOMETER_ARRAY_SIZE][];
    //public static float[][] accelArray = new float[MOTION_ARRAY_SIZE][];
    //external data
    //public static float weatherElevation = 0; //should be 0 when using sea level pressure
    public static float weatherPressure = 1000; //use pressure at sea level if available
    //public static however the map would be set up
    //intermediate data
    public static String city = null;
    public static String countryCode = null;
    public static float pressure = -1;
    public static float[] magnetometer = new float[3];
    public static float[] accelerometer = new float[3];
    public static float elevation = -1; //in meters
    //location data
    //public static however the location data will be stored
    public static boolean locationKnown = false;
    public static double latitude = 0;
    public static double longitude = 0;
}
