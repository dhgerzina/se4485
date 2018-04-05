package com.example.daniel.pinme;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Intent intent;
    MyThread t;
    static TextView t1;
    static TextView t2;
    static TextView t3;
    static TextView t4;
    static TextView t5;
    static TextView t6;
    static TextView t7;
    static TextView t8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        t1 = findViewById(R.id.textView);
        t2 = findViewById(R.id.textView2);
        t3 = findViewById(R.id.textView3);
        t4 = findViewById(R.id.textView4);
        t5 = findViewById(R.id.textView5);
        t6 = findViewById(R.id.textView6);
        t7 = findViewById(R.id.textView7);
        t8 = findViewById(R.id.textView8);

        t1.setText("MAG: ");
        t3.setText("ACC: ");
        t5.setText("BAR: ");
        t2.setText("NS: ");
        t4.setText("IP: ");
        t6.setText("TZ: ");
        t7.setText("LAT: ");
        t8.setText("LON: ");

        ToggleButton toggle = findViewById(R.id.toggleButton);
       // toggle.setOnClickListener(mButtonStartListener);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                   whenChecked();
                } else {
                   whenUnChecked();
                }
            }
        });

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
        intent = new Intent(this, ListenerService.class);
        System.out.println("Button toggled ON");
        startService(intent);

        t = new MyThread(){
            @Override
            public void run() {
                try {
                    while (!isInterrupted() && shouldBeRunning) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

    }

    public void whenUnChecked(){
        System.out.println("Button toggled OFF");
        stopService(intent);
        t.shouldBeRunning = false;
    }

    public static void updateTextView() {
        t1.setText("MAG: " + BlackBoard.magnet);
        t3.setText("ACC: " + BlackBoard.motion);
        t5.setText("BAR: " + BlackBoard.pressure[BlackBoard.pressureCounter]);
        t2.setText("NS: " + BlackBoard.networkStatus);
        t4.setText("IP: " + BlackBoard.lastIP);
        t6.setText("TZ: " + BlackBoard.timeZone);
        t7.setText("LAT: " + Calendar.getInstance().getTimeInMillis());
        t8.setText("LON: ");
    }

}
