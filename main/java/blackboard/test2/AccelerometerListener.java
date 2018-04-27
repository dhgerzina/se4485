package blackboard.test2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerListener extends KnowledgeSource implements SensorEventListener {
    @Override
    public void onSensorChanged(SensorEvent event) {
        double[] values = new double[3];
        for (int i = 0; i < values.length; i++) {values[i] = event.values[i];}
        BlackBoard.updateAccelerometer(values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //ignored
    }

    @Override
    public void registerListener() {
        //register for automatic updates
        Sensor accelerometer = ListenerService.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        ListenerService.sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void unregisterListener() {
        //unregister for automatic updates
        ListenerService.sensorManager.unregisterListener(this);
    }

    @Override
    public void broadcastUpdates() {
        // do nothing
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //ignored
    }
}