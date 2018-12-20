package com.nibrasco.freshksa;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.Model.User;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingDetailsFragment extends Fragment {

    TextView txtName, txtPhone;
    TextInputEditText edtAddress;
    RadioGroup rdGrpTime;
    Button btnConfirm;
    LocationManager locationManager;

    public ShippingDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View v = getView();
        LoadContent(v);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shippingdetails, container, false);
    }


    private void LinkControls(View v)
    {
        txtName = (TextView) v.findViewById(R.id.txtDetailsUserName);
        txtPhone = (TextView) v.findViewById(R.id.txtDetailsPhone);
        edtAddress = (TextInputEditText) v.findViewById(R.id.edtOrderAddress);
        rdGrpTime = (RadioGroup)v.findViewById(R.id.rdGrpTime);
        btnConfirm = (Button)v.findViewById(R.id.btnConfirmShipping);
    }

    private void LoadContent(View v)
    {
        LinkControls(v);
        InitGps();
        final User user = Session.getInstance().User();
        txtName.setText(user.getName());
        txtPhone.setText(user.getPhone());
        LinkListeners();
    }
    private void LinkListeners() {
        rdGrpTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(group.getCheckedRadioButtonId())
                {
                    case R.id.rdTimeNoon:
                        Session.getInstance().Cart().setTimeOfDelivery(0);
                        break;
                    case R.id.rdTimeAfterNoon:
                        Session.getInstance().Cart().setTimeOfDelivery(1);
                        break;
                    case R.id.rdTimeEvening:
                        Session.getInstance().Cart().setTimeOfDelivery(2);
                        break;
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAddress.getText().toString().equals("")){
                    Snackbar.make(v, getResources().getString(R.string.msgSignInEmpty), Snackbar.LENGTH_LONG);
                    return;
                }
                Session.getInstance().Cart().setAddress(edtAddress.getText().toString());
                PaymentDetailsFragment fragment = new PaymentDetailsFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.homeContainer, fragment);
                ft.commit();
            }
        });
    }

    private void InitGps(){
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 1037);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double Latitude = location.getLatitude();
                    double Longtitude = location.getLongitude();
                    //LatLng LatLng = new LatLng(Latitude, Longtitude);
                    Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(Latitude, Longtitude, 1);
                        String str = addressList.get(0).getAddressLine(0) + ",";
                        str += addressList.get(0).getCountryName();

                        edtAddress.setText(str);
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
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double Latitude = location.getLatitude();
                    double Longtitude = location.getLongitude();
                    //LatLng LatLng = new LatLng(Latitude, Longtitude);
                    Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(Latitude, Longtitude, 1);
                        String str = addressList.get(0).getAddressLine(0) + ",";
                        str += addressList.get(0).getCountryName();

                        edtAddress.setText(str);


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
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }
}
