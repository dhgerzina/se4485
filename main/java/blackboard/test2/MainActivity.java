package blackboard.test2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.text.DecimalFormat;
import java.util.Calendar;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Intent intent;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private UIUpdateThread uiThread;
    private TextView netStatusText;
    private TextView IPText;
    private TextView elevationText;
    private TextView timeZoneText;
    private TextView latitudeText;
    private TextView longitudeText;
    ToggleButton toggleService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent = new Intent(this, ListenerService.class);

        netStatusText = findViewById(R.id.textView);
        IPText = findViewById(R.id.textView2);
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

        Button detailedButton = findViewById(R.id.goToDetailed);
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

        if(toggleService.isActivated()) {
            uiThread = new UIUpdateThread(this, null);
            uiThread.start();
        }
    }

    @Override
    protected void onPause () {
        super.onPause();

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

        //TODO: add pins based on known data
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void whenChecked(){
        System.out.println("Button toggled ON");
        startService(intent);

        uiThread = new UIUpdateThread(this, null);
        uiThread.start();
    }

    public void whenUnChecked(){
        System.out.println("Button toggled OFF");
        stopService(intent);
        uiThread.shouldBeRunning = false;
    }

    public void goToDetailed () {
        Intent detailedViewIntent = new Intent(this, DetailedViewActivity.class);
        startActivity(detailedViewIntent);
    }

    public void updateUI() {
        //update the ui
        String temp;
        temp = "NS: " + BlackBoard.networkStatus;
        netStatusText.setText(temp);
        temp = "IP: " + BlackBoard.lastIP;
        IPText.setText(temp);
        temp = "ELE: " + BlackBoard.elevation;
        elevationText.setText(temp);
        temp = "TZ: " + BlackBoard.timeZone;
        timeZoneText.setText(temp);
        temp = "LAT: " + Calendar.getInstance().getTimeZone().getDisplayName();
        latitudeText.setText(temp);
        temp = "LON: ";
        longitudeText.setText(temp);
    }
}