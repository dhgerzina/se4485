package blackboard.test2;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Calendar;

public class LocationEstimator extends KnowledgeSource {
    @Override
    public void registerListener() {
        //register for automatic updates
        IntentFilter filter = new IntentFilter();
        filter.addAction(BlackBoard.NETWORK_STATUS_UPDATED);
        ListenerService.context.registerReceiver(this, filter);
    }

    @Override
    public void unregisterListener() {
        //unregister for automatic updates
        ListenerService.context.unregisterReceiver(this);
    }

    @Override
    public void broadcastUpdates() {
        //not used currently
        ListenerService.broadcastUpdate(BlackBoard.CITY_UPDATED);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //estimate country, city, and lat/lon from ip address
        NetworkGetRequestTask locatorTask = new NetworkGetRequestTask();
        locatorTask.execute(0);
        //TODO: find a better place to do this
        BlackBoard.timeZone = Calendar.getInstance().getTimeZone();
        //TODO: nothing currently uses this
        ListenerService.broadcastUpdate(BlackBoard.TIMEZONE_UPDATED);
    }
}
