package com.example.contacts2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contacts.db";
    public static final String TABLE_NAME = "contacts_table";
    public static final String COLUMN_1 = "ID";
    public static final String COLUMN_2 = "Name";
    public static final String COLUMN_3 = "Number";
    public static final String COLUMN_4 = "Sex";
    public static final String COLUMN_5 = "ImageUrl";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_2 + " TEXT," + COLUMN_3 + " TEXT," + COLUMN_4 + " BOOLEAN," + COLUMN_5 + " TEXT)" );
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_2,"Saurav");
        contentValues.put(COLUMN_3,"121");
        contentValues.put(COLUMN_4,false);
        contentValues.put(COLUMN_5,"null");
        db.insert(TABLE_NAME,null,contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean addNewContact(Contact contact){
        String name = contact.getName();
        String number = contact.getNumber();
        String imageUrl = contact.getImageUrl();
        boolean sex = contact.isMale();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_2,name);
        contentValues.put(COLUMN_3,number);
        contentValues.put(COLUMN_4,sex);
        contentValues.put(COLUMN_5,imageUrl);

        long result = db.insert(TABLE_NAME,null,contentValues);

        return result != -1;
    }

    public Cursor viewData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.query(TABLE_NAME,new String[]{COLUMN_1,COLUMN_2,COLUMN_3,COLUMN_4,COLUMN_5},null,null,null,null,COLUMN_2 + " ASC");
        return result;
    }

    public boolean updateData(String name,String number,String imageUrl,boolean sex,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1,id);
        contentValues.put(COLUMN_2,name);
        contentValues.put(COLUMN_3,number);
        contentValues.put(COLUMN_4,sex);
        contentValues.put(COLUMN_5,imageUrl);

        int result = db.update(TABLE_NAME,contentValues,"ID=?" , new String[]{id});
        if(result == 0){
            return false;
        }
        return true;
    }

    public int deleteContact(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID=?",new String[]{id});
    }

    public void deleteList(ArrayList<Contact> list){
        for(Contact contact : list){
            deleteContact(contact.getId());
        }
    }


}
