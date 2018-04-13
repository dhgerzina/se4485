package blackboard.test2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

public class DetailedViewActivity extends AppCompatActivity {
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private UIUpdateThread uiThread;
    private TextView magnetText;
    private TextView netStatusText;
    private TextView accelerationText;
    private TextView IPText;
    private TextView barometerText;
    private TextView elevationText;
    private TextView timeZoneText;
    private TextView cityText;
    private TextView latitudeText;
    private TextView longitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        magnetText = findViewById(R.id.textView);
        accelerationText = findViewById(R.id.textView2);
        netStatusText = findViewById(R.id.textView3);
        IPText = findViewById(R.id.textView4);
        barometerText = findViewById(R.id.textView5);
        elevationText = findViewById(R.id.textView6);
        cityText = findViewById(R.id.textView7);
        timeZoneText = findViewById(R.id.textView8);
        latitudeText = findViewById(R.id.textView9);
        longitudeText = findViewById(R.id.textView10);

        Button detailedButton = findViewById(R.id.goBack);
        detailedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        updateUI();
    }

    @Override
    protected void onResume () {
        super.onResume();

        uiThread = new UIUpdateThread(null, this);
        uiThread.start();
    }

    @Override
    protected void onPause () {
        super.onPause();

        uiThread.shouldBeRunning = false;
    }

    public void updateUI() {
        //update the ui
        String temp;

        temp = "MAG: " + decimalFormat.format(BlackBoard.magnetometer[0]) + ", " +
                decimalFormat.format(BlackBoard.magnetometer[1]) + ", " +
                decimalFormat.format(BlackBoard.magnetometer[2]);
        magnetText.setText(temp);
        temp = "ACC: " + decimalFormat.format(BlackBoard.accelerometer[0]) + ", "  +
                decimalFormat.format(BlackBoard.accelerometer[1]) + ", " +
                decimalFormat.format(BlackBoard.accelerometer[2]);
        accelerationText.setText(temp);
        temp = "BAR: " + BlackBoard.pressure;
        barometerText.setText(temp);
        temp = "ELE: " + BlackBoard.elevation;
        elevationText.setText(temp);

        temp = "NS: " + BlackBoard.networkStatus;
        netStatusText.setText(temp);
        temp = "IP: " + BlackBoard.lastIP;
        IPText.setText(temp);

        temp = "CITY: " + BlackBoard.city;
        cityText.setText(temp);
        temp = "TZ: " + BlackBoard.timeZone;
        timeZoneText.setText(temp);

        temp = "LAT: " + Calendar.getInstance().getTimeZone().getDisplayName();
        latitudeText.setText(temp);
        temp = "LON: ";
        longitudeText.setText(temp);
    }
}
