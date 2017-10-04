package com.dev.jvh.takenote.domain;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.dev.jvh.takenote.persistence.SubjectRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/19/2017.
 * Subject is a container for different notes
 */

public class Subject implements Parcelable {

    private int _id;
    private String title;
    private String description;
    private List<Note> notes;

    public Subject(){notes = new ArrayList<>();}
    public Subject (String title){
        this();
        this.title = title;
    }
    public Subject (int _id, String title, String description)
    {
        this(title);
        this._id = _id;
        this.description = description;
    }

    public int get_id() {
        return _id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription() { return description; }

    public List<Note> getNotes() { return notes; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(_id);
        dest.writeTypedList(notes);
    }

    public final static Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>(){

        @Override
        public Subject createFromParcel(Parcel source) {
            return new Subject(source);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    private Subject(Parcel in)
    {
        title = in.readString();
        description = in.readString();
        _id = in.readInt();
        notes = new ArrayList<>();
        in.readTypedList(notes,Note.CREATOR);
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public void saveToDatabase(Context context) {
        new SubjectRepository().saveSubjectToDatabase(this, context);
    }
}
