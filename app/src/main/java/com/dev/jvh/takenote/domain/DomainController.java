package com.dev.jvh.takenote.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.dev.jvh.takenote.persistence.SubjectRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/27/2017.
 * Facade pattern, handles all the actions that come from the UI
 */

public class DomainController implements Parcelable {

    private DatabaseInitializer db;

    private List<Subject> subjects;

    public DomainController(DatabaseInitializer db)
    {
        this.db = db;
        subjects = new ArrayList<>();
    }

    public List<Subject> getSubjectsFromDatabase()
    {
        subjects = new SubjectRepository().getSubjectsFromDatabase(db.getWritableDatabase());
        return subjects;
    }

    public void saveSubjectToDatabase(Subject subject)
    {
        new SubjectRepository().saveSubjectToDatabase(subject, db.getWritableDatabase());
    }

    public  void deleteSubjectFromDatabase(Subject subject)
    {
        new SubjectRepository().deleteSubjectFromDatabase(subject, db.getWritableDatabase());
    }

    public void setDb(DatabaseInitializer db) {
        this.db = db;
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
}
