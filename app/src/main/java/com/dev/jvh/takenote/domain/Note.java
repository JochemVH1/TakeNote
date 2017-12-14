package com.dev.jvh.takenote.domain;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.dev.jvh.takenote.persistence.NoteRepository;

import java.util.Date;

/**
 * Created by JochemVanHespen on 9/27/2017.
 * Note class represent one note a user makes
 */

public class Note{

    private int _id;
    private String title;
    private String content;
    private int subject_id;
    private String dateCreated;
    private String dateUpdated;
    private boolean notifyMe;
    private String notificationTime;

    private Note(String title, String content)
    {
        this.title = title;
        this.content = content;
    }
    public Note(String title, String content, int subject_id, String dateCreated, String dateUpdated, boolean notifyMe, String notificationTime)
    {
        this(title,content);
        this.subject_id = subject_id;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.notifyMe = notifyMe;
        this.notificationTime = notificationTime;
    }
    public Note(int _id ,String title, String content, int subject_id, String dateCreated, String dateUpdated, boolean notifyMe, String notificationTime)
    {
        this(title,content,subject_id,dateCreated,dateUpdated, notifyMe, notificationTime);
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

    public void setNotifyMe(boolean notifyMe) {
        this.notifyMe = notifyMe;
    }

    public boolean isNotifyMe() {
        return notifyMe;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
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
