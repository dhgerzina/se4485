package blackboard.test2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class PressureListener extends KnowledgeSource implements SensorEventListener {
    PressureUpdateThread updateThread;

    @Override
    public void registerListener() {
        //register for automatic updates
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
        //only active for a short period then turned off again
        //does not update the pressure while this is running so no worries about race conditions
        BlackBoard.pressureArray[BlackBoard.pressureCounter] = event.values[0];
        BlackBoard.pressureCounter++;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not important
    }

    @Override
    public void broadcastUpdates() {
        //broadcast pressure update
        ListenerService.broadcastUpdate(BlackBoard.PRESSURE_UPDATED);
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
                    //add listener
                    Sensor pressure = ListenerService.sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
                    ListenerService.sensorManager.registerListener(pressureListener, pressure, SensorManager.SENSOR_DELAY_NORMAL);
                    //get data for a bit
                    Thread.sleep(BlackBoard.PRESSURE_UPDATE_TIME);
                    //stop listening
                    ListenerService.sensorManager.unregisterListener(pressureListener);
                    //update pressure
                    updatePressure();
                    //wait for next cycle
                    Thread.sleep(BlackBoard.PRESSURE_UPDATE_WAIT);
                }
            } catch (InterruptedException e) {
                ListenerService.sensorManager.unregisterListener(pressureListener);
            }
        }

        private void updatePressure () {
            float sum = 0;
            for (int i = 0; i < BlackBoard.pressureCounter; i++) {
                sum += BlackBoard.pressureArray[i];
            }
            BlackBoard.updatePressure(sum/BlackBoard.pressureCounter);
            BlackBoard.pressureCounter = 0;

            //broadcast pressure update
            pressureListener.broadcastUpdates();
        }
    }
}
