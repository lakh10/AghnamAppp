package com.nibrasco.freshksa.Model;

import android.provider.ContactsContract;
import com.google.firebase.database.DatabaseReference;

public class Delivery {
    private String CartId;
    private String Address;
    private String Account;
    private String UserName;

    public String getCartId() {
        return CartId;
    }

    public void setCartId(String cartId) {
        CartId = cartId;
    }

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
        CartId = Session.getInstance().User().getCart();
        Address = Session.getInstance().Cart().getAddress();
        Account = Session.getInstance().Cart().getBankAccount();
        UserName = Session.getInstance().User().getName();
    }
    public void MapToDbRef(DatabaseReference dRef){
        DatabaseReference ref = dRef.child(CartId);
        ref.child("Address").setValue(Address);
        ref.child("Account").setValue(Account);
        ref.child("UserName").setValue(UserName);
    }
}
