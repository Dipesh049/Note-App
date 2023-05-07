package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityEditNoteBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EditNote extends AppCompatActivity {

    ActivityEditNoteBinding EditNoteXml;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EditNoteXml = ActivityEditNoteBinding.inflate(getLayoutInflater());
        setContentView(EditNoteXml.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent data = getIntent();
        EditNoteXml.txtTitle.setText(data.getStringExtra("title"));
        EditNoteXml.txtDescription.setText(data.getStringExtra("desc"));

        EditNoteXml.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = EditNoteXml.txtTitle.getText().toString();
                String newDesc = EditNoteXml.txtDescription.getText().toString();

                if(newDesc.isEmpty()){
                    EditNoteXml.txtError.setText("Description is Empty! ");
                }else {
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    HashMap<String,Object> note = new HashMap<>();
                    note.put("title",newTitle);
                    note.put("desc",newDesc);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditNote.this, "Note is Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditNote.this,notesActivity.class));
                            finishAffinity();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditNote.this, "Failed to update", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

        EditNoteXml.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditNote.this, "Note is deleted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditNote.this,notesActivity.class));
                        finishAffinity();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditNote.this, "Failed to Delete Note!", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


        
    }
}