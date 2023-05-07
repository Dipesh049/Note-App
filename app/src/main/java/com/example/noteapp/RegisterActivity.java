package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding RegisterXml;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegisterXml = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(RegisterXml.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getApplicationContext());

        RegisterXml.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });

        RegisterXml.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = RegisterXml.txtUsername.getText().toString().trim();
                String email = RegisterXml.txtEmail.getText().toString().trim();
                String password = RegisterXml.txtPassword.getText().toString().trim();
                StringBuilder error =  new StringBuilder("Please Enter ");
                int fieldEmpty = 0;
                if(username.isEmpty()){
                    error.append("Username ");
                    fieldEmpty++;
                }
                if(email.isEmpty()){
                    if(username.isEmpty()){
                        error.append("and Email ");
                    }else{
                        error.append("Email ");
                    }
                    fieldEmpty++;
                }
                if(password.isEmpty()){
                    if(username.isEmpty() || email.isEmpty()){
                        error.append("and Password ");
                    }else{
                        error.append("Password ");
                    }
                    fieldEmpty++;
                }
                if(fieldEmpty==3){
                    error.replace(0,error.length(),"");
                    error.append("All Fields are Required");
                }else if(fieldEmpty==0){
                    error.replace(0,error.length(),"");
                    //register user
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                RegisterXml.txtError.setText(error.toString());
            }
        });
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Verification Email is Sent, Verify and login again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "Failed to send Verification Email", Toast.LENGTH_SHORT).show();
        }
    }
}