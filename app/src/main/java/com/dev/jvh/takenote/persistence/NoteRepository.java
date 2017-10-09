package com.dev.jvh.takenote.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;

import com.dev.jvh.takenote.domain.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/28/2017.
 * Handles communication with the content provider for the note object
 */

public class NoteRepository {
     public void saveNoteToDatabase(Note note, Context context){
         ContentValues values = new ContentValues(3);
         values.put("title",note.getTitle());
         values.put("text",note.getText());
         values.put("subject_id",note.getSubject_id());
         context.getContentResolver().insert(
                 Uri.parse("content://com.dev.jvh.takenote.contentprovider/notes"),
                 values
         );
     }

     public List<Note> getNotesWithSubjectId(Context context, int idSubject) {
         List<Note> temp = new ArrayList<>();
         Cursor cursor = context.getContentResolver().query(
                 Uri.parse("content://com.dev.jvh.takenote.contentprovider/notes"),
                 new String[]{"_id","title","text","subject_id"},
                 "subject_id=?",new String[]{String.valueOf(idSubject)},null
         );
         if(cursor.moveToFirst()){
             do{
                 temp.add(new Note(cursor.getInt(0),
                         cursor.getString(1),
                         cursor.getString(2),
                         cursor.getInt(3)));
             }while(cursor.moveToNext());
         }
         cursor.close();
         return temp;
     }

    public void updateInDatabase(Note note, Context context) {
        ContentValues values = new ContentValues(3);
        values.put("title",note.getTitle());
        values.put("text",note.getText());
        values.put("subject_id",note.getSubject_id());
        context.getContentResolver().update(
                Uri.parse("content://com.dev.jvh.takenote.contentprovider/notes/" + String.valueOf(note.get_id())),
                values,null,null
        );
    }
}
