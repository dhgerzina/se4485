package blackboard.test2;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

public class NetworkListener extends KnowledgeSource {
    @Override
    public void registerListener() {
        //register for automatic updates
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        ListenerService.context.registerReceiver(this, filter);
    }

    @Override
    public void unregisterListener() {
        //unregister for automatic updates
        ListenerService.context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //automatic updates
        boolean found = false;
        try {
            for (NetworkInterface nInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress address : Collections.list(nInterface.getInetAddresses())) {
                    if (!address.isLoopbackAddress()) {
                        BlackBoard.updateIP(address.getHostAddress());
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {BlackBoard.updateIP(null);}
        } catch (SocketException e) {
            e.printStackTrace();
        }

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {BlackBoard.updateNetworkStatus(BlackBoard.NETWORK_STATUS_NONE);}
        else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {BlackBoard.updateNetworkStatus(BlackBoard.NETWORK_STATUS_WIFI);}
        else {BlackBoard.updateNetworkStatus(BlackBoard.NETWORK_STATUS_CELLULAR);}

        broadcastUpdates();
    }

    @Override
    public void broadcastUpdates() {
        //broadcast network status update
        ListenerService.broadcastUpdate(BlackBoard.NETWORK_STATUS_UPDATED);
    }
}
