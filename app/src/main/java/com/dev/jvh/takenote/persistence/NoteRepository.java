package com.dev.jvh.takenote.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.dev.jvh.takenote.domain.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/28/2017.
 * Handles communication with the content provider for the note object
 */

public class NoteRepository {
     public void saveNoteToDatabase(Note note, Context context){
         ContentValues values = new ContentValues(7);
         values.put("title",note.getTitle());
         values.put("text",note.getText());
         values.put("subject_id",note.getSubject_id());
         values.put("dateCreated",note.getDateCreated());
         values.put("dateUpdated",note.getDateUpdated());
         values.put("notifyMe",note.isNotifyMe());
         values.put("notifyMeDate", note.getNotificationTime());
         context.getContentResolver().insert(
                 Uri.parse("content://com.dev.jvh.takenote.contentprovider/notes"),
                 values
         );
     }

     public List<Note> getNotesWithSubjectId(Context context, int idSubject) {
         List<Note> temp = new ArrayList<>();
         Cursor cursor = context.getContentResolver().query(
                 Uri.parse("content://com.dev.jvh.takenote.contentprovider/notes"),
                 new String[]{"_id","title","text","subject_id","dateCreated","dateUpdated","notifyMe","notifyMeDate"},
                 "subject_id=?",new String[]{String.valueOf(idSubject)},null
         );
         try
         {
             assert cursor != null;
             if(cursor.moveToFirst()){
                 do{
                     temp.add(new Note(cursor.getInt(0),
                             cursor.getString(1),
                             cursor.getString(2),
                             cursor.getInt(3),
                             cursor.getString(4),
                             cursor.getString(5),
                             cursor.getInt(6) == 1,
                             cursor.getString(7)));
                 }while(cursor.moveToNext());
             }
             cursor.close();
         }catch(NullPointerException npe)
         {
             Log.e("NoteRepositoty", "getNotesWithSubjectId: " + npe.getMessage());
         }

         return temp;
     }

    public void updateInDatabase(Note note, Context context) {
        ContentValues values = new ContentValues(6);
        values.put("title",note.getTitle());
        values.put("text",note.getText());
        values.put("subject_id",note.getSubject_id());
        values.put("dateCreated",note.getDateCreated());
        values.put("dateUpdated",note.getDateUpdated());
        values.put("notifyMe",note.isNotifyMe());
        values.put("notifyMeDate",note.getNotificationTime());
        context.getContentResolver().update(
                Uri.parse("content://com.dev.jvh.takenote.contentprovider/notes/" + String.valueOf(note.get_id())),
                values,null,null
        );
    }

    public void deleteNoteInDatabase(Note note, Context context) {
         context.getContentResolver().delete(
                 Uri.parse("content://com.dev.jvh.takenote.contentprovider/notes"),
                         "_id=?",new String[]{String.valueOf(note.get_id())}
         );
    }
}
