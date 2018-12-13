package com.nibrasco.freshksa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.google.firebase.database.*;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.Model.ViewPagerItemAdapter;

public class OrderItemFragment extends Fragment{


    private EditText edtNotes;
    private TextView txtTotal;
    private Button btnConfirm;

    private ViewPager itemsView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private Cart.eCategory selectedCategory = Cart.eCategory.None;
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
        LoadContent();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCategory != Cart.eCategory.None) {
                    //Add the item to the cart at this point
                    SaveChanges(v);
                }
            }
        });
    }
    private void SaveChanges(View v)
    {
        final Snackbar snack = Snackbar.make(v, "Saving Your Order", Snackbar.LENGTH_LONG);
        snack.show();
        Session.getInstance().Cart().AddItem(Session.getInstance().Item());
        //if(Session.getInstance().User().getCart().equals("0"))
        //{
            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            final DatabaseReference tblCart = db.getReference("Cart");
            tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot cartsSnap) {
                    if(Session.getInstance().User().getCart().equals("0")) {
                        long count = cartsSnap.getChildrenCount();
                        Session.getInstance().Cart().MapToDbRef(tblCart.child(Long.toString(count)));
                    }
                    else {
                        Session.getInstance().Cart().MapToDbRef(tblCart.child(Session.getInstance().User().getCart()));
                    }
                    snack.dismiss();
                    CartFragment f = new CartFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.homeContainer, f);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        //}
    }
    private void LinkControls(View v)
    {

        edtNotes = (EditText)v.findViewById(R.id.edtNotes);

        txtTotal = (TextView)v.findViewById(R.id.txtTotal);
        btnConfirm = (Button)v.findViewById(R.id.btnItemOrder);
        itemsView = (ViewPager) v.findViewById(R.id.viewPagerItems);
        toolbar =  v.findViewById(R.id.toolbar);


        //ActionBar.setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
    }
    private void LoadContent()
    {
        ViewPagerItemAdapter adapter = new ViewPagerItemAdapter(getFragmentManager());
        adapter.addFragment(new SheepFragment(), getResources().getString(R.string.recyclerItemSheep));
        adapter.addFragment(new SheepFragment(), getResources().getString(R.string.recyclerItemGoat));
        adapter.addFragment(new CamelFragment(), getResources().getString(R.string.recyclerItemGroundMeat));
        adapter.addFragment(new CamelFragment(), getResources().getString(R.string.recyclerItemCamel));
        adapter.addFragment(new CamelFragment(), getResources().getString(R.string.recyclerItemGroundLamb));

        itemsView.setAdapter(adapter);
        itemsView.setCurrentItem(0);

        tabLayout.setupWithViewPager(itemsView);
        int i = 0;
        for (Cart.eCategory category:
                Cart.eCategory.values()) {
            if(i < tabLayout.getTabCount() && category != Cart.eCategory.None && category != Cart.eCategory.HalfSheep) {
                switch (category) {
                    case Goat:
                        tabLayout.getTabAt(i).setIcon(category.Value());
                        break;
                    case Sheep:
                        tabLayout.getTabAt(i).setIcon(category.Value());
                        break;
                    case GroundMeat:
                        tabLayout.getTabAt(i).setIcon(category.Value());
                        break;
                    case Camel:
                        tabLayout.getTabAt(i).setIcon(category.Value());
                        break;
                    case HalfSheep:
                        break;
                }
                i++;
            }

        }
        //Fragment fragment = new SheepFragment();
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.replace(R.id.viewPagerItems, fragment);
        //ft.commit();
    }
}
