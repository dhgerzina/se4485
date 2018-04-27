package blackboard.test2;

import android.content.BroadcastReceiver;
import android.content.Intent;

public abstract class KnowledgeSource extends BroadcastReceiver {
    /**
     * Set up the listener
     * Currently: register to receive broadcasts, register for sensor updates,
     *      or create a thread to do so
     */
    public abstract void registerListener();

    /**
     * Take down listener
     * Undo whatever was done in registerListener()
     */
    public abstract void unregisterListener();

    /**
     * Not required, but many knowledge sources do so
     */
    public abstract void broadcastUpdates();
}
