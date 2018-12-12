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
import com.google.firebase.database.*;
import com.nibrasco.freshksa.Model.Session;
import com.nibrasco.freshksa.Model.User;

public class SignUp extends AppCompatActivity {

    EditText edtPhone, edtName, edtPwd;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtPhone = (EditText)findViewById(R.id.edtPhone);
        edtPwd = (EditText)findViewById(R.id.edtPwd);
        edtName = (EditText)findViewById(R.id.edtName);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference tblUser = db.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snack = Snackbar.make(v, "Registering Your Account...", Snackbar.LENGTH_LONG);
                snack.setActionTextColor(Color.WHITE).show();
                tblUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            snack.dismiss();
                            String name = edtName.getText().toString(),
                                    pwd = edtPwd.getText().toString(),
                                    phone = edtPhone.getText().toString();
                            Session.getInstance().User(new User(name, pwd));
                            Session.getInstance().User().MapToDbRef(tblUser.child(phone));
                            snack.setText("Account Registered Successfully")
                                    .setActionTextColor(Color.GREEN).show();
                            startActivity(new Intent(SignUp.this, Home.class));
                            snack.dismiss();
                        }
                        else
                        {
                            snack.dismiss();
                            snack.setText("Phone number already exists, please choose another phone number!")
                                    .setActionTextColor(Color.RED).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
