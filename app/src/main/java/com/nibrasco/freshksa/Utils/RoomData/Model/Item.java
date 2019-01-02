package com.nibrasco.freshksa.Utils.RoomData.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "Item")
public class Item {
    private int Category=  -1;
    private int Quantity = 0;
    private boolean Intestine = false;
    private String Notes = "";
    private int Slicing = -1;
    private int Packaging = 0;
    private int Weight = -1;
    @ForeignKey(
            entity = Order.class,
            parentColumns ={"id"},
            childColumns = {"order"},
            onDelete = ForeignKey.CASCADE
    )
    private int order = -1;
    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public boolean isIntestine() {
        return Intestine;
    }

    public void setIntestine(boolean intestine) {
        Intestine = intestine;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public int getSlicing() {
        return Slicing;
    }

    public void setSlicing(int slicing) {
        Slicing = slicing;
    }

    public int getPackaging() {
        return Packaging;
    }

    public void setPackaging(int packaging) {
        Packaging = packaging;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }
}
