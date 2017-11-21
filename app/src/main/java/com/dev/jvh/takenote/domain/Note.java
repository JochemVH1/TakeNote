package com.dev.jvh.takenote.domain;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.dev.jvh.takenote.persistence.NoteRepository;

/**
 * Created by JochemVanHespen on 9/27/2017.
 * Note class represent one note a user makes
 */

public class Note{

    private int _id;
    private String title;
    private String content;
    private int subject_id;

    private Note(String title, String content)
    {
        this.title = title;
        this.content = content;
    }
    public Note(String title, String content, int subject_id)
    {
        this(title,content);
        this.subject_id = subject_id;
    }
    public Note(int _id ,String title, String content, int subject_id)
    {
        this(title,content,subject_id);
        this._id = _id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return content;
    }

    public void setText(String text) {
        this.content = text;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public int get_id() {
        return _id;
    }


    void saveToDatabase(Context context) {
        new NoteRepository().saveNoteToDatabase(this, context);
    }

    void updateInDatabase(Context context) {
        new NoteRepository().updateInDatabase(this, context);
    }

    void delete(Context context) {
        new NoteRepository().deleteNoteInDatabase(this,context);
    }
}
