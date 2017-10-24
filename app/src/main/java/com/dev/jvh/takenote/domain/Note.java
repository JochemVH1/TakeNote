package com.dev.jvh.takenote.domain;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.dev.jvh.takenote.persistence.NoteRepository;

/**
 * Created by JochemVanHespen on 9/27/2017.
 * Note class represent one note a user makes
 */

public class Note implements Parcelable{

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

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
    }

    public final static Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>(){

        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private Note(Parcel in)
    {
        title = in.readString();
        content = in.readString();
    }

    void saveToDatabase(Context context) {
        new NoteRepository().saveNoteToDatabase(this, context);
    }

    void updateInDatabase(Context context) {
        new NoteRepository().updateInDatabase(this, context);
    }
}
