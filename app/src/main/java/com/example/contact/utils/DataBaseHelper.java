package com.example.contact.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.contact.DataModel.Contact;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DataBaseHelper";

    private static final String DATABASE_NAME = "contacts.db";
    private static final String TABLE_NAME = "contacts_table";
    private static final String COL0 = "ID";
    private static final String COL1= "NAME";
    private static final String COL2 = "PHONE_NUMBER";
    private static final String COL3 = "DEVICE";
    private static final String COL4 = "EMAIL";
    private static final String COL5 = "PROFILE_PHOTO";



    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " TEXT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    /**
     * Insert a contact into database
     * @param contact
     * @return true if data successfully added to database, false otherwise
     */
    public boolean addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, contact.getName());
        contentValues.put(COL2, contact.getNumber());
        contentValues.put(COL3, contact.getDevice());
        contentValues.put(COL4, contact.getMail());
        contentValues.put(COL5, contact.getImageUrl());
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result == - 1 ? false : true;
    }

    /**
     * Retrieve all contacts from database
     * @return
     */
    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    /**
     * update the contact where id = @param 'id'
     * Replace the current contact with @param contact
     * @param contact
     * @param id
     * @return
     */
    public boolean updateContact(Contact contact, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, contact.getName());
        contentValues.put(COL2, contact.getNumber());
        contentValues.put(COL3, contact.getDevice());
        contentValues.put(COL4, contact.getMail());
        contentValues.put(COL5, contact.getImageUrl());
        int result = db.update(TABLE_NAME, contentValues, COL0 + " = ?", new String[]{String.valueOf(id)});
        if (result == 1) {
            return  true;
        } else {
            return false;
        }
    }

    /**
     * Retrieve contact id using @param contact
     * @param contact
     * @return
     */
    public Cursor getContactId(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + contact.getName()
                + "' AND " + COL2 + " = '" + contact.getNumber() + "'";

        return db.rawQuery(sql, null);
    }
}
