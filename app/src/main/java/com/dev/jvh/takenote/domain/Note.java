package com.dev.jvh.takenote.domain;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.dev.jvh.takenote.persistence.NoteRepository;

/**
 * Created by JochemVanHespen on 9/27/2017.
 * Note class represent one note a user makes
 */

public class Note implements Parcelable{

    private String title;
    private String content;

    public Note(String title, String content)
    {
        this.title = title;
        this.content = content;
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

    public void saveToDatabase(int subject_id, SQLiteDatabase database) {
        new NoteRepository().saveNoteToDatabase(this, subject_id, database);
    }
}
