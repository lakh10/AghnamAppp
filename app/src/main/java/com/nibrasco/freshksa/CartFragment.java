package com.nibrasco.freshksa;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.nibrasco.freshksa.Model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private Cart cart;
    private ArrayList<CartItemCategory> items;

    private ImageView rmvImg;
    private RecyclerView itemsView;
    private TextView txtCartTotal, btnOrder;
    private Button btnConfirmCart;
    private Float Total = 0.0f;
    public CartFragment() {
        // Required empty public constructor
        cart = Session.getInstance().Cart();
        Total = cart.GetTotal();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        LinkControls(v);
        DisplayValues();
        Boolean flipListeners = cart != null && cart.Items().size() > 0;
        if(flipListeners) {
            FillRecyclerView(v);
            AssignListeners(flipListeners);
        }
        else {
            btnOrder.setVisibility(View.GONE);
            btnConfirmCart.setText(btnOrder.getText());
            AssignListeners(flipListeners);
        }
    }

    private void DisplayValues() {
        txtCartTotal.setText(Float.toString(Total));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
    private void LinkControls(View v) {
        rmvImg = (ImageView)v.findViewById(R.id.rmvImg);
        itemsView = (RecyclerView)v.findViewById(R.id.recyclerCartItems);
        txtCartTotal = (TextView)v.findViewById(R.id.txtCartTotal);
        btnOrder = (TextView) v.findViewById(R.id.btnItemOrder);
        btnConfirmCart = (Button)v.findViewById(R.id.btnConfirmCart);
    }
    private void FillRecyclerView(View v) {
        if(cart != null && cart.Items().size() > 0) {
            items = new ArrayList<>();
            List<Cart.Item> cartItems = cart.Items();
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
    private void AssignListeners(Boolean flipped) {
        View.OnClickListener orderListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderItemFragment f = new OrderItemFragment();
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.homeContainer, f);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        View.OnClickListener shippingListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShippingDetailsFragment f = new ShippingDetailsFragment();
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.homeContainer, f);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };

        itemsView.addOnItemTouchListener(new RecyclerItemTouchListener(getActivity().getApplicationContext(), itemsView, new RecyclerItemTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                RemoveItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        if (!flipped) {
            btnConfirmCart.setOnClickListener(orderListener);
        }else {
            btnConfirmCart.setOnClickListener(shippingListener);
            btnOrder.setOnClickListener(orderListener);
        }
    }
    void RemoveItem(final int index){
        try{
            if(index >= 0 && cart.Items().size() > 0) {
                items.remove(index);
                itemsView.getAdapter().notifyDataSetChanged();
                Session.getInstance().Cart(cart);
                final FirebaseDatabase db = FirebaseDatabase.getInstance();
                final DatabaseReference tblCart = db.getReference("Cart");
                tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot cartsSnap) {
                        String id = Session.getInstance().User().getCart();
                        tblCart.child(id).child("Items").child(Integer.toString(cart.GetItem(index).getCategory().Value())).removeValue();
                        cart.RemoveItem(index);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                Boolean flipListeners = cart != null && cart.Items().size() > 0;
                if (flipListeners) {
                    FillRecyclerView(getView());
                    AssignListeners(flipListeners);
                } else {
                    btnOrder.setVisibility(View.GONE);
                    btnConfirmCart.setText(btnOrder.getText());
                    AssignListeners(flipListeners);
                }
            }
        }catch (Exception e){
        }

    }
}
