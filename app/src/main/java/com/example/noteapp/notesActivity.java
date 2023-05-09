package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityNotesBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Random;

public class notesActivity extends AppCompatActivity {

    ActivityNotesBinding NotesXml;
    FirebaseAuth firebaseAuth;

    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebaseModel,NoteViewHolder> noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotesXml = ActivityNotesBinding.inflate(getLayoutInflater());
        setContentView(NotesXml.getRoot());

        SharedPreferences sh = getSharedPreferences("MySharedPref",MODE_PRIVATE);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(getApplicationContext());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        NotesXml.floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(notesActivity.this, CreateNote.class));
            }
        });

        NotesXml.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sh.edit().clear().apply();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(notesActivity.this,MainActivity.class));

            }
        });

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebaseModel> allUserNotes = new FirestoreRecyclerOptions.Builder<firebaseModel>().setQuery(query,firebaseModel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebaseModel, NoteViewHolder>(allUserNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebaseModel model) {

                //progress bar
                NotesXml.spinKit.setVisibility(View.VISIBLE);

                int colorCode = getRandomColor();
               holder.note.setBackgroundColor(holder.itemView.getResources().getColor(colorCode,null));


                holder.title.setText(model.getTitle());
                 holder.desc.setText(model.getDesc());
                 String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();

                NotesXml.spinKit.setVisibility(View.INVISIBLE);
                 holder.itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
//                         Toast.makeText(notesActivity.this, "clicked note", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(view.getContext(),EditNote.class);
                         intent.putExtra("title",model.getTitle());
                         intent.putExtra("desc",model.getDesc());
                         intent.putExtra("noteId",docId);
                         view.getContext().startActivity(intent);


                     }
                 });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };

        NotesXml.recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        NotesXml.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        NotesXml.recyclerView.setAdapter(noteAdapter);


    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView desc;
        LinearLayout note;

        public NoteViewHolder( View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            note = itemView.findViewById(R.id.note);


        }
    }

    public int getRandomColor(){
        int[] color = new int[]{R.color.color1,R.color.color2,R.color.color3,R.color.color4,R.color.color4,
                R.color.color5,R.color.color6,R.color.color7,R.color.color8};
        Random random = new Random();
        int code = random.nextInt(color.length);

        return color[code];

    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.startListening();
        }

    }

}