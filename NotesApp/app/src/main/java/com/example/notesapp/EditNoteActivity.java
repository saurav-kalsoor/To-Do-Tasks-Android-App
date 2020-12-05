package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditNoteActivity extends AppCompatActivity {

    private EditText mTitle,mDescription;
    private NumberPicker mNumberPicker;
    private String id;
    private DocumentReference mDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        mTitle = (EditText)findViewById(R.id.inputTitle);
        mDescription = (EditText)findViewById(R.id.inputDescription);
        mNumberPicker = (NumberPicker)findViewById(R.id.numberPicker);

        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(10);

        id = getIntent().getStringExtra("documentId");
        mDocRef = FirebaseFirestore.getInstance().collection("Notebook").document(id);

        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Note mNote = documentSnapshot.toObject(Note.class);
                    mTitle.setText(mNote.getTitle());
                    mDescription.setText(mNote.getDescription());
                    mNumberPicker.setValue(mNote.getPriority());
                }
            }
        });
    }

    private void editNote() {
        String title = mTitle.getText().toString();
        String desc = mDescription.getText().toString();
        int priority = mNumberPicker.getValue();
        mDocRef.update("title",title);
        mDocRef.update("description",desc);
        mDocRef.update("priority",priority);
        Toast.makeText(EditNoteActivity.this,"Note Edit Successfully",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_note){
            editNote();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }
}
