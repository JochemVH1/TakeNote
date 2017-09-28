package com.dev.jvh.takenote.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/19/2017.
 * Subject is a container for different notes
 */

public class Subject implements Parcelable {

    private String title;
    private String description;
    private List<Note> notes;

    public Subject (String title){
        this.title = title;
        notes = new ArrayList<>();
    }
    public Subject (String title, String description)
    {
        this(title);
        this.description = description;
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
        notes = new ArrayList<>();
        in.readTypedList(notes,Note.CREATOR);
    }
}
