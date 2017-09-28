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
    public DatabaseInitializer(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_SUBJECTS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_SUBJECTS);
        onCreate(db);
    }
}
