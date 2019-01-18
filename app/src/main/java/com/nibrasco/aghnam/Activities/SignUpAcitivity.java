package com.nibrasco.aghnam.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.*;
import com.nibrasco.aghnam.Model.Cart;
import com.nibrasco.aghnam.Model.Session;
import com.nibrasco.aghnam.Model.User;
import com.nibrasco.aghnam.R;

public class SignUpAcitivity extends AppCompatActivity {

    private EditText edtPhone, edtName, edtPwd;
    private Button btnSignUp;
    private DatabaseReference tblUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.nibrasco.aghnam.R.layout.activity_sign_up);
        edtPhone = (EditText)findViewById(com.nibrasco.aghnam.R.id.edtSignUpPhone);
        edtPwd = (EditText)findViewById(com.nibrasco.aghnam.R.id.edtSignUpPwd);
        edtName = (EditText)findViewById(com.nibrasco.aghnam.R.id.edtSignUpName);
        btnSignUp = (Button)findViewById(com.nibrasco.aghnam.R.id.btnSignUp);

        try {
            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            tblUser = db.getReference("User");

            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String name = edtName.getText().toString(),
                            pwd = edtPwd.getText().toString(),
                            phone = edtPhone.getText().toString();
                    if (!phone.equals("") || !pwd.equals("")) {
                        if (phone.length() >= 10) {
                            String message = getResources().getString(com.nibrasco.aghnam.R.string.msgSignUpRegister);
                            final Snackbar snack = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
                            snack.setActionTextColor(Color.WHITE).show();
                            tblUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                        snack.dismiss();

                                        Session.getInstance().User(new User(name, pwd));
                                        Session.getInstance().User().setPhone(phone);
                                        Session.getInstance().Cart(new Cart());
                                        final DatabaseReference tblCart = db.getReference("Cart");
                                        tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot cartsSnap) {
                                                String id = Session.getInstance().User().getCart();
                                                if (!id.equals("0"))
                                                    Session.getInstance().Cart(new Cart(cartsSnap.child(id)));
                                                else {
                                                    Session.getInstance().User().setCart(Long.toString(cartsSnap.getChildrenCount()));
                                                    Session.getInstance().User().MapToDbRef(tblUser.child(phone));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        snack.setText(getResources().getString(com.nibrasco.aghnam.R.string.msgSignUpSuccess))
                                                .setActionTextColor(Color.GREEN)
                                                .show();
                                        startActivity(new Intent(SignUpAcitivity.this, HomeActivity.class));
                                        snack.dismiss();
                                    } else {
                                        snack.dismiss();
                                        snack.setText(getResources().getString(com.nibrasco.aghnam.R.string.msgSignInRegisterFailed))
                                                .setActionTextColor(Color.RED).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {

                            Snackbar.make(v, getResources().getString(com.nibrasco.aghnam.R.string.msgSignUpWrongNumber), Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.YELLOW)
                                    .show();
                        }

                    } else {

                        Snackbar.make(v, getResources().getString(R.string.msgSignInEmpty), Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED)
                                .show();
                    }
                }
            });
        }catch(Exception e){
            Log.e(SignUpAcitivity.class.getName(), e.getMessage());
        }
    }
}
