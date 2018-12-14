package com.nibrasco.freshksa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.Model.User;

public class SignIn extends AppCompatActivity {

    private EditText edtPhone, edtPwd;
    private Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtPhone = (EditText)findViewById(R.id.edtPhone);
        edtPwd = (EditText)findViewById(R.id.edtPwd);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference tblUser = db.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String phone = edtPhone.getText().toString();
                final String pwd = edtPwd.getText().toString();
                if(!phone.equals("") &&  !pwd.equals("")) {
                    final Snackbar snack = Snackbar.make(v, "Authenticating...", Snackbar.LENGTH_INDEFINITE);
                    snack.show();

                    tblUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                snack.dismiss();

                                User user = new User(dataSnapshot.child(phone));
                                if (user.getPassword().equals(pwd)) {
                                    snack.setText("Sign In Successfull!");
                                    TextView textView = (TextView) snack.getView().findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.GREEN);
                                    snack.show();
                                    Session.getInstance().User(user);
                                    final DatabaseReference tblCart = db.getReference("Cart");
                                    tblCart.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot cartsSnap) {
                                            String id = Session.getInstance().User().getCart();
                                            if (cartsSnap.child(id).exists())
                                                Session.getInstance().Cart(new Cart(cartsSnap.child(id)));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    startActivity(new Intent(SignIn.this, Home.class));

                                } else {
                                    snack.setText("Sign In Failed!");
                                    TextView textView = (TextView) snack.getView().findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.RED);
                                    snack.show();
                                }
                            } else {
                                snack.dismiss();
                                snack.setText("User Inexistant, please create and Account!");
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
                    Snackbar.make(v, "Fields cannot be empty!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
