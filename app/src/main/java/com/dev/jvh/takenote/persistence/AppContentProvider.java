package com.dev.jvh.takenote.persistence;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import junit.framework.Assert;

/**
 * Created by JochemVanHespen on 10/2/2017.
 * Content provider for the application accessible trough com.dev.jvh.takenote.contentprovicer
 */

public class AppContentProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String AUTHORITY = "com.dev.jvh.takenote.contentprovider";
    private static final int TABLE_SUBJECTS = 1;
    private static final int TABLE_SUBJECTS_ID = 2;
    private static final int TABLE_NOTES = 3;
    private static final int TABLE_NOTES_ID = 4;
    public static final String TAG = "AppContentProvider";

    static {
        uriMatcher.addURI(AUTHORITY,"subjects",TABLE_SUBJECTS);
        uriMatcher.addURI(AUTHORITY,"subjects" + "/#",TABLE_SUBJECTS_ID);
        uriMatcher.addURI(AUTHORITY,"notes",TABLE_NOTES);
        uriMatcher.addURI(AUTHORITY,"notes" + "/#",TABLE_NOTES_ID);
    }

    private DatabaseInitializer database;

    @Override
    public boolean onCreate() {
        database = new DatabaseInitializer(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch(uriMatcher.match(uri)){
            case TABLE_SUBJECTS:
                queryBuilder.setTables("subjects");
                break;
            case TABLE_SUBJECTS_ID:
                queryBuilder.setTables("subjects");
                queryBuilder.appendWhere("_id =" + uri.getLastPathSegment());
                break;
            case TABLE_NOTES:
                queryBuilder.setTables("notes");
                break;
            case TABLE_NOTES_ID:
                queryBuilder.setTables("notes");
                queryBuilder.appendWhere("_id =" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
        Cursor cursor = queryBuilder.query(database.getWritableDatabase(),projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase sqlDb = database.getWritableDatabase();
        long id = 0;
        String tableName = "";


        switch (uriMatcher.match(uri))
        {
            case TABLE_SUBJECTS:
                tableName = "subjects";
                id = sqlDb.insert(tableName,"",values);
                break;
            case TABLE_NOTES:
                tableName = "notes";

                id = sqlDb.insert(tableName,"",values);
                if(values.get("subject_id") != null)
                {
                    getContext().getContentResolver().notifyChange(Uri.parse(uri + "/" + values.get("subject_id")) ,null);
                    return Uri.parse(tableName + "/" + id);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(tableName + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqlDb = database.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri))
        {
            case TABLE_SUBJECTS:
                count = sqlDb.delete("subjects",selection,selectionArgs);
                break;
            case TABLE_SUBJECTS_ID:
                count = sqlDb.delete("subjects",
                        "_id=" + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection)? "AND (" + selection + ")" : ""),selectionArgs);
                break;
            case TABLE_NOTES:
                count = sqlDb.delete("notes",selection,selectionArgs);
                break;
            case TABLE_NOTES_ID:
                count = sqlDb.delete("notes",
                        "_id=" + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection)? "AND (" + selection + ")" : ""),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqlDb = database.getWritableDatabase();
        int count = 0;
        switch(uriMatcher.match(uri))
        {
            case TABLE_SUBJECTS:
                count = sqlDb.update("subjects",values,selection,selectionArgs);
                break;
            case TABLE_SUBJECTS_ID:
                count = sqlDb.update("subjects",values,
                        "_id=" + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection)? " AND (" + selection + ")" : "" ),selectionArgs);
                break;
            case TABLE_NOTES:
                count = sqlDb.update("notes",values,selection,selectionArgs);
                break;
            case TABLE_NOTES_ID:
                count = sqlDb.update("notes ",values,
                        "_id=" + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection)? " AND (" + selection + ")" : "" ),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
