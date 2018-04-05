package com.example.daniel.pinme;

/**
 * Created by Daniel on 3/30/2018.
 */

    import android.content.BroadcastReceiver;
    import android.content.Intent;

public abstract class KnowledgeSource extends BroadcastReceiver {
    //override these
    public abstract void registerListener() ;
    public abstract void unregisterListener();

    //support function so that stuff will make more sense
    public void broadcast (Intent intent) {
        ListenerService.context.sendBroadcast(intent);
    }

    //overridden and called by knowledge sources when changes have been made to the black board
    //broadcast specific updates in child classes
    public void sendUpdates () {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BlackBoard.BLACKBOARD_UPDATED);
        broadcast(broadcastIntent);
    }
}
