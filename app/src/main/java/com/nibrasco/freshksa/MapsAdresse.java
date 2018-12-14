package com.nibrasco.freshksa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.LocationListener;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.nibrasco.freshksa.Model.Session;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MapsAdresse extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient providerClient;
    private LocationRequest request;
    private LocationCallback callback;
    private Location myLocation;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_adresse);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //assert mapFragment != null;
        //mapFragment.getMapAsync(this);
        MapView mapView = findViewById(R.id.map);
        mapView.getMapAsync(this);
        startLocationUpdates();
    }
    private void BuildCallback() {
        callback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Toast.makeText(MapsAdresse.this, "Callback", Toast.LENGTH_SHORT).show();
                myLocation = locationResult.getLastLocation();

                if(myLocation != null) {
                    LatLng loc = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                    Geocoder geoCoder = new Geocoder(MapsAdresse.this, Locale.getDefault());
                    Address address = null;
                    try {
                        address = (geoCoder.getFromLocation(loc.latitude, loc.longitude, 1)).get(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Session.getInstance().Cart().setAddress(address);
                }
            }
        };
    }
    private void BuildRequest() {
        request  = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(10f)
                .setInterval(5000)
                .setFastestInterval(3000)
                .setExpirationTime(TimeUnit.SECONDS.toMillis(100));
    }
    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1000);
    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        BuildRequest();

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(request);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        BuildCallback();
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(request, callback,
                Looper.myLooper());
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1000:
            {
                if(grantResults.length > 0){
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        BuildRequest();
                        BuildCallback();
                        providerClient = LocationServices.getFusedLocationProviderClient(this);

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                            return;
                        }
                        providerClient.requestLocationUpdates(request, callback, Looper.myLooper());
                    }
                }
            }
            break;
            default:
                break;
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = googleMap;

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                //mMap.setMyLocationEnabled(true);
                //mMap.setOnMyLocationChangeListener(this);
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                if (PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    try {


                    } catch (SecurityException e) {
                        Log.d("Err", e.getMessage());

                    }
                }
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
