package com.dev.jvh.takenote.domain;

import com.dev.jvh.takenote.persistence.SubjectRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/27/2017.
 */

public class Domaincontroller {

    private DatabaseInitializer db;

    private List<Subject> subjects;

    public Domaincontroller (DatabaseInitializer db)
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
}
