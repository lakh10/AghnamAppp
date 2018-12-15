package com.nibrasco.freshksa;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.Model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingDetailsFragment extends Fragment {

    TextView txtName, txtPhone, txtAddress;
    RadioGroup rdGrpTime;
    Button btnConfirm;

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
        txtName = (TextView) v.findViewById(R.id.txtUserName);
        txtPhone = (TextView) v.findViewById(R.id.txtDeliveryCost);
        txtAddress = (TextView) v.findViewById(R.id.txtOrderTotal);
        rdGrpTime = (RadioGroup)v.findViewById(R.id.rdGrpTime);
        btnConfirm = (Button)v.findViewById(R.id.btnConfirmShipping);
    }

    private void LoadContent(View v)
    {
        LinkControls(v);
        final User user = Session.getInstance().User();
        txtName.setText(user.getName());
        txtAddress.setText(Session.getInstance().Cart().getAddress());
        txtPhone.setText(user.getPhone());
        LinkListeners();
    }
    private void LinkListeners()
    {
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
                PaymentDetailsFragment fragment = new PaymentDetailsFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.orderItemContentFrame, fragment);
                ft.commit();
            }
        });
    }
}
