package blackboard.test2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.TimeZone;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, HasBlackBoardUI {
    public static final int UI_UPDATE_DELAY = 1000;

    private GoogleMap mMap;
    private Intent listenerServiceIntent;
    private UIUpdateThread uiThread;

    private TextView netStatusText;
    private TextView cityText;
    private TextView elevationText;
    private TextView timeZoneText;
    private TextView latitudeText;
    private TextView longitudeText;
    ToggleButton toggleService;
    Button detailedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listenerServiceIntent = new Intent(this, ListenerService.class);

        netStatusText = findViewById(R.id.textView);
        cityText = findViewById(R.id.textView2);
        elevationText = findViewById(R.id.textView3);
        timeZoneText = findViewById(R.id.textView4);
        latitudeText = findViewById(R.id.textView5);
        longitudeText = findViewById(R.id.textView6);

        toggleService = findViewById(R.id.toggleService);
        // toggle.setOnClickListener(mButtonStartListener);
        toggleService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    whenChecked();
                } else {
                    whenUnChecked();
                }
            }
        });

        detailedButton = findViewById(R.id.goToDetailed);
        detailedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToDetailed();
            }
        });

        updateUI();
    }

    @Override
    protected void onResume () {
        super.onResume();

        //resume the UIUpdateThread if needed (create a new one)
        if(toggleService.isActivated()) {
            uiThread = new UIUpdateThread(this, UI_UPDATE_DELAY);
            uiThread.start();
        }
    }

    @Override
    protected void onPause () {
        super.onPause();

        //pause the UIUpdateThread if needed
        if (uiThread != null) {
            uiThread.shouldBeRunning = false;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    /**
     * Do this when the Find Location button is checked
     */
    public void whenChecked(){
        System.out.println("Button toggled ON");
        startService(listenerServiceIntent);

        uiThread = new UIUpdateThread(this, UI_UPDATE_DELAY);
        uiThread.start();
    }

    /**
     * Do this when the Find Location button is unchecked
     */
    public void whenUnChecked(){
        System.out.println("Button toggled OFF");
        stopService(listenerServiceIntent);
        uiThread.shouldBeRunning = false;
    }

    /**
     * Do this when the Go To Detailed View button is pressed
     */
    public void goToDetailed () {
        Intent detailedViewIntent = new Intent(this, DetailedViewActivity.class);
        startActivity(detailedViewIntent);
    }

    /**
     * Update the UI
     */
    @Override
    public void updateUI () {
        //update the ui text
        String temp;
        temp = "NS: " + BlackBoard.getCurrentNetworkStatus();
        netStatusText.setText(temp);
        temp = "CITY: " + BlackBoard.getCurrentCity();
        cityText.setText(temp);
        temp = "ELE: " + BlackBoard.getCurrentElevationRounded();
        elevationText.setText(temp);
        TimeZone t = BlackBoard.getCurrentTimeZone();
        temp = "TZ: ";
        if (t != null) {temp += t.getDisplayName();}
        timeZoneText.setText(temp);
        double[] ll = BlackBoard.getCurrentLocation();
        temp = "LAT: ";
        if (ll != null) {temp += ll[0];}
        latitudeText.setText(temp);
        temp = "LON: ";
        if (ll != null) {temp += ll[1];}
        longitudeText.setText(temp);

        updateMap(ll);
    }

    /**
     * Update the map
     * Kept separate from updating the UI for readability
     */
    public void updateMap (double[] ll) {
        // Add a marker at current location and move the camera
        if (mMap != null) {
            mMap.clear();
            if (ll != null) {
                LatLng location = new LatLng(ll[0], ll[1]);
                mMap.addMarker(new MarkerOptions().position(location).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            }
        }
    }
}