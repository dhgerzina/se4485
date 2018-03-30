package blackboard.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Alek on 3/24/2018.
 */

public class ListenerService extends Service {
    //static data needed for the knowledge sources
    public static SensorManager sensorManager;
    public static Context context;

    //list of knowledge sources
    public KnowledgeSource[] knowledgeSources;
    public static final int KNOWLEDGE_SOURCE_COUNT = 2;
    public static final int PRESSURE_LISTENER = 0;
    public static final int NETWORK_LISTENER = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //android stuff
        super.onCreate();

        //set static data
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        context = getApplicationContext();

        //create knowledge sources
        knowledgeSources = new KnowledgeSource[KNOWLEDGE_SOURCE_COUNT];
        knowledgeSources[PRESSURE_LISTENER] = new PressureListener();
        knowledgeSources[NETWORK_LISTENER] = new NetworkListener();

        //start the automatic updates for the knowledge sources
        for (KnowledgeSource ks : knowledgeSources) {ks.registerListener();}
    }

    @Override
    public void onDestroy() {
        //android stuff
        super.onDestroy();

        //stop the automatic updates for the knowledge sources
        for (KnowledgeSource ks : knowledgeSources) {ks.unregisterListener();}
    }
}
