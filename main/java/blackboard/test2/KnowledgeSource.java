package blackboard.test2;

import android.content.BroadcastReceiver;
import android.content.Intent;

public abstract class KnowledgeSource extends BroadcastReceiver {
    //override these
    public abstract void registerListener();
    public abstract void unregisterListener();
    public abstract void broadcastUpdates();
}
