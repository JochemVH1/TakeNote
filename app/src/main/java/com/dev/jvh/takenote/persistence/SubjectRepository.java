package com.dev.jvh.takenote.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dev.jvh.takenote.domain.Note;
import com.dev.jvh.takenote.domain.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/27/2017.
 */

public class SubjectRepository {

    public List<Subject> getSubjectsFromDatabase(SQLiteDatabase database) {
        List<Subject> subjects = new ArrayList<>();
        Cursor cursor = database.query("subjects",new String[]{"_id,title,description"},null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Subject subject = new Subject(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                Cursor noteCursor = database.query(
                        "notes", new String[]{"title","text"},
                        "subject_id=?", new String[]{String.valueOf(cursor.getInt(0))}
                        ,null,null,null);
                if(noteCursor.moveToFirst()){
                    do{
                        subject.addNote(new Note(noteCursor.getString(0),noteCursor.getString(1)));
                    }while(noteCursor.moveToNext());
                }
                subjects.add(subject);
            }while(cursor.moveToNext());
        }
        return subjects;
    }

    public void saveSubjectToDatabase(Subject subject, SQLiteDatabase database) {
        ContentValues values = new ContentValues(1);
        values.put("title",subject.getTitle());
        database.insert("subjects","",values);

    }

    public void deleteSubjectFromDatabase(Subject subject, SQLiteDatabase database){
        Cursor cursor = database.query(
                "subjects", new String[]{"_id, title"},
                "title=?",new String[]{subject.getTitle()},
                null,null,null);
        // TODO what happens when 2 subjects have the same title?
        if(cursor.moveToFirst())
        {
            do{
                int temp = cursor.getInt(0);
                database.delete("subjects","_id=?",new String[]{String.valueOf(temp)});
            }while(cursor.moveToNext());
        }

    }

}
