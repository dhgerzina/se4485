package blackboard.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private BlackBoardUpdateReceiver receiver;

    static boolean listenerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //android stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent for the listener service
        final Intent listenerServiceIntent = new Intent(getApplicationContext(), ListenerService.class);

        //set up the automatic updates for the ui
        IntentFilter filter = new IntentFilter(BlackBoard.BLACKBOARD_UPDATED);
        receiver = new BlackBoardUpdateReceiver();
        registerReceiver(receiver, filter);

        //set the functions for the button
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if(!listenerRunning) {
                    listenerRunning = true;
                    ((Button) findViewById(R.id.button)).setText("on");
                    startService(listenerServiceIntent);
                }
                else {
                    stopService(listenerServiceIntent);
                    ((Button) findViewById(R.id.button)).setText("off");
                    listenerRunning = false;
                }
            }
        });

        updateText();
    }

    public void updateText() {
        //update the ui
        TextView view1 = findViewById(R.id.textView);
        TextView view2 = findViewById(R.id.textView2);
        TextView view3 = findViewById(R.id.textView3);
        view1.setText(String.valueOf(BlackBoard.pressure[BlackBoard.pressureCounter]));
        view2.setText(String.valueOf(BlackBoard.normalPressure));
        view3.setText(BlackBoard.lastIP);
    }

    public class BlackBoardUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateText();
        }
    }
}

