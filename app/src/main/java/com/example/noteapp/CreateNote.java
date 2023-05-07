package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.noteapp.databinding.ActivityCreateNoteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    ActivityCreateNoteBinding CreateNoteXml;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CreateNoteXml = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(CreateNoteXml.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getApplicationContext());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        CreateNoteXml.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = CreateNoteXml.txtTitle.getText().toString();
                String desc = CreateNoteXml.txtDescription.getText().toString();
                if(desc.isEmpty()){
                    CreateNoteXml.txtError.setText("Description is Empty !");
                }else {
                    CreateNoteXml.spinKit.setVisibility(View.VISIBLE);

                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("desc",desc);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            CreateNoteXml.spinKit.setVisibility(View.INVISIBLE);
                            Toast.makeText(CreateNote.this, "Note Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateNote.this,notesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CreateNoteXml.spinKit.setVisibility(View.INVISIBLE);
                            Toast.makeText(CreateNote.this, "Failed to Create Note", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });




    }
}