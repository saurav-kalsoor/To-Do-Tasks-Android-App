package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewNoteActivity extends AppCompatActivity {

    private EditText mTitle,mDescription;
    private NumberPicker mNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        setTitle("Add Note");

        mTitle = (EditText)findViewById(R.id.inputTitle);
        mDescription = (EditText)findViewById(R.id.inputDescription);
        mNumberPicker = (NumberPicker)findViewById(R.id.numberPicker);

        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(10);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote() {
        String title,desc;
        int priority;
        title = mTitle.getText().toString();
        desc = mDescription.getText().toString();
        priority = mNumberPicker.getValue();

        if(TextUtils.isEmpty(title.trim()) || TextUtils.isEmpty(desc.trim())){
            Toast.makeText(NewNoteActivity.this,"Fields Are Empty",Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference mCollectionRef = FirebaseFirestore.getInstance().collection("Notebook");
        Note newNote = new Note(title,desc,priority);
        mCollectionRef.add(newNote);
        Toast.makeText(NewNoteActivity.this,"Note Added Successfully",Toast.LENGTH_SHORT).show();
        finish();
    }
}
