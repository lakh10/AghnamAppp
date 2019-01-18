package com.nibrasco.aghnam.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String Name;
    private String Phone;
    private String Password;
    private String Cart;
    private List<String> Orders;

    public String getCart() {
        return Cart;
    }

    public void setCart(String cart) {
        Cart = cart;
    }

    public List<String> getOrders() {
        return Orders;
    }

    public void setOrders(List<String> orders) {
        Orders = orders;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public void AddOrder(String id){
        Orders.add(id);
    }
    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Phone = "06...";
        Cart = "0";
        Orders = new ArrayList<>();
        Password = password;
    }

    public User(DataSnapshot userSnap)
    {
        Phone = userSnap.getKey();
        Name = userSnap.child("Name").getValue(String.class);
        Password = userSnap.child("Password").getValue(String.class);
        Cart = userSnap.child("Cart").getValue(String.class);
        Orders = new ArrayList<>();
        if (userSnap.hasChild("Orders")){
            DataSnapshot ordersSnap = userSnap.child("Orders");
            if(!ordersSnap.hasChild("0") && ordersSnap.getChildrenCount() >= 1) {
                for (DataSnapshot orderSnap : ordersSnap.getChildren()) {
                    AddOrder(orderSnap.getKey());
                }
            }
        }

    }

    public void MapToDbRef(DatabaseReference userRef)
    {
        userRef.child("Name").setValue(Name);
        userRef.child("Password").setValue(Password);
        userRef.child("Cart").setValue(Cart);

        DatabaseReference ordersRef = userRef.child("Orders");
        if(Orders.size() == 0) {
            ordersRef.child("0").setValue("");
        } else {
            for (String item : Orders) {
                ordersRef.child(item).setValue("");
            }
        }
    }
}
