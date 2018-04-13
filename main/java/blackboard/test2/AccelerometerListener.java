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
        BlackBoard.accelerometer[0] = event.values[0];
        BlackBoard.accelerometer[1] = event.values[1];
        BlackBoard.accelerometer[2] = event.values[2];
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
    public void onReceive(Context context, Intent intent) {
        //ignored
    }
}