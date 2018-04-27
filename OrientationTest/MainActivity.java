package com.example.andy.testapp;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

//This program is based after the Android Developer Guide's example
//for using an android mobile device's Accelerometer and Magnetometer in Senor Fusion
//to find the device's Azimuth (angle from north), Pitch (angle from the ground), and Roll (angle from tilting sideways).
//(https://developer.android.com/guide/topics/sensors/sensors_position.html#sensors-pos-orient)

public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    private static final String TAG = "Testing";

    //Something to Display data
    TextView angle;

    //Something to constantly update the display
    Handler handler = new Handler();
    Runnable runnable;


    //Sensory components
    private SensorManager sm;
    private Sensor accelSensor;
    private Sensor magnetSensor;

    //Storage for Accelerometer and Magnetometer readings
    float[] mAccelerometer = new float[3];
    float[] mMagnetometer = new float[3];

    //Storage for Angle calculations
    float[] rotationMatrix = new float[9];
    float[] orientationAngles = new float[3];


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        angle = findViewById(R.id.angleText);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Created only to make some method calls more tidy
        accelSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Set update rates for both sensors. Delaying updates can make the program more efficient and reduces power consumption.
        sm.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        sm.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);

        //Set a thread to update the program
        handler.post(runnable = new Runnable() {
            @Override
            public void run() {
                updateOrientationAngles();
                handler.postDelayed(this,500);
            }
        });

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        //Stop taking updates from any registered sensors
        sm.unregisterListener(this);

        //Stop the thread
        handler.removeCallbacksAndMessages(runnable);
    }

   //Obtain readings from sensors and store them in arrays (as vectors).
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor == accelSensor)
        {
            System.arraycopy(event.values,0,mAccelerometer, 0, mAccelerometer.length);
        }
        else if(event.sensor == magnetSensor)
        {
            System.arraycopy(event.values,0,mMagnetometer, 0, mMagnetometer.length);
        }
    }

    //Compute orientation angles based on readings.
    public void updateOrientationAngles()
    {
        //Update the rotation matrix
        sm.getRotationMatrix(rotationMatrix, null, mAccelerometer, mMagnetometer);

        //Update orientation angles using the Rotation Matrix
        //Stored as (Azimuth, Pitch, Roll)
        //Expressed in Radians
        sm.getOrientation(rotationMatrix, orientationAngles);

        //Update the text display
        String angles = Arrays.toString(orientationAngles);
        angle.setText(angles);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        //Unused
    }


}
