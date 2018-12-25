package com.nibrasco.freshksa.Fragments;


import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.location.*;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Utils.GPSTracker;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.Model.User;
import com.nibrasco.freshksa.R;

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
    Cart cart;
    public ShippingDetailsFragment() {
        // Required empty public constructor
        cart = Session.getInstance().Cart();
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
        final User user = Session.getInstance().User();
        txtName.setText(user.getName());
        txtPhone.setText(user.getPhone());
        InitGps();
        LinkListeners();
    }
    private void LinkListeners() {
        rdGrpTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(group.getCheckedRadioButtonId())
                {
                    case R.id.rdTimeNoon:
                        cart.setTimeOfDelivery(0);
                        break;
                    case R.id.rdTimeAfterNoon:
                        cart.setTimeOfDelivery(1);
                        break;
                    case R.id.rdTimeEvening:
                        cart.setTimeOfDelivery(2);
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
                cart.setAddress(edtAddress.getText().toString());
                Session.getInstance().Cart(cart);
                PaymentDetailsFragment fragment = new PaymentDetailsFragment();
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeContainer, fragment)
                        .commit();
            }
        });
    }

    private void InitGps(){
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 1037);
        Snackbar snackbar  = Snackbar.make(getView(), "Getting Location", Snackbar.LENGTH_INDEFINITE);
        ((TextView)(snackbar.getView()
                        .findViewById(android.support.design.R.id.snackbar_text)))
                        .setTextColor(Color.YELLOW);
        snackbar.show();

        GPSTracker gps = new GPSTracker(getActivity());
        GetLocation(gps);
        snackbar.dismiss();
    }
    void GetLocation(GPSTracker gps){
        double Latitude = gps.getLatitude();
        double Longtitude = gps.getLongitude();
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
        try {
            List<Address> addressList = geocoder.getFromLocation(Latitude, Longtitude, 1);
            if(addressList.size() > 0){
                String str = addressList.get(0).getAddressLine(0);
                cart.setAddress(str);
                edtAddress.setText(cart.getAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
