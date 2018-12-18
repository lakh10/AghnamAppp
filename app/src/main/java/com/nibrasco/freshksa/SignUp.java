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

    private EditText edtPhone, edtName, edtPwd;
    private Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtPhone = (EditText)findViewById(R.id.edtSignUpPhone);
        edtPwd = (EditText)findViewById(R.id.edtSignUpPwd);
        edtName = (EditText)findViewById(R.id.edtSignUpName);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference tblUser = db.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = edtName.getText().toString(),
                        pwd = edtPwd.getText().toString(),
                        phone = edtPhone.getText().toString();
                if(!phone.equals("") &&  !pwd.equals("")) {
                    String message = getResources().getString(R.string.msgSignUpRegister);
                    final Snackbar snack = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
                    snack.setActionTextColor(Color.WHITE).show();
                    tblUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                snack.dismiss();

                                Session.getInstance().User(new User(name, pwd));
                                Session.getInstance().User().MapToDbRef(tblUser.child(phone));
                                snack.setText(getResources().getString(R.string.msgSignUpSuccess))
                                        .setActionTextColor(Color.GREEN)
                                        .show();
                                startActivity(new Intent(SignUp.this, Home.class));
                                snack.dismiss();
                            } else {
                                snack.dismiss();
                                snack.setText(getResources().getString(R.string.msgSignInRegisterFailed))
                                        .setActionTextColor(Color.RED).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{

                    Snackbar.make(v, getResources().getString(R.string.msgSignInEmpty), Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.YELLOW)
                            .show();
                }
            }
        });
    }
}
