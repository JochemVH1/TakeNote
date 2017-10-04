package com.dev.jvh.takenote.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;

import com.dev.jvh.takenote.domain.Note;

/**
 * Created by JochemVanHespen on 9/28/2017.
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

     public Cursor getNotesWithSubjectId(Context context, int idSubject) {
         return context.getContentResolver().query(
                 Uri.parse("content://com.dev.jvh.takenote.contentprovider/notes"),
                 new String[]{"_id","title","text","subject_id"},
                 "subject_id=?",new String[]{String.valueOf(idSubject)},null
         );
     }
}
