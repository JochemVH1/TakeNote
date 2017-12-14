package com.dev.jvh.takenote.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JochemVanHespen on 9/26/2017.
 * Initializes the database
 */

class DatabaseInitializer extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TakeNoteDB";
    private final String TABLE_SUBJECTS = "subjects";
    private final String TABLE_NOTES = "notes";
    DatabaseInitializer(Context context)
    {
        super(context,DATABASE_NAME,null,5);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // enable foreign keys
        db.execSQL("PRAGMA foreign_keys=ON");
        // create subject table
        db.execSQL("CREATE TABLE " + TABLE_SUBJECTS + " " +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " title TEXT, " +
                "description TEXT, " +
                "colorId INTEGER, dateCreated TEXT, dateUpdated TEXT);");
        // create note table
        db.execSQL("CREATE TABLE " + TABLE_NOTES + " " +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "text TEXT,  " +
                "subject_id INTEGER, " +
                "dateCreated TEXT, " +
                "dateUpdated TEXT, " +
                "notifyMe INTERGER," +
                "notifymeDate TEXT, " +
                " FOREIGN KEY (subject_id) REFERENCES " + TABLE_SUBJECTS + " (_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
}
