package com.ibm.mce.samples.gcm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ibm.mce.sdk.location.LocationManager;
import com.ibm.mce.sdk.location.LocationPreferences;
import com.ibm.mce.sdk.location.MceGeofence;
import com.ibm.mce.sdk.location.MceLocation;

import java.util.List;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    private static final int REQUEST_LOCATION = 0;

    private GoogleMap mMap;
    private Button enableLocations;
    private Button showGeofences;
    private boolean mapEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        enableLocations = (Button)findViewById(R.id.enableLocations);
        showGeofences = (Button)findViewById(R.id.showGeofences);
        boolean locationsEnabled = LocationPreferences.isEnableLocations(getApplicationContext());
        enableLocations.setText(locationsEnabled ? getResources().getString(R.string.disable_locations_text) : getResources().getString(R.string.enable_locations_text));
        showGeofences.setEnabled(false);

        enableLocations.setOnClickListener(this);
        showGeofences.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Log.e("Location", "Failed to start map", e);
        }

    }

    @Override
    public void onClick(View view) {
        if(enableLocations == view) {
            boolean locationsEnabled = LocationPreferences.isEnableLocations(getApplicationContext());
            if(locationsEnabled) {
                LocationManager.disableLocationSupport(getApplicationContext());
                enableLocations.setText(getResources().getString(R.string.enable_locations_text));
                showGeofences.setEnabled(false);
            } else {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_LOCATION);

                } else {
                    LocationManager.enableLocationSupport(getApplicationContext());
                    enableLocations.setText(getResources().getString(R.string.disable_locations_text));
                    showGeofences.setEnabled(mapEnabled);
                }

            }
        } else if(showGeofences == view) {
            mMap.clear();
            List<MceLocation> locations = LocationManager.getAllLocations(getApplicationContext());

            for(MceLocation location : locations) {
                LatLng center = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addCircle(new CircleOptions()
                        .center(center)
                        .radius(location.getRadius())
                        .strokeColor(Color.RED)
                        .strokeWidth(1)
                        .fillColor(0x5500ff00));
                mMap.addMarker(new MarkerOptions().position(center).title(location.getId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mapEnabled = true;
        boolean locationsEnabled = LocationPreferences.isEnableLocations(getApplicationContext());
        showGeofences.setEnabled(locationsEnabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_LOCATION) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationManager.enableLocationSupport(getApplicationContext());
                showGeofences.setEnabled(true);
            }
        }
    }


}
