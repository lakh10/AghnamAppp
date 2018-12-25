package com.nibrasco.freshksa.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nibrasco.freshksa.R;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    FloatingActionButton fabMyLocation;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fabMyLocation = (FloatingActionButton) findViewById(R.id.fabMyLocation);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                double Latitude = location.getLatitude();
                double Longtitude = location.getLongitude();
                LatLng LatLng = new LatLng(Latitude, Longtitude);
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList = geocoder.getFromLocation(Latitude, Longtitude, 1);
                    String str = addressList.get(0).getAddressLine(0);
                    mMap.addMarker(new MarkerOptions().position(LatLng).title(str));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 11f));


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double Latitude = location.getLatitude();
                    double Longtitude = location.getLongitude();
                    LatLng LatLng = new LatLng(Latitude, Longtitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(Latitude, Longtitude, 1);
                        String str = addressList.get(0).getAddressLine(0) + ",";
                        str += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(LatLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 11f));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            });
        }
        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double Latitude = location.getLatitude();
                    double Longtitude = location.getLongitude();
                    LatLng LatLng = new LatLng(Latitude, Longtitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(Latitude, Longtitude, 1);
                        String str = addressList.get(0).getAddressLine(0) + ",";
                        str += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(LatLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 11f));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            });
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
        mMap = googleMap;
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 1037);
    }
}