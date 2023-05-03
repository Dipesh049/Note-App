package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding forgotXml;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotXml = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(forgotXml.getRoot());
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getApplicationContext());

        forgotXml.goBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this,MainActivity.class));
            }
        });

        forgotXml.sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = forgotXml.forgotPassword.getText().toString().trim();
                if(email.isEmpty())
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                }else {
                    // send email code
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPasswordActivity.this, "Email sent, You can Reset your password", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this,MainActivity.class));
                            }else{
                                Toast.makeText(ForgotPasswordActivity.this, "Email is Not Registered", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });

    }
}