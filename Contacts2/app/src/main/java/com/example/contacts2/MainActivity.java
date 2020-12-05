package com.example.contacts2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddBtn;
    private ContactAdapter mAdapter;
    private ArrayList<Contact> mContactList;
    public DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(null);

        myDb = new DatabaseHelper(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mAddBtn = findViewById(R.id.button_Add);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewContact();
            }
        });

        buildRecyclerView();


    }

    private void buildRecyclerView() {

        mContactList = new ArrayList<>();
        Cursor result = myDb.viewData();

        if (result.getCount() == 0) {
            Toast.makeText(MainActivity.this, "No Contacts Found, Add To Continue", Toast.LENGTH_LONG).show();
            return;
        }


        while (result.moveToNext()) {
            String sex = result.getString(3);
            boolean isMale = Integer.parseInt(sex) == 1;
            Contact contact = new Contact(result.getString(1), result.getString(2), result.getString(4), result.getString(0), isMale);
            mContactList.add(contact);
        }


        mAdapter = new ContactAdapter(this, mContactList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Contact contact = mContactList.get(position);
                deleteContact(position);
                Snackbar snackbar = Snackbar.make(mRecyclerView, "Contact Deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDb.addNewContact(contact);
                        mContactList.add(position, contact);
                        mAdapter.notifyItemInserted(position);
                    }
                }).show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX / 4, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                        .setIconHorizontalMargin(TypedValue.COMPLEX_UNIT_DIP, 25)
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_44dp)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(mRecyclerView);
    }

        public void deleteContact ( int position){
            String id = mContactList.get(position).getId();
            int deletedId = myDb.deleteContact(id);

            if (deletedId > 0) {
                mContactList.remove(position);
                Toast.makeText(MainActivity.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
                mAdapter.notifyItemRemoved(position);
            }

        }

        public void addNewContact () {
            startActivity(new Intent(MainActivity.this, AddNewContact.class));

        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.main_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public void onBackPressed () {
            super.onBackPressed();
            System.exit(0);
        }

    }
