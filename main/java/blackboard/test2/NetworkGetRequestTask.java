package blackboard.test2;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkGetRequestTask extends AsyncTask {
    public static final int IP_LOCATE_ID = 0;
    public static final int GET_PRESSURE_ID = 1;

    public static String IP_API_BASE_URL = "http://ip-api.com/json";
    public static String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static String WEATHER_APIKEY_APPEND = "&APPID=";

    @Override
    protected Object doInBackground(Object[] objects) {
        if ((Integer)(objects[0]) == IP_LOCATE_ID) {
            locateIP();
        }
        else if ((Integer)(objects[0]) == GET_PRESSURE_ID) {
            getPressure();
        }
        return null;
    }

    public static void locateIP () {
        try {
            String data = requestData(IP_API_BASE_URL);
            JSONObject loc = new JSONObject(data);

            if (BlackBoard.city != loc.getString("city")) {
                //only send city update if it actually changed
                ListenerService.broadcastUpdate(BlackBoard.CITY_UPDATED);
            }
            BlackBoard.city = loc.getString("city");
            BlackBoard.countryCode = loc.getString("countryCode");
            BlackBoard.latitude = loc.getDouble("lat");
            BlackBoard.longitude = loc.getDouble("lon");
            BlackBoard.locationKnown = true;
        } catch (JSONException e) {
            BlackBoard.locationKnown = false;
        }
    }

    public static void getPressure () {
        JSONObject weather = null;
        try {
            String data = requestData(WEATHER_BASE_URL + BlackBoard.city + WEATHER_APIKEY_APPEND + BlackBoard.OPEN_WEATHER_MAPS_APIKEY);
            JSONObject all = new JSONObject(data);
            weather = all.getJSONObject("main");
        } catch (JSONException e) {}
        try {
            BlackBoard.weatherPressure = weather.getInt("sea_level");
        } catch (JSONException e) {
            try {BlackBoard.weatherPressure = weather.getInt("pressure");
            } catch (JSONException je) {
                BlackBoard.weatherPressure = -1;
            }
        }
    }

    public static String requestData (String url) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            //connect to the url
            connection = (HttpURLConnection) ( new URL(url)).openConnection();
            connection.connect();

            //get the response
            String lines = "";
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                lines += line;
                line = reader.readLine();
            }

            //exit and return
            reader.close();
            connection.disconnect();
            return lines;
        } catch(Exception e) {
        } finally {
            try {reader.close();} catch(Exception e) {}
            try {connection.disconnect();} catch(Exception e) {}
        }

        return null;
    }
}
