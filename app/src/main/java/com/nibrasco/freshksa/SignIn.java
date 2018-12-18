package com.nibrasco.freshksa;

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

public class SignIn extends AppCompatActivity {

    private TextInputEditText edtPhone, edtPwd;
    private Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtPhone = (TextInputEditText)findViewById(R.id.edtPhone);
        edtPwd = (TextInputEditText)findViewById(R.id.edtPwd);

        btnSignIn = findViewById(R.id.edtSignUpPhone);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference tblUser = db.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String phone = edtPhone.getText().toString();
                final String pwd = edtPwd.getText().toString();
                if(!phone.equals("") &&  !pwd.equals("")) {
                    String message = getResources().getString(R.string.msgSignInLoadingProfile);
                    final Snackbar snack = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE);
                    snack.show();

                    tblUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                snack.dismiss();

                                User user = new User(dataSnapshot.child(phone));
                                if (user.getPassword().equals(pwd)) {
                                    // TODO: replace the string with the
                                    String message = getResources().getString(R.string.msgSignInSuccess);
                                    snack.setText(message);
                                    TextView textView = (TextView) snack.getView().findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.GREEN);
                                    snack.show();
                                    Session.getInstance().User(user);
                                    final DatabaseReference tblCart = db.getReference("Cart");
                                    tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot cartsSnap) {
                                            String id = Session.getInstance().User().getCart();
                                            if (!id.equals("0"))
                                                Session.getInstance().Cart(new Cart(cartsSnap.child(id)));
                                            else {
                                                Session.getInstance().Cart(new Cart());
                                                Session.getInstance().User().setCart(Long.toString(cartsSnap.getChildrenCount()));
                                                Session.getInstance().User().MapToDbRef(tblUser.child(Session.getInstance().User().getPhone()));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    startActivity(new Intent(SignIn.this, Home.class));

                                } else {
                                    String message = getResources().getString(R.string.msgSignInFailed);
                                    snack.setText(message);
                                    TextView textView = (TextView) snack.getView().findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.RED);
                                    snack.show();
                                }
                            } else {
                                String message = getResources().getString(R.string.msgSignInInexistant);
                                snack.setText(message);
                                TextView textView = (TextView) snack.getView().findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
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
        });
    }
}
