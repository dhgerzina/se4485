package blackboard.test2;

import android.support.annotation.NonNull;

import java.util.TimeZone;

public class BlackBoard {
    //CONSTANTS
    //api keys (Google api key is found in res/values/google_maps_api.xml)
    public static final String OPEN_WEATHER_MAPS_APIKEY = "2743b78ac50d9fda94e0d4f735740196";
    //broadcast update strings
    //public static final String BLACKBOARD_UPDATED = "bbtest.BLACKBOARD_UPDATED";
    public static final String TIMEZONE_UPDATED = "bbtest.TIMEZONE_UPDATED";
    public static final String NETWORK_STATUS_UPDATED = "bbtest.NETWORK_STATUS_UPDATED";
    public static final String PRESSURE_UPDATED = "bbtest.PRESSURE_UPDATED";
    public static final String MAGNETOMETER_UPDATED = "bbtest.MAGNETOMETER_UPDATED";
    public static final String ACCELEROMETER_UPDATED = "bbtest.MOTION_SENSOR_UPDATED";
    public static final String CITY_UPDATED = "bbtest.CITY_UPDATED";
    public static final String ELEVATION_UPDATED = "bbtest.ELEVATION_UPDATED";
    //types (specifically the different types of network statuses)
    public static final String NETWORK_STATUS_NONE = "None";
    public static final String NETWORK_STATUS_CELLULAR = "Cellular";
    public static final String NETWORK_STATUS_WIFI = "WiFi";
    //data storage array sizes
    public static final int PRESSURE_ARRAY_SIZE = 5;
    public static final int MAGNETOMETER_ARRAY_SIZE = 5;
    public static final int ACCELEROMETER_ARRAY_SIZE = 5;
    public static final int TIMEZONE_ARRAY_SIZE = 2;
    public static final int NETWORK_STATUS_ARRAY_SIZE = 5;
    public static final int IP_ARRAY_SIZE = 5;
    //public static final int WEATHER_PRESSURE_ARRAY_SIZE = 5;
    public static final int CITY_ARRAY_SIZE = 5;
    public static final int COUNTRY_ARRAY_SIZE = 5;
    public static final int ELEVATION_ARRAY_SIZE = 5;
    public static final int LOCATION_ARRAY_SIZE = 5;
    //other
    public static final int ROUND_DECIMAL_PLACES = 2;

    //secondary data updates
    //the android sensors update far more often than necessary
    //so these are used to temporarily hold data to be averaged out
    //pressure is unique in that it does not need to be checked constantly
    public static final int PRESSURE_UPDATE_TIME = 1000;
    public static final int PRESSURE_UPDATE_WAIT = 9000;
    public static final int SECONDARY_PRESSURE_ARRAY_SIZE = 10; //can't get more than this many pressure sensor updates in PRESSURE_UPDATE_TIME
    //public static final int MAGNETOMETER_ARRAY_SIZE = 50;
    //public static final int ACCELEROMETER_ARRAY_SIZE = 50;
    public static float[] pressureArray = new float[SECONDARY_PRESSURE_ARRAY_SIZE];
    //public static float[][] magnetArray = new float[MAGNETOMETER_ARRAY_SIZE][];
    //public static float[][] accelArray = new float[MOTION_ARRAY_SIZE][];
    public static int pressureCounter = 0;
    //public static int magnetometerCounter = 0;
    //public static int accelerometerCounter = 0;

    //raw data
    private static double[] pressure = new double[PRESSURE_ARRAY_SIZE];
    private static int pressureCurrentLoc = -1;
    private static double[][] magnetometer = new double[MAGNETOMETER_ARRAY_SIZE][3];
    private static int magnetometerCurrentLoc = -1;
    private static double[][] accelerometer = new double[ACCELEROMETER_ARRAY_SIZE][3];
    private static int accelerometerCurrentLoc = -1;
    private static TimeZone[] timeZone = new TimeZone[TIMEZONE_ARRAY_SIZE];
    private static int timezoneCurrentLoc = -1;
    private static String[] networkStatus = new String[NETWORK_STATUS_ARRAY_SIZE];
    private static int networkStatusCurrentLoc = -1;
    private static String[] ip = new String[IP_ARRAY_SIZE];
    private static int ipCurrentLoc = -1;
    //external data
    private static double weatherPressure = -1; //use pressure at sea level, old values do not need to be stored
    //TODO: get map data

    //intermediate data
    private static String[] city = new String[CITY_ARRAY_SIZE];
    private static int cityCurrentLoc = -1;
    private static String[] countryCode = new String[COUNTRY_ARRAY_SIZE];
    private static int countryCurrentLoc = -1;
    private static double[] elevation = new double[ELEVATION_ARRAY_SIZE]; //in meters
    private static int elevationCurrentLoc = -1;
    //TODO: data for comparing motion data to map

    //location data
    private static double[][] latlon = new double[LOCATION_ARRAY_SIZE][2];
    private static int latlonCurrentLoc = -1;

    //get current data
    public static double getCurrentPressure () {
        if (pressureCurrentLoc != -1) {return pressure[pressureCurrentLoc];}
        else {return -1;}
    }
    public static double getCurrentPressureRounded () {
        if (pressureCurrentLoc != -1) {
            double temp;
            double p = getCurrentPressure();
                temp = p * Math.pow(10, ROUND_DECIMAL_PLACES);
                temp = Math.round(temp);
                temp /= Math.pow(10, ROUND_DECIMAL_PLACES);
                return temp;
        }
        else {return -1;}
    }
    public static double[] getCurrentMagnetometer () {
        if (magnetometerCurrentLoc != -1) {return magnetometer[magnetometerCurrentLoc];}
        else {return null;}
    }
    public static double[] getCurrentMagnetometerRounded () {
        if (magnetometerCurrentLoc != -1) {
            double[] temp = new double[3];
            double[] acc = getCurrentMagnetometer();
            for (int i = 0; i < temp.length; i++) {
                temp[i] = acc[i] * Math.pow(10, ROUND_DECIMAL_PLACES);
                temp[i] = Math.round(temp[i]);
                temp[i] /= Math.pow(10, ROUND_DECIMAL_PLACES);
            }
            return temp;
        }
        else {return null;}
    }
    public static double[] getCurrentAccelerometer () {
        if (accelerometerCurrentLoc != -1) {return accelerometer[accelerometerCurrentLoc];}
        else {return null;}
    }
    public static double[] getCurrentAccelerometerRounded () {
        if (accelerometerCurrentLoc != -1) {
            double[] temp = new double[3];
            double[] acc = getCurrentAccelerometer();
            for (int i = 0; i < temp.length; i++) {
                temp[i] = acc[i] * Math.pow(10, ROUND_DECIMAL_PLACES);
                temp[i] = Math.round(temp[i]);
                temp[i] /= Math.pow(10, ROUND_DECIMAL_PLACES);
            }
            return temp;
        }
        else {return null;}
    }
    public static TimeZone getCurrentTimeZone () {
        if (timezoneCurrentLoc != -1) {return timeZone[timezoneCurrentLoc];}
        else {return null;}
    }
    public static String getCurrentNetworkStatus () {
        if (networkStatusCurrentLoc != -1) {return networkStatus[networkStatusCurrentLoc];}
        else {return null;}
    }
    public static String getCurrentIP () {
        if (ipCurrentLoc != -1) {return ip[ipCurrentLoc];}
        else {return null;}
    }
    public static double getCurrentWeatherPressure () {
        return weatherPressure;
    }
    public static double getCurrentWeatherPressureRounded () {
        double temp;
        double p = getCurrentWeatherPressure();
        temp = p * Math.pow(10, ROUND_DECIMAL_PLACES);
        temp = Math.round(temp);
        temp /= Math.pow(10, ROUND_DECIMAL_PLACES);
        return temp;
    }
    public static String getCurrentCity () {
        if (cityCurrentLoc != -1) {return city[cityCurrentLoc];}
        else {return null;}
    }
    public static String getCurrentCountryCode () {
        if (countryCurrentLoc != -1) {return countryCode[countryCurrentLoc];}
        else {return null;}
    }
    public static double getCurrentElevation () {
        if (elevationCurrentLoc != -1) {return elevation[elevationCurrentLoc];}
        else {return -1;}
    }
    public static double getCurrentElevationRounded () {
        if (elevationCurrentLoc != -1) {
            double temp;
            double p = getCurrentElevation();
            temp = p * Math.pow(10, ROUND_DECIMAL_PLACES);
            temp = Math.round(temp);
            temp /= Math.pow(10, ROUND_DECIMAL_PLACES);
            return temp;
        }
        else {return -1;}
    }
    public static double[] getCurrentLocation () {
        if (latlonCurrentLoc != -1) {return latlon[latlonCurrentLoc];}
        else {return null;}
    }

    //get past data
    public static double[] getMagnetometer (int relativeIndex) {
        if (magnetometerCurrentLoc != -1) {
            int index = magnetometerCurrentLoc + relativeIndex;
            if (index < 0) {index += MAGNETOMETER_ARRAY_SIZE;}
            return magnetometer[index];
        }
        else {return null;}
    }
    public static double[] getMagnetometerRounded (int relativeIndex) {
        if (magnetometerCurrentLoc != -1) {
            double[] temp = new double[3];
            double[] acc = getMagnetometer(relativeIndex);
            for (int i = 0; i < temp.length; i++) {
                temp[i] = acc[i] * Math.pow(10, ROUND_DECIMAL_PLACES);
                temp[i] = Math.round(temp[i]);
                temp[i] /= Math.pow(10, ROUND_DECIMAL_PLACES);
            }
            return temp;
        }
        else {return null;}
    }
    public static double[] getAccelerometer (int relativeIndex) {
        if (accelerometerCurrentLoc != -1) {
            int index = accelerometerCurrentLoc + relativeIndex;
            if (index < 0) {index += ACCELEROMETER_ARRAY_SIZE;}
            return accelerometer[index];
        }
        else {return null;}
    }
    public static double[] getAccelerometerRounded (int relativeIndex) {
        if (accelerometerCurrentLoc != -1) {
            double[] temp = new double[3];
            double[] acc = getAccelerometer(relativeIndex);
            for (int i = 0; i < temp.length; i++) {
                temp[i] = acc[i] * Math.pow(10, ROUND_DECIMAL_PLACES);
                temp[i] = Math.round(temp[i]);
                temp[i] /= Math.pow(10, ROUND_DECIMAL_PLACES);
            }
            return temp;
        }
        else {return null;}
    }

    public static void updatePressure (double p) {
        pressureCurrentLoc++;
        if (pressureCurrentLoc >= PRESSURE_ARRAY_SIZE) {pressureCurrentLoc = 0;}
        pressure[pressureCurrentLoc] = p;
    }
    public static void updateMagnetometer (double[] m) {
        magnetometerCurrentLoc++;
        if (magnetometerCurrentLoc >= MAGNETOMETER_ARRAY_SIZE) {magnetometerCurrentLoc = 0;}
        magnetometer[magnetometerCurrentLoc] = m.clone();
    }
    public static void updateAccelerometer (double[] a) {
        accelerometerCurrentLoc++;
        if (accelerometerCurrentLoc >= ACCELEROMETER_ARRAY_SIZE) {accelerometerCurrentLoc = 0;}
        accelerometer[accelerometerCurrentLoc] = a.clone();
    }
    public static void updateTimeZone (TimeZone tz) {
        timezoneCurrentLoc++;
        if (timezoneCurrentLoc >= TIMEZONE_ARRAY_SIZE) {timezoneCurrentLoc = 0;}
        timeZone[timezoneCurrentLoc] = tz;
    }
    public static void updateNetworkStatus (String ns) {
        networkStatusCurrentLoc++;
        if (networkStatusCurrentLoc >= NETWORK_STATUS_ARRAY_SIZE) {networkStatusCurrentLoc = 0;}
        networkStatus[networkStatusCurrentLoc] = ns;
    }
    public static void updateIP (String ip) {
        ipCurrentLoc++;
        if (ipCurrentLoc >= IP_ARRAY_SIZE) {ipCurrentLoc = 0;}
        BlackBoard.ip[ipCurrentLoc] = ip;
    }
    public static void updateWeatherPressure (double p) {
        weatherPressure = p;
    }
    public static void updateCity (String c) {
        cityCurrentLoc++;
        if (cityCurrentLoc >= CITY_ARRAY_SIZE) {cityCurrentLoc = 0;}
        city[cityCurrentLoc] = c;
    }
    public static void updateCountry (String c) {
        countryCurrentLoc++;
        if (countryCurrentLoc >= COUNTRY_ARRAY_SIZE) {countryCurrentLoc = 0;}
        countryCode[countryCurrentLoc] = c;
    }
    public static void updateElevation (double e) {
        elevationCurrentLoc++;
        if (elevationCurrentLoc >= ELEVATION_ARRAY_SIZE) {elevationCurrentLoc = 0;}
        elevation[elevationCurrentLoc] = e;
    }
    public static void updateLocation (double[] ll) {
        //add new location even if null
        //null means location is unknown
        latlonCurrentLoc++;
        if (latlonCurrentLoc >= LOCATION_ARRAY_SIZE) {latlonCurrentLoc = 0;}
        latlon[latlonCurrentLoc] = ll;
    }
}
