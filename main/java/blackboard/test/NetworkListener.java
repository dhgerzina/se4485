package blackboard.test;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

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
        try {
            for (NetworkInterface nInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress address : Collections.list(nInterface.getInetAddresses())) {
                    if (!address.isLoopbackAddress()) {
                        BlackBoard.networkStatus = true;
                        BlackBoard.lastIP = address.getHostAddress();
                        break;
                    }

                    BlackBoard.networkStatus = false;
                    BlackBoard.lastIP = null;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        sendUpdates();
    }

    @Override
    public void sendUpdates() {
        //include network status update in broadcast
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BlackBoard.NETWORK_STATUS_UPDATED);
        broadcast(broadcastIntent);

        super.sendUpdates();
    }
}
