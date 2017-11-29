package com.dev.jvh.takenote.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;
import com.dev.jvh.takenote.util.FragmentType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JochemVanHespen on 11/29/2017.
 * NoteActivity is used in combination with the FragmentTypeEnumeration
 * The FragmentTypeEnumeration determines which fragment is loaded inside the note_view layout file
 * of the NoteActivityClass
 */

public class NoteActivity extends BaseActivity
    implements NoteCreateFragment.NoteListener
{
    private DomainController controller;
    private int currentSubjectId;
    private FragmentManager fragmentManager;
    protected final String NOTE_FRAGMENT_TAG = "Note_fragment";
    protected final String NOTE_CREATE_FRAGMENT_TAG = "Note_create_fragment";
    protected final String SUBJECT_DETAIL_FRAGMENT_TAG = "Subject_detail_fragment";
    private Note currentNote;
    private FragmentType type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.note_view);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Bundle bundle= getIntent().getExtras();
        assert bundle != null;
        controller = bundle.getParcelable("controller");
        currentSubjectId = bundle.getInt("currentSubjectId");
        type = (FragmentType) bundle.get("type");
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if(type == FragmentType.NOTEFRAGMENT)
        {
            NoteFragment noteFragment = new NoteFragment();
            ft.add(R.id.note_layout,noteFragment,NOTE_FRAGMENT_TAG);
            ft.addToBackStack(NOTE_FRAGMENT_TAG);
        }
        if(type == FragmentType.SUBJECTDETAILFRAGMENT)
        {
            SubjectDetailFragment subjectDetailFragment = new SubjectDetailFragment();
            ft.add(R.id.note_layout,subjectDetailFragment,SUBJECT_DETAIL_FRAGMENT_TAG);
            ft.addToBackStack(SUBJECT_DETAIL_FRAGMENT_TAG);
        }
        ft.commit();
    }

    public DomainController getController() {
        return controller;
    }

    public int getCurrentSubjectId() {
        return currentSubjectId;
    }

    public void setCurrentNote(Note currentNote) {
        this.currentNote = currentNote;
    }

    public Note getCurrentNote() {
        return currentNote;
    }

    @Override
    public void createNote(String noteTitle, String noteContent) {
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        String currentDateTime = df.format(new Date());
        Note note = new Note(
                noteTitle,
                noteContent,
                currentSubjectId,
                currentDateTime,
                currentDateTime);
        controller.addNoteToSubject(note,this);
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
}
