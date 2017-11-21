package com.dev.jvh.takenote.domain;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.dev.jvh.takenote.persistence.NoteRepository;
import com.dev.jvh.takenote.persistence.SubjectRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/27/2017.
 * Facade pattern, handles all the actions that come from the UI
 */

public class DomainController {

    public List<Subject> getSubjectsFromDatabase(Context context)
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

    public Subject getSubjectById(int idSubject, Context context) {
        return new SubjectRepository().getSubjectById(idSubject, context);
    }

    public List<Note> getNotesWithSubjectId(Context context, int idSubject) {
        return new NoteRepository().getNotesWithSubjectId(context,idSubject);
    }

    public void addNoteToSubject(Note note, Context context) {
        note.saveToDatabase(context);
    }

    public void updateNote(Note note, Context context) { note.updateInDatabase(context);
    }

    public void updateSubject(Subject subject, Context context) {
        subject.update(context);
    }

    public void deleteNote(Note currentNote,Context context) {
        currentNote.delete(context);
    }
}
