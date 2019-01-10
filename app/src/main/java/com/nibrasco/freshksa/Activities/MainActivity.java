package com.nibrasco.freshksa.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.nibrasco.freshksa.R;

public class MainActivity extends AppCompatActivity {

    MaterialButton btnSignIn, btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.nibrasco.freshksa.R.layout.activity_main);
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)){
            finish();
            return;
        }

        btnSignIn = (MaterialButton)findViewById(com.nibrasco.freshksa.R.id.btnMainSignIn);
        btnSignUp = (MaterialButton)findViewById(R.id.btnMainSignUp);
//
//
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signIn);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainActivity.this, SignUpAcitivity.class);
                startActivity(signUp);
            }
        });
        //OutputToDB();
    }
}

