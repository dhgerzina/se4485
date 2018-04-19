package blackboard.test2;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class WeatherListener extends KnowledgeSource {
    @Override
    public void registerListener() {
        //register for automatic updates
        //get updates for both city change and pressure change
        //TODO: get local air pressure again if a certain amount of time has passed in the same city
        IntentFilter filter = new IntentFilter();
        filter.addAction(BlackBoard.CITY_UPDATED);
        filter.addAction(BlackBoard.PRESSURE_UPDATED);
        ListenerService.context.registerReceiver(this, filter);
    }

    @Override
    public void unregisterListener() {
        //unregister for automatic updates
        ListenerService.context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //automatic updates
        //only get new data from source if the city has changed
        if (intent.getAction().equals(BlackBoard.CITY_UPDATED)) {
            onCityChange();
        }
        else {
            onPressureChange();
            broadcastUpdates();
        }
    }

    public void onCityChange() {
        NetworkGetRequestTask weatherTask = new NetworkGetRequestTask();
        weatherTask.execute(1);
    }

    public void onPressureChange() {
        if (BlackBoard.weatherPressure == -1 || BlackBoard.pressure == -1) {
            BlackBoard.elevation = -1;
        }
        else {
            //TODO: this is not even close to accurate for some reason, easily 40 meters off at times
            BlackBoard.elevation = SensorManager.getAltitude(BlackBoard.weatherPressure, BlackBoard.pressure);
        }
    }

    @Override
    public void broadcastUpdates() {
        //broadcast elevation update
        //TODO: nothing currently uses this
        ListenerService.broadcastUpdate(BlackBoard.ELEVATION_UPDATED);
    }
}
