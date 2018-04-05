package com.example.daniel.pinme;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by James on 3/24/2018.
 */

public class PressureListener extends KnowledgeSource implements SensorEventListener {
    @Override
    public void registerListener() {
        //register for automatic updates
        Sensor pressure = ListenerService.sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        ListenerService.sensorManager.registerListener(this, pressure, 1000);
    }

    @Override
    public void unregisterListener() {
        //unregister for automatic updates
        ListenerService.sensorManager.unregisterListener(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //ignored because sensors use onSensorChanged instead
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //automatic updates
        BlackBoard.pressure[BlackBoard.pressureCounter] = event.values[0];
        BlackBoard.pressureCounter++;
        if (BlackBoard.pressureCounter == BlackBoard.pressure.length) {
            BlackBoard.pressureCounter = 0;
            float sum = 0;
            for (float i : BlackBoard.pressure) {sum += i;}
            BlackBoard.normalPressure = sum/BlackBoard.pressure.length;
        }
        sendUpdates();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not important
    }

    @Override
    public void sendUpdates() {
        //include pressure update in broadcast
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BlackBoard.PRESSURE_UPDATED);
        broadcast(broadcastIntent);

        super.sendUpdates();
    }
}
