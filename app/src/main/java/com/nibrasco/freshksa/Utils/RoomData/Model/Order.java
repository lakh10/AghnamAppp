package com.nibrasco.freshksa.Utils.RoomData.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Order {

    @PrimaryKey
    private int id;
    private int TimeOfDelivery;
    private String Address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeOfDelivery() {
        return TimeOfDelivery;
    }

    public void setTimeOfDelivery(int timeOfDelivery) {
        TimeOfDelivery = timeOfDelivery;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

}
