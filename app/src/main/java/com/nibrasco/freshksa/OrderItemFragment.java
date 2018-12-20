package com.nibrasco.freshksa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.google.firebase.database.*;
import com.nibrasco.freshksa.Model.*;

import java.util.ArrayList;

public class OrderItemFragment extends Fragment {


    private RecyclerView itemsView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orderitem, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View v = getView();
        Session.getInstance().Item(new Cart.Item());
        LinkControls(v);
        LoadContent(v);
    }
    private Boolean SaveChanges(View v) {
        final Snackbar snack = Snackbar.make(v, "Saving Your Order", Snackbar.LENGTH_LONG);
        snack.show();
        final Boolean[] success = {true};
        Session.getInstance().Cart().AddItem(Session.getInstance().Item());
        //if(Session.getInstance().User().getCart().equals("0"))
        //{
            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            final DatabaseReference tblCart = db.getReference("Cart");
            tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot cartsSnap) {

                    DatabaseReference cartRef = tblCart.child(Session.getInstance().User().getCart());
                    Session.getInstance().Item().MapToDbRef(cartRef.child("Items"));

                    snack.dismiss();
                    success[0] = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    success[0] = false;
                }
            });
        //}
        return success[0];
    }

    private void LinkControls(View v)
    {

        itemsView = (RecyclerView) v.findViewById(R.id.recyclerItems);
    }
    private void LoadContent(final View v)
    {
        final ArrayList<ItemCategory> list = new ArrayList<>();
        for (Cart.eCategory c : Cart.eCategory.values()) {
            if(c != Cart.eCategory.None)
                list.add(new ItemCategory(getResources(), c));
        }

        final RecyclerItemAdapter adapter = new RecyclerItemAdapter(v.getContext(), list);
        itemsView.addOnItemTouchListener(
                new RecyclerItemTouchListener(getActivity().getApplicationContext(), itemsView, new RecyclerItemTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        ItemCategory item = list.get(position);
                        Session.getInstance().Item().setCategory(item.getImage_drawable());
                        Cart.eCategory category = Session.getInstance().Item().getCategory();
                        Fragment fragment = null;
                        switch(category)
                        {
                            case Goat:
                                fragment = new SheepFragment();
                                break;
                            case Sheep:
                                fragment = new SheepFragment();
                                break;
                            case GroundMeat:
                                fragment = new CamelFragment();
                                break;
                            case Camel:
                                fragment = new CamelFragment();
                                break;
                            case HalfSheep:
                                fragment = new HalfFragment();
                                break;
                        }
                        if (fragment != null) {
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.orderItemContentFrame, fragment);
                            ft.commit();
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        itemsView.setAdapter(adapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        itemsView.setLayoutManager(layoutManager);

        Session.getInstance().Item().setCategory(Cart.eCategory.Sheep.Value());
        Fragment fragment = new SheepFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.orderItemContentFrame, fragment);
        ft.commit();
    }
}
