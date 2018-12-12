package com.nibrasco.freshksa;

import android.Manifest;
import android.annotation.TargetApi;
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

public class MapsAdresse extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient providerClient;
    LocationRequest request;
    LocationCallback callback;
    Location myLocation;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_adresse);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BuildRequest();
        BuildCallback();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1337);
            return;
        }

        providerClient = LocationServices.getFusedLocationProviderClient(this);
        providerClient.requestLocationUpdates(request, callback, Looper.myLooper());
    }
    void BuildCallback() {
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
    void BuildRequest() {
        request  = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setSmallestDisplacement(10f);
        request.setInterval(5000);
        request.setFastestInterval(3000);
        request.setExpirationTime(TimeUnit.SECONDS.toMillis(100));
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
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1337);
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
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1337);
                if (PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    try {
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    } catch (SecurityException e) {
                        Log.d("Err", e.getMessage());

                    }
                }
            }

        }
    }
}
