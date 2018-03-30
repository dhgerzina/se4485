package blackboard.test;

import java.util.TimeZone;

/**
 * Created by Alek on 3/23/2018.
 */

public class BlackBoard {
    //broadcast update strings
    public static final String BLACKBOARD_UPDATED = "bbtest.BLACKBOARD_UPDATED";
    public static final String TIMEZONE_UPDATED = "bbtest.TIMEZONE_UPDATED";
    public static final String NETWORK_STATUS_UPDATED = "bbtest.NETWORK_STATUS_UPDATED";
    public static final String PRESSURE_UPDATED = "bbtest.PRESSURE_UPDATED";
    public static final String MAGNETOMETER_UPDATED = "bbtest.MAGNETOMETER_UPDATED";
    public static final String MOTION_SENSOR_UPDATED = "bbtest.MOTION_SENSOR_UPDATED";
    public static final String CITY_UPDATED = "bbtest.CITY_UPDATED";
    public static final String ELEVATION_UPDATED = "bbtest.ELEVATION_UPDATED";
    //raw data
    public static TimeZone timeZone = null;
    public static boolean networkStatus = false;
    public static String lastIP = null;
    public static float[] pressure = new float[10];
    public static int pressureCounter = 0;
    public static float[] magnet = null;
    public static float[] motion = null;
    //external data
    public static float weatherElevation = -1; //should be 0 when using sea level pressure
    public static float weatherPressure = -1; //use pressure at sea level if available
    //public static however the map would be set up
    //intermediate data
    public static String city = null;
    public static float normalPressure = -1;
    public static float elevation = -1;
    //location data
    //public static however the location data will be stored
}
