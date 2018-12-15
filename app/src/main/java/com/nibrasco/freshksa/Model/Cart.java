package com.nibrasco.freshksa.Model;

import android.location.Address;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.nibrasco.freshksa.R;

import java.util.*;

public class Cart {

    public static class WeightLists
    {
        public static class Weight
        {
            Float Price;
            String Name;

            public Weight(Float price, String name) {
                Price = price;
                Name = name;
            }
        }
        private static ArrayList<Weight> Weights;

        public static String GetName(eCategory Category, int index)
        {
            Weights = GetList(Category);
            if(index < Weights.size())
                return Weights.get(index).Name;
            return "NaN";
        }
        public static float GetPrice(eCategory Category, int index)
        {
            Weights = GetList(Category);
            if (index < Weights.size())
                return Weights.get(index).Price;
            return -1;
        }

        public static ArrayList<Weight> GetList(eCategory Category){
            Weights = new ArrayList<>();
            switch (Category){
                case HalfSheep:
                    Weights.add( new Weight(660.0f, "X"));
                    break;
                case Sheep:
                    Weights.add( new Weight(950.0f, "A"));
                    Weights.add( new Weight(1030.0f, "B"));
                    Weights.add( new Weight(1120.0f, "C"));
                    Weights.add( new Weight(1210.0f, "D"));
                    Weights.add( new Weight(1290.0f, "E"));
                    Weights.add( new Weight(1370.0f, "F"));
                    break;
                case Goat:
                    Weights.add( new Weight(850.0f, "A"));
                    Weights.add( new Weight(920.0f, "B"));
                    Weights.add( new Weight(990.0f, "C"));
                    break;
                case Camel:
                    Weights.add( new Weight(400.0f, "A"));
                    Weights.add( new Weight(600.0f, "B"));
                    Weights.add( new Weight(800.0f, "C"));
                    Weights.add( new Weight(950.0f, "D"));
                    break;
                case GroundMeat:
                    Weights.add( new Weight(400.0f, "A"));
                    Weights.add( new Weight(430.0f, "B"));
                    break;
                default:
                    break;
            }
            return Weights;
        }

        public static ArrayList<String> GetNames(eCategory Category)
        {
            ArrayList<String> list = new ArrayList<>();
            Weights = GetList(Category);
            for (Weight w:
                 Weights) {
                list.add(w.Name);
            }
            return list;
        }
    }

    public enum eTime
    {
        Noon(0),
        AfterNoon(1),
        Evening(2);
        private final int value;
        eTime(int value) {
            this.value = value;
        }

        public int Value() {
            return value;
        }
        public static eTime Get(int value){
            for(eTime t : values()){
                if(t.value == value) return t;
            }
            return Noon;
        }
    }
    public enum eSlicing
    {
        None(0),
        Fridge(1),
        Halfs(2),
        Quarters(3),
        Whole(4);
        private final int value;
        eSlicing(int value) {
            this.value = value;
        }

        public int Value() {
            return value;
        }
        public static eSlicing Get(int value){
            for(eSlicing t : values()){
                if(t.value == value) return t;
            }
            return None;
        }
    }

    public enum ePackaging
    {
        None(0),
        Bags(1),
        Plates(2);
        private final int value;
        ePackaging(int value) {
            this.value = value;
        }

        public int Value() {
            return value;
        }
        public static ePackaging Get(int value){
            for(ePackaging t : values()){
                if(t.value == value) return t;
            }
            return None;
        }
    }

    public enum eCategory
    {
        None(-1),
        Sheep(R.drawable.naeme),
        Lamb(R.drawable.hree),
        Goat(R.drawable.tais),
        GroundMeat(R.drawable.ajl),
        Camel(R.drawable.hashe),
        HalfSheep(0);
        private final int value;
        eCategory(int value) {
            this.value = value;
        }

        public int Value() {
            return value;
        }
        public static eCategory Get(int value){
            for(eCategory t : values()){
                if(t.value == value) return t;
            }
            return None;
        }
    }

    public static class Item {

        private eCategory Category= eCategory.None;
        private int Quantity = 0;
        private boolean Intestine = false;
        private String Notes = "";
        private eSlicing Slicing = eSlicing.None;
        private ePackaging Packaging = ePackaging.None;
        private int Weight = -1;
        private float Total = 0.0f;

        public eCategory getCategory() {
            return Category;
        }

        public void setCategory(int category) {
            Category = eCategory.Get(category);
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

        public eSlicing getSlicing() {
            return Slicing;
        }

        public void setSlicing(int slicing) {
            Slicing = eSlicing.Get(slicing);
        }

        public ePackaging getPackaging() {
            return Packaging;
        }

        public void setPackaging(int packaging) {
            Packaging = ePackaging.Get(packaging);
        }

        public int getWeight() {
            return Weight;
        }

        public void setWeight(int weightIndex) {
            Weight = weightIndex;
            Total = WeightLists.GetPrice(Category, Weight);
        }

        public float getTotal() {
            return Total;
        }

        public void setTotal(float total) {
            Total = total;
        }

        public float getDefaultPrice()
        {
            switch (Category)
            {
                case None:
                    return 0.0f;
                case Sheep:
                    return 1030.0f;
                case HalfSheep:
                    return 660.0f;
                case Goat:
                    return 990.0f;
                case GroundMeat:
                case Camel:
                    return 400.0f;
                    default:
                        return 0.0f;
            }
        }

        public Item() {

        }

        public Item(DataSnapshot itemSnap)
        {
            Category = eCategory.Get(Integer.parseInt(itemSnap.getKey()));
            Notes = itemSnap.child("Notes").getValue(String.class);
            Total = Float.parseFloat(itemSnap.child("Total").getValue().toString());
            Intestine = (Integer.parseInt(itemSnap.child("Intestine").getValue().toString()) != 0);
            Packaging  = ePackaging.Get(Integer.parseInt(itemSnap.child("Packaging").getValue().toString()));
            Quantity = Integer.parseInt(itemSnap.child("Quantity").getValue().toString());
            Slicing = eSlicing.Get(Integer.parseInt(itemSnap.child("Slicing").getValue().toString()));
            Weight = Integer.parseInt(itemSnap.child("Weight").getValue().toString());
        }
        public void MapToDbRef(DatabaseReference itemsRef)
        {
            DatabaseReference itemRef = itemsRef.child(Integer.toString(Category.value));
            itemRef.child("Intestine").setValue(Intestine ? 1 : 0);
            itemRef.child("Notes").setValue(Notes);
            itemRef.child("Packaging").setValue(Packaging.value);
            itemRef.child("Quantity").setValue(Quantity);
            itemRef.child("Slicing").setValue(Slicing.value);
            itemRef.child("Total").setValue(Total);
            itemRef.child("Weight").setValue(Weight);
        }

        public String ToString()
        {
            return "\nCategory : " + Category.value
                    + "\nQuantity : " +  Quantity
                    + "\nPackaging : " + Packaging.value
                    + "\nSlicing : " + Slicing.value
                    + "\nWeight : " + WeightLists.GetName(Category, Weight)
                    + "\nNotes : " + Notes
                    + "\nTotal : " + Total;
        }

    }

    private eTime TimeOfDelivery;
    private String Address;
    private String BankAccount;
    private List<Item> Items;

    public String getBankAccount() {
        return BankAccount;
    }

    public void setBankAccount(String bankAccount) {
        BankAccount = bankAccount;
    }

    public String getAddress() {
        return Address;
    }
    public void setAddress(android.location.Address address)
    {
        Address += address.getLocality() + " " + address.getCountryName();
    }

    public eTime getTimeOfDelivery() {
        return TimeOfDelivery;
    }

    public void setTimeOfDelivery(int TimeOfDelivery) {
        this.TimeOfDelivery = eTime.Get(TimeOfDelivery);
    }

    public Integer GetCount()
    {
        return Items.size();
    }
    public Item GetItem(int index)
    {
        return Items.get(index);
    }
    public void AddItem(Item item)
    {
        Items.add(item);
    }
    public void RemoveItem(Item item)
    {
        Items.remove(item);
    }
    public void ModifyItem(int index, Item newItem)
    {
        Items.set(index, newItem);
    }
    public List<Item> Items(){return Items;}

    public Cart() {
        Address = "N/A";
        TimeOfDelivery = eTime.Noon;
        BankAccount = "0";
        Items = new ArrayList<Item>();
    }

    public Cart(String address) {
        Address = address;
        TimeOfDelivery = eTime.Noon;
        BankAccount = "0";
        Items = new ArrayList<Item>();
    }

    /*
    public Cart(String address, ArrayList<Item> items) {
        Address = address;
        Items = items;
    }
    */
    public Cart(DataSnapshot cart)
    {
        Address = cart.child("Address").getValue(String.class);
        TimeOfDelivery = eTime.Get(Integer.parseInt(cart.child("TimeOfDelivery").getValue().toString()));
        //BankAccount = cart.child("BankAccount").getValue().toString();
        Items = new ArrayList<Item>();
        if(cart.child("Items").exists() || cart.child("Items").hasChildren())
        {
            DataSnapshot itemsSnap = cart.child("Items");

            for(DataSnapshot itemSnap : itemsSnap.getChildren())
            {
                if(itemSnap.hasChildren())
                    Items.add(new Item(itemSnap));
            }
        }
    }

    public void MapToDbRef(DatabaseReference cartRef)
    {
        cartRef.child("Address").setValue(Address);
        cartRef.child("TimeOfDelivery").setValue(TimeOfDelivery.Value());
        cartRef.child("BankAccount").setValue(BankAccount);
        if(Items.size() > 0){
            DatabaseReference itemsRef = cartRef.child("Items");
            for (Item item : Items) {
                item.MapToDbRef(itemsRef);
            }
        }

    }
    public float GetTotal()
    {
        if (Items.size() > 0)
        {
            float t = 0.0f;
            for (Item i:
                 Items) {
                t += i.Total;
            }
            return t;
        }
        return 0.0f;
    }
    public String ToString()
    {
        String obj = "Address = " + Address + "\nItems(Count = " + Items.size() + "):\n";
        for(Item item : Items)
        {
            obj += item.ToString();
        }
        return obj;
    }

}
