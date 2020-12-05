package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewActivity extends AppCompatActivity {

    private TextView mTitle,mDescription,mPriority;
    private String id,title,desc;
    private String priority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        setTitle("Note");

        mTitle = (TextView)findViewById(R.id.mainTitle);
        mDescription = (TextView)findViewById(R.id.mainDescription);
        mPriority = (TextView)findViewById(R.id.mainPriority);


        id = getIntent().getStringExtra("documentId");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("description");
        priority = getIntent().getStringExtra("priority");

        mTitle.setText(title);
        mDescription.setText(desc);
        mPriority.setText(priority);
        /*DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("Notebook").document(id);



        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    Note mNote = documentSnapshot.toObject(Note.class);
                    mTitle.setText(mNote.getTitle());
                    mDescription.setText(mNote.getDescription());
                    mPriority.setText(String.valueOf(mNote.getPriority()));

                }
                else {
                    Toast.makeText(NewActivity.this,"Note Not Found",Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.edit_note:
                editNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void editNote() {
        Intent intent = new Intent(NewActivity.this,EditNoteActivity.class);
        intent.putExtra("documentId",id);
        startActivity(intent);
        finish();
    }
}
