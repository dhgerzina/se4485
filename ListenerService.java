package com.example.daniel.pinme;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Daniel on 3/16/2018.
 */

public class ListenerService extends IntentService {
    private NotificationManager mNM;
    private int NOTIFICATION = R.string.local_service_started;

    public ListenerService(){
        super("ListenerService");
    }

    //static data needed for the knowledge sources
    public static SensorManager sensorManager;
    public static Context context;

    //list of knowledge sources
    public KnowledgeSource[] knowledgeSources;
    public static final int KNOWLEDGE_SOURCE_COUNT = 2;
    public static final int PRESSURE_LISTENER = 0;
    public static final int NETWORK_LISTENER = 1;


    @Override
    public void onCreate(){
        super.onCreate();

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        //set static data
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        context = getApplicationContext();

        //create knowledge sources
        knowledgeSources = new KnowledgeSource[KNOWLEDGE_SOURCE_COUNT];
        knowledgeSources[PRESSURE_LISTENER] = new PressureListener();
        knowledgeSources[NETWORK_LISTENER] = new NetworkListener();

        //start the automatic updates for the knowledge sources
        for (KnowledgeSource ks : knowledgeSources) {ks.registerListener();}

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();

        Toast.makeText(this, R.string.local_service_started, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        //stop the automatic updates for the knowledge sources
        for (KnowledgeSource ks : knowledgeSources) {ks.unregisterListener();}

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        //TODO: may need to change MainActivity.class
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                //TODO: change icon
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.local_service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

}
