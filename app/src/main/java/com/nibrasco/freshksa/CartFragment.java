package com.nibrasco.freshksa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.CartItemCategory;
import com.nibrasco.freshksa.Model.RecyclerCartItemAdapter;
import com.nibrasco.freshksa.Model.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {


    private RecyclerView itemsView;
    private TextView txtCartTotal, btnOrder;
    private Button btnConfirmCart;
    private Float Total = 0.0f;
    public CartFragment() {
        // Required empty public constructor
        Total = Session.getInstance().Cart().GetTotal();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        LinkControls(v);
        DisplayValues();
        if(Session.getInstance().Cart() != null && Session.getInstance().Cart().Items().size() > 0) {
            FillRecyclerView(v);
        }
        AssignListeners();
        //else  {
        //    OrderItemFragment f = new OrderItemFragment();
        //    assert getFragmentManager() != null;
        //    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        //    fragmentTransaction.replace(R.id.homeContainer, f);
        //    fragmentTransaction.addToBackStack(null);
        //    fragmentTransaction.commit();
        //}
    }

    private void DisplayValues() {
        txtCartTotal.setText(Float.toString(Total));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
    private void LinkControls(View v)
    {
        itemsView = (RecyclerView)v.findViewById(R.id.recyclerCartItems);
        txtCartTotal = (TextView)v.findViewById(R.id.txtCartTotal);
        btnOrder = (TextView) v.findViewById(R.id.btnItemOrder);
        btnConfirmCart = (Button)v.findViewById(R.id.btnConfirmCart);
    }
    private void FillRecyclerView(View v)
    {
        if(Session.getInstance().Cart() != null && Session.getInstance().Cart().Items().size() > 0) {
            ArrayList<CartItemCategory> items = new ArrayList<>();
            List<Cart.Item> cartItems = Session.getInstance().Cart().Items();
            for (Cart.Item item :
                    cartItems) {
                items.add(new CartItemCategory(item));
            }
            RecyclerCartItemAdapter adapter = new RecyclerCartItemAdapter(v.getContext(), items);
            itemsView.setAdapter(adapter);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            itemsView.setLayoutManager(layoutManager);
        }
    }
    private void AssignListeners()
    {
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               OrderItemFragment f = new OrderItemFragment();
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.homeContainer, f);
               fragmentTransaction.addToBackStack(null);
               fragmentTransaction.commit();
            }
        });
        btnConfirmCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShippingDetailsFragment f = new ShippingDetailsFragment();
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.homeContainer, f);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
