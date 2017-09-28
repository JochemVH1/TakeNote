package com.dev.jvh.takenote.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by JochemVanHespen on 9/26/2017.
 * Initializes the database
 */

public class DatabaseInitializer extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TakeNoteDB";
    private final String TABLE_SUBJECTS = "subjects";
    private final String TABLE_NOTES = "notes";
    public DatabaseInitializer(Context context)
    {
        super(context,DATABASE_NAME,null,2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // enable foreign keys
        db.execSQL("PRAGMA foreign_keys=ON");
        // create subject table
        db.execSQL("CREATE TABLE " + TABLE_SUBJECTS + " " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT);");
        // create note table
        db.execSQL("CREATE TABLE " + TABLE_NOTES + " " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, text TEXT, subject_id INTEGER," +
                " FOREIGN KEY (subject_id) REFERENCES " + TABLE_SUBJECTS + " (_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NOTES);
        onCreate(db);
    }
}