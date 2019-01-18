package com.nibrasco.aghnam.Model;

import com.google.firebase.database.DatabaseReference;

public class Delivery {
    //private String CartId;
    private String Address;
    private String Account;
    private String UserName;

    //public String getCartId() {
    //    return CartId;
    //}
//
    //public void setCartId(String cartId) {
   //    CartId = cartId;
   //}

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void Delivery(){
        //CartId = Session.getInstance().User().getCart();
        Address = Session.getInstance().Cart().getAddress();
        Account = Session.getInstance().Cart().getBankAccount();
        UserName = Session.getInstance().User().getName();
    }
    public void MapToDbRef(DatabaseReference dRef){
        dRef.child("Address").setValue(Address);
        dRef.child("Account").setValue(Account);
        dRef.child("UserName").setValue(UserName);
    }
}
