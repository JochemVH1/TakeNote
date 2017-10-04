package com.dev.jvh.takenote.domain;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dev.jvh.takenote.persistence.NoteRepository;
import com.dev.jvh.takenote.persistence.SubjectRepository;
import com.dev.jvh.takenote.ui.NoteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/27/2017.
 * Facade pattern, handles all the actions that come from the UI
 */

public class DomainController implements Parcelable {


    private List<Subject> subjects;

    public DomainController()
    {
        subjects = new ArrayList<>();
    }

    public Cursor getSubjectsFromDatabase(Context context)
    {
        return new SubjectRepository().getSubjectsFromDatabase(context);
    }

    public void saveSubjectToDatabase(Subject subject, Context context)
    {
        subject.saveToDatabase(context);
    }

    public  void deleteSubjectFromDatabase(Context context, int subjectId)
    {
        new SubjectRepository().deleteSubjectFromDatabase(context, subjectId);
    }


    public List<Subject> getSubjects() {
        return subjects;
    }

    public Subject getSubjectFromList(int index)
    {
        return subjects.get(index);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(subjects);
    }

    public static final Parcelable.Creator<DomainController> CREATOR = new Parcelable.Creator<DomainController>(){

        @Override
        public DomainController createFromParcel(Parcel source) {
            return new DomainController(source);
        }

        @Override
        public DomainController[] newArray(int size) {
            return new DomainController[size];
        }
    };

    private DomainController(Parcel in)
    {
        subjects = new ArrayList<>();
        in.readTypedList(subjects,Subject.CREATOR);
    }

    public Subject getSubjectById(int idSubject, Context context) {
        return new SubjectRepository().getSubjectById(idSubject, context);
    }

    public Cursor getNotesWithSubjectId(Context context, int idSubject) {
        return new NoteRepository().getNotesWithSubjectId(context,idSubject);
    }

    public void addNoteToSubject(Note note, Context context) {
        note.saveToDatabase(context);
    }
}
