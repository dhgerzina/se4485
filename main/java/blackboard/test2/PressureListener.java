package blackboard.test2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Alek on 3/24/2018.
 */

public class PressureListener extends KnowledgeSource implements SensorEventListener {
    PressureUpdateThread updateThread;

    @Override
    public void registerListener() {
        //register for automatic updates
        Sensor pressure = ListenerService.sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        ListenerService.sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        updateThread = new PressureUpdateThread(this);
        updateThread.start();
    }

    @Override
    public void unregisterListener() {
        //unregister for automatic updates
        ListenerService.sensorManager.unregisterListener(this);
        updateThread.shouldBeRunning = false;
        updateThread.interrupt();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //ignored because sensors use onSensorChanged instead
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //automatic updates
        try {
            BlackBoard.pressureSemaphore.acquire();
            BlackBoard.pressureArray[BlackBoard.pressureCounter] = event.values[0];
            BlackBoard.pressureCounter++;
            BlackBoard.pressureSemaphore.release();
        } catch (InterruptedException e) {}
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not important
    }

    @Override
    public void sendUpdates() {
        //include pressureArray update in broadcast
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BlackBoard.PRESSURE_UPDATED);
        broadcast(broadcastIntent);

        super.sendUpdates();
    }

    private class PressureUpdateThread extends Thread {
        private boolean shouldBeRunning = true;
        private PressureListener pressureListener;

        private PressureUpdateThread (PressureListener listener) {
            pressureListener = listener;
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted() && shouldBeRunning) {
                    Thread.sleep(10000);

                    BlackBoard.pressureSemaphore.acquire();
                    updatePressure();
                    BlackBoard.pressureSemaphore.release();
                }
            } catch (InterruptedException e) {}
        }

        private void updatePressure () {
            float sum = 0;
            for (int i = 0; i < BlackBoard.pressureCounter; i++) {
                sum += BlackBoard.pressureArray[i];
            }
            BlackBoard.pressure = sum/BlackBoard.pressureCounter;
            BlackBoard.pressureCounter = 0;

            pressureListener.sendUpdates();
        }
    }
}
