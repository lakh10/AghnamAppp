package com.nibrasco.freshksa.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.Model.User;
import com.nibrasco.freshksa.R;
import com.nibrasco.freshksa.Utils.PreferenceManager;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText edtPhone, edtPwd;
    private Button btnSignIn;
    private User user;
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    final DatabaseReference tblUser = db.getReference("User");
    final DatabaseReference tblCart = db.getReference("Cart");

    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(SignInActivity.this);



        setContentView(com.nibrasco.freshksa.R.layout.activity_sign_in);
        LinkControls();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Authenticate(v);
            }
        });

    }
    private void LinkControls(){
        edtPhone = (TextInputEditText)findViewById(com.nibrasco.freshksa.R.id.edtPhone);
        edtPwd = (TextInputEditText)findViewById(com.nibrasco.freshksa.R.id.edtPwd);
        btnSignIn = findViewById(com.nibrasco.freshksa.R.id.edtSignUpPhone);
    }

    private void Authenticate(View v){
        final String phone = edtPhone.getText().toString();
        final String pwd = edtPwd.getText().toString();
        if(!phone.equals("") &&  !pwd.equals("")) {
            String message = getResources().getString(com.nibrasco.freshksa.R.string.msgSignInLoadingProfile);
            final Snackbar snack = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE);
            snack.show();
            tblUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                        snack.dismiss();

                        user = new User(dataSnapshot.child(phone));
                        if (user.getPassword().equals(pwd)) {
                            String message = getResources().getString(com.nibrasco.freshksa.R.string.msgSignInSuccess);
                            ((TextView)(snack.setText(message)
                                    .getView().findViewById(android.support.design.R.id.snackbar_text)))
                                    .setTextColor(Color.GREEN);
                                    snack.show();
                            GetCart();
                            Session.getInstance().User(user);
                            //preferenceManager.setUserPhone(phone);
                            startActivity(new Intent(SignInActivity.this, HomeActivity.class));

                        } else {
                            String message = getResources().getString(com.nibrasco.freshksa.R.string.msgSignInFailed);
                            ((TextView)(snack.setText(message)
                                    .getView().findViewById(android.support.design.R.id.snackbar_text)))
                                    .setTextColor(Color.YELLOW);
                            snack.show();
                        }
                    } else {
                        String message = getResources().getString(com.nibrasco.freshksa.R.string.msgSignInInexistant);
                        ((TextView)(snack.setText(message)
                                .getView().findViewById(android.support.design.R.id.snackbar_text)))
                                .setTextColor(Color.RED);
                        snack.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{

            Snackbar.make(v, getResources().getString(R.string.msgSignInEmpty), Snackbar.LENGTH_LONG).show();
        }
    }

    private void GetCart(){
        tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot cartsSnap) {
                String id = Session.getInstance().User().getCart();
                if (!id.equals("0"))
                    Session.getInstance().Cart(new Cart(cartsSnap.child(id)));
                else {
                    Session.getInstance().Cart(new Cart());
                    user.setCart(Long.toString(cartsSnap.getChildrenCount()));
                    user.MapToDbRef(tblUser.child(user.getPhone()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
