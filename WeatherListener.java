package com.example.daniel.pinme;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by James on 3/30/2018.
 */

public class WeatherListener extends KnowledgeSource {
    @Override
    public void registerListener() {
        //register for automatic updates
        //get updates for both city change and pressure change
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
        // (will likely later include to update if a certain amount of time has passed)
        if (intent.getAction() == BlackBoard.CITY_UPDATED) {
            onCityChange();
            onPressureChange();
            sendUpdates();
        }
        else {
            onPressureChange();
            sendUpdates();
        }
    }

    public void onCityChange() {

    }

    public void onPressureChange() {

    }

    @Override
    public void sendUpdates() {
        //include elevation update in broadcast
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BlackBoard.ELEVATION_UPDATED);
        broadcast(broadcastIntent);

        super.sendUpdates();
    }
}
