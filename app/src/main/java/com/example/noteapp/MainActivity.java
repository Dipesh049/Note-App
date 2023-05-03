package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.noteapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainXml;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainXml = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainXml.getRoot());
        getSupportActionBar().hide();

        mainXml.gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        mainXml.gotoRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ForgotPasswordActivity.class));
            }
        });

        mainXml.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mainXml.txtEmail.getText().toString().trim();
                String password = mainXml.txtPassword.getText().toString().trim();

                if(email.isEmpty() && password.isEmpty()){
                    mainXml.txtError.setText("Please Enter Email and Password");
                }
                if (email.isEmpty() && !password.isEmpty()){
                    mainXml.txtError.setText("Please Enter Email");
                }else if(!email.isEmpty() && password.isEmpty()){
                    mainXml.txtError.setText("Please Enter Password");
                }else{
                    // login user

                }

            }
        });


    }
}