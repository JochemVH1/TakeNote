package com.dev.jvh.takenote.domain;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.dev.jvh.takenote.persistence.SubjectRepository;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/19/2017.
 * Subject is a container for different notes
 */

public class Subject {

    private int _id;
    private String title;
    private String description;
    private int colorId;
    private List<Note> notes;
    private String dateCreated;
    private String dateUpdated;

    public Subject(){
        notes = new ArrayList<>();
    }

    public Subject (String title, int colorId, String dateCreated, String dateUpdated)
    {
        this();
        this.title = title;
        this.colorId = colorId;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;

    }
    public Subject (int _id, String title, String description, int colorId, String dateCreated, String dateUpdated)
    {
        this(title,colorId,dateCreated,dateUpdated);
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Note> getNotes() { return notes; }

    public int getColorId() {
        return colorId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    void saveToDatabase(Context context) {
        new SubjectRepository().saveSubjectToDatabase(this, context);
    }

    void update(Context context) {
        new SubjectRepository().updateSubject(this,context);
    }

    public long wordCount()
    {
        //ENABLE JACK COMPILER FOR USING LAMBDAS
        //ADD LANGUAGE SUPPORT FOR JAVA 8
/*        return notes
                .stream()
                .map(note -> note.getText().split(" "))
                .count();*/
        return 0;
    }
    public int letterCount()
    {
/*        return notes.stream()
                .map(note -> note.getText().length())
                .reduce((total, next) -> total = total + next)
                .get();*/
        return 0;
    }
}
