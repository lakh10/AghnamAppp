package com.nibrasco.aghnam.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nibrasco.aghnam.Model.Session;
import com.nibrasco.aghnam.R;
import com.nibrasco.aghnam.Utils.GPSTracker;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap mMap;
    FloatingActionButton fabMyLocation;
    Button btnConfirm;
    GPSTracker gps;
    String address;

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
                PackageManager.PERMISSION_GRANTED) {
            return;
        }

        double Latitude = gps.getLatitude();
        double Longitude = gps.getLongitude();
        GetLocation(new LatLng(Latitude, Longitude));

    }
    /*
    private List<Address> getManualLocation(double lat, double lng) {
        //SharedPreferences settings;
        //settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        //settings.edit().putBoolean("SecondaryGeoCode", true).apply();
        ApplicationInfo app = null;
        try {
            app = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bundle bundle = app.metaData;

        String address = String.format(Locale.ENGLISH,
                "https://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language=" + Locale.getDefault().getCountry()
                + "&key=" + bundle.getString("com.google.android.geo.API_KEY"),
                lat, lng);

        HttpGet httpGet = new HttpGet(address);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;

        StringBuilder stringBuilder = new StringBuilder();
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            List<Address> retList = new ArrayList<>();

            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                JSONArray results = jsonObject.getJSONArray("results");
                boolean hasGoodPartTwo = false;

                for (int i = 0; i < results.length(); i++) {
                    Address addr = new Address(Locale.getDefault());
                        String neighborhood = ((JSONArray)((JSONObject)results.get(i)).get("types")).getString(0);
                        if (neighborhood.compareTo("neighborhood") == 0) {
                            String neighborhood1 = ((JSONObject)results.get(i)).getString("long_name");
                            addr.setSubThoroughfare(neighborhood1);
                        }
                        String locality = ((JSONArray)((JSONObject)results.get(i)).get("types")).getString(0);
                        if (locality.compareTo("locality") == 0) {
                            String locality1 = ((JSONObject)results.get(0)).getString("long_name");
                            addr.setLocality(locality1);
                        }

                        String subadminArea = ((JSONArray)((JSONObject)results.get(i)).get("types")).getString(0);
                        if (locality.compareTo("administrative_area_level_2") == 0) {
                            String subadminArea1 = ((JSONObject)results.get(i)).getString("long_name");
                            addr.setSubAdminArea(subadminArea1);
                        }
                        String adminArea = ((JSONArray)((JSONObject)results.get(i)).get("types")).getString(0);
                        if (adminArea.compareTo("administrative_area_level_1") == 0) {
                            String adminArea1 = ((JSONObject)results.get(i)).getString("long_name");
                            addr.setAdminArea(adminArea1);
                        }

                        String postalcode = ((JSONArray)((JSONObject)results.get(i)).get("types")).getString(0);
                        if (postalcode.compareTo("postal_code") == 0) {
                            String postalcode1 = ((JSONObject)results.get(i)).getString("long_name");
                            addr.setPostalCode(postalcode1);
                        }
                        String sublocality = ((JSONArray)((JSONObject)results.get(i)).get("types")).getString(0);
                        if (sublocality.compareTo("sublocality") == 0) {
                            String sublocality1 = ((JSONObject)results.get(i)).getString("long_name");
                            addr.setSubLocality(sublocality1);
                        }
                        String countr = ((JSONArray)((JSONObject)results.get(i)).get("types")).getString(0);
                        if (countr.compareTo("country") == 0) {
                            String countr1 = ((JSONObject)results.get(i)).getString("long_name");

                            addr.setCountryName(countr1);
                        }
                        retList.add(addr);
                    }

            }
            return retList;
        }catch (Exception e){
            Log.e(MapsFragment.class.getName(), e.getMessage());
            return new ArrayList<Address>();
        }
    }
    */
    private void GetLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
        try {

            List<Address> addressList = (List<Address>)new LocationTask(latLng.latitude, latLng.longitude).execute().get();
            if (!addressList.isEmpty()) {
                Address adr = addressList.get(0);
                address = adr.getAddressLine(0);
                //addr_label.setText("Address:"+addre/*+","+addr1.getSubLocality()+","+addr1.getSubThoroughfare()+","+addr1.getLocality()*/);
                //city.setText("City:"+addr1.getSubAdminArea());
                //state.setText("State:"+addr1.getAdminArea());
                //country.setText("Country:"+addr1.getCountryName());
                //pin.setText("Pin:"+addr1.getPostalCode());
                Session.getInstance().Cart().setAddress(address);
                mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title(address)
                                .snippet(adr.getCountryName())
                );
                mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                                new CameraPosition
                                        .Builder()
                                        .target(latLng)
                                        .zoom(15)
                                        .build()
                        ));
            }else {
                //TODO: msg => invalid location
                Toast.makeText(getContext(), "msg", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        InitGps();
        InitControls();
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                GetLocation(gps);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address != null && !address.equals("")) {
                    ShippingDetailsFragment shippingDetailsFragment = new ShippingDetailsFragment();
                    getActivity().
                            getSupportFragmentManager().beginTransaction()
                            .replace(R.id.homeContainer, shippingDetailsFragment)
                            .commit();
                }else {
                    //TODO: english to arabic string
                    String msg = "";
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });
        mMap.clear();
        GetLocation(gps);
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
    private class LocationTask extends AsyncTask<Double, Integer, List<Address>> {

        double latitude, longitude;
        LocationTask(double latitude,double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
        @Override
        protected List<Address> doInBackground(Double... coordinates) {
            List<Address> addresses = null;
            try {
                Geocoder geocoder = new Geocoder(getContext());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
    }
}
