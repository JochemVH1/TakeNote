package com.dev.jvh.takenote.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.dev.jvh.takenote.domain.Subject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/27/2017.
 * Handles communication with the content provider for the subject object
 */

public class SubjectRepository {

    public List<Subject> getSubjectsFromDatabase(Context context) {
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.dev.jvh.takenote.contentprovider/subjects"),
                new String[]{"_id","title","description"},null,null,null);
        List<Subject> temp = new ArrayList<>();
        try
        {
            if(cursor.moveToFirst())
            {
                do{
                    temp.add(new Subject(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
                }while(cursor.moveToNext());}
            cursor.close();
        } catch (NullPointerException npe)
        {

        }
        return temp;

    }

    public void saveSubjectToDatabase(Subject subject, Context context) {
        ContentValues values = new ContentValues(1);
        values.put("title",subject.getTitle());
        context.getContentResolver().insert(
                Uri.parse("content://com.dev.jvh.takenote.contentprovider/subjects"),values);

    }

    public void deleteSubjectFromDatabase(Context context, int subjectId){
        context.getContentResolver().delete(
                Uri.parse("content://com.dev.jvh.takenote.contentprovider/subjects"),
                        "_id=?", new String[]{String.valueOf(subjectId)});
        context.getContentResolver().delete(
                Uri.parse("content://com.dev.jvh.takenote.contentprovider/notes"),
                "subject_id=?",new String[]{String.valueOf(subjectId)}
        );
    }

    public Subject getSubjectById(int idSubject, Context context) {
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.dev.jvh.takenote.contentprovider/subjects/" + String.valueOf(idSubject)),
                        new String[]{"_id","title","description"},null,null,null,null);
        Subject subject = new Subject();
        try{
            if(cursor.moveToFirst())
            {
                do{
                    subject = new Subject(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                }while(cursor.moveToNext());}
            cursor.close();
        } catch(NullPointerException npe){
            Log.e("SubjectRepository", "getSubjectById: " + npe.getMessage());
        }

        return subject;
    }

    public void updateSubject(Subject subject, Context context) {
        ContentValues values = new ContentValues(2);
        values.put("title",subject.getTitle());
        values.put("description",subject.getDescription());
        context.getContentResolver().update(
                Uri.parse("content://com.dev.jvh.takenote.contentprovider/subjects/" + String.valueOf(subject.get_id())),
                values,null,null
        );
    }
}
