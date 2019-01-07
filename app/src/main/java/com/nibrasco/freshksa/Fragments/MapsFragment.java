package com.nibrasco.freshksa.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.R;
import com.nibrasco.freshksa.Utils.GPSTracker;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap mMap;
    FloatingActionButton fabMyLocation;
    Button btnConfirm;
    GPSTracker gps;
    String address;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());

        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        return rootView;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void InitControls(){
        fabMyLocation = (FloatingActionButton) getView().findViewById(R.id.fabMyLocation);
        btnConfirm = (Button) getView().findViewById(R.id.btnConfirmAddress);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                GetLocation(latLng);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void InitGps(){
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps = new GPSTracker(getContext());
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 1037);
        GetLocation(gps);
    }

    private void GetLocation(GPSTracker gps){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        double Latitude = gps.getLatitude();
        double Longtitude = gps.getLongitude();
        GetLocation(new LatLng(Latitude, Longtitude));

    }
    private void GetLocation(LatLng LatLng)
    {
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
        try {
            List<Address> addressList = geocoder.getFromLocation(LatLng.latitude, LatLng.longitude, 1);
            if (!addressList.isEmpty()) {
                Address adr = addressList.get(0);
                address = adr.getAddressLine(0);
                Session.getInstance().Cart().setAddress(address);
                mMap.addMarker(
                        new MarkerOptions()
                                .position(LatLng)
                                .title(address)
                                .snippet(adr.getCountryName())
                );
                mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                                new CameraPosition
                                        .Builder()
                                        .target(LatLng)
                                        .zoom(15)
                                        .build()
                        ));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        InitGps();
        InitControls();
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLocation(gps);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Session.getInstance().Cart().setAddress(address);
                ShippingDetailsFragment shippingDetailsFragment = new ShippingDetailsFragment();
                getActivity().
                        getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homeContainer, shippingDetailsFragment)
                        .commit();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
