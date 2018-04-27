package blackboard.test2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.TimeZone;

public class DetailedViewActivity extends AppCompatActivity implements HasBlackBoardUI {
    public static final int UI_UPDATE_DELAY = 100;
    private UIUpdateThread uiThread;

    private TextView magnetText;
    private TextView accelerationText;
    private TextView magnetText1;
    private TextView accelerationText1;
    private TextView magnetText2;
    private TextView accelerationText2;
    private TextView magnetText3;
    private TextView accelerationText3;
    private TextView magnetText4;
    private TextView accelerationText4;

    private TextView barometerText;
    private TextView seaPressureText;
    private TextView elevationText;
    private TextView timeZoneText;
    private TextView cityText;
    private TextView countryCodeText;
    private TextView netStatusText;
    private TextView IPText;
    private TextView latitudeText;
    private TextView longitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        magnetText = findViewById(R.id.textView);
        accelerationText = findViewById(R.id.textView2);
        magnetText1 = findViewById(R.id.textView13);
        accelerationText1 = findViewById(R.id.textView14);
        magnetText2 = findViewById(R.id.textView15);
        accelerationText2 = findViewById(R.id.textView16);
        magnetText3 = findViewById(R.id.textView17);
        accelerationText3 = findViewById(R.id.textView18);
        magnetText4 = findViewById(R.id.textView19);
        accelerationText4 = findViewById(R.id.textView20);
        barometerText = findViewById(R.id.textView3);
        seaPressureText = findViewById(R.id.textView4);
        elevationText = findViewById(R.id.textView5);
        timeZoneText = findViewById(R.id.textView6);
        cityText = findViewById(R.id.textView7);
        countryCodeText = findViewById(R.id.textView8);
        netStatusText = findViewById(R.id.textView9);
        IPText = findViewById(R.id.textView10);
        latitudeText = findViewById(R.id.textView11);
        longitudeText = findViewById(R.id.textView12);

        Button backButton = findViewById(R.id.goBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        updateUI();
    }

    @Override
    protected void onResume () {
        super.onResume();

        //resume the UIUpdateThread (create a new one)
        uiThread = new UIUpdateThread(this, UI_UPDATE_DELAY);
        uiThread.start();
    }

    @Override
    protected void onPause () {
        super.onPause();

        //pause the UIUpdateThread
        uiThread.shouldBeRunning = false;
    }

    @Override
    public void updateUI() {
        //update the ui text
        String temp;

        temp = "MAG: " + Arrays.toString(BlackBoard.getCurrentMagnetometerRounded());
        magnetText.setText(temp);
        temp = "ACC: " + Arrays.toString(BlackBoard.getCurrentAccelerometerRounded());
        accelerationText.setText(temp);

        temp = Arrays.toString(BlackBoard.getMagnetometerRounded(-1));
        magnetText1.setText(temp);
        temp = Arrays.toString(BlackBoard.getMagnetometerRounded(-2));
        magnetText2.setText(temp);
        temp = Arrays.toString(BlackBoard.getMagnetometerRounded(-3));
        magnetText3.setText(temp);
        temp = Arrays.toString(BlackBoard.getMagnetometerRounded(-4));
        magnetText4.setText(temp);
        temp = Arrays.toString(BlackBoard.getAccelerometerRounded(-1));
        accelerationText1.setText(temp);
        temp = Arrays.toString(BlackBoard.getAccelerometerRounded(-2));
        accelerationText2.setText(temp);
        temp = Arrays.toString(BlackBoard.getAccelerometerRounded(-3));
        accelerationText3.setText(temp);
        temp = Arrays.toString(BlackBoard.getAccelerometerRounded(-4));
        accelerationText4.setText(temp);

        temp = "BAR: " + BlackBoard.getCurrentPressureRounded();
        barometerText.setText(temp);
        temp = "SEA: " + BlackBoard.getCurrentWeatherPressureRounded();
        seaPressureText.setText(temp);
        temp = "ELE: " + BlackBoard.getCurrentElevationRounded();
        elevationText.setText(temp);

        TimeZone t = BlackBoard.getCurrentTimeZone();
        temp = "TZ: ";
        if (t != null) {temp += t.getDisplayName();}
        timeZoneText.setText(temp);
        temp = "CITY: " + BlackBoard.getCurrentCity();
        cityText.setText(temp);
        temp = "CC: " + BlackBoard.getCurrentCountryCode();
        countryCodeText.setText(temp);

        temp = "NS: " + BlackBoard.getCurrentNetworkStatus();
        netStatusText.setText(temp);
        temp = "IP: " + BlackBoard.getCurrentIP();
        IPText.setText(temp);

        double[] ll = BlackBoard.getCurrentLocation();
        temp = "LAT: ";
        if (ll != null) {temp += ll[0];}
        latitudeText.setText(temp);
        temp = "LON: ";
        if (ll != null) {temp += ll[1];}
        longitudeText.setText(temp);
    }
}
