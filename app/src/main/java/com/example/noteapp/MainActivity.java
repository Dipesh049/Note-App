package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityMainBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainXml;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainXml = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainXml.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


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
                }else if (email.isEmpty() && !password.isEmpty()){
                    mainXml.txtError.setText("Please Enter Email");
                }else if(!email.isEmpty() && password.isEmpty()){
                    mainXml.txtError.setText("Please Enter Password");
                }else{
                    // login user
//                    Sprite threeBounce = new ThreeBounce();
//                    mainXml.spinKit.setIndeterminateDrawable(threeBounce);
                    mainXml.spinKit.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkMailVerification();
                            }else {
//                                Toast.makeText(MainActivity.this, "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                                mainXml.txtError.setText("Account Doesn't Exist");
                                mainXml.spinKit.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                }

            }
        });


    }
    private void checkMailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,notesActivity.class));
            mainXml.spinKit.setVisibility(View.INVISIBLE);

        }else {

            mainXml.txtError.setText("Please verify your Email first");
            firebaseAuth.signOut();
            mainXml.spinKit.setVisibility(View.INVISIBLE);
        }
    }
}