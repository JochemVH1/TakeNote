package com.dev.jvh.takenote.ui;

import android.app.ActivityOptions;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity
    implements
        CreateSubjectDialog.CreateSubjectListener,
        NoteCreateFragment.NoteListener

{
    private FirebaseAuth mAuth;
    private DomainController controller;
    private FirebaseUser user;
    private FragmentManager fragmentManager;
    protected final String SUBJECT_FRAGMENT_TAG = "Subject_fragment";
    protected final String HEADER_FRAGMENT_TAG = "Header_fragment";
    protected final String NOTE_FRAGMENT_TAG = "Note_fragment";
    protected final String SUBJECT_DETAIL_FRAGMENT_TAG = "Subject_detail_fragment";
    protected final String NOTE_CREATE_FRAGMENT_TAG = "Note_create_fragment";
    private int currentSubjectId;
    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null)
        {
            startLoginActivity();
        }
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.subject_view);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        controller = new DomainController();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        HeaderFragment headerFragment = new HeaderFragment();
        SubjectFragment subjectFragment = new SubjectFragment();
        ft.add(R.id.main_layout,headerFragment,HEADER_FRAGMENT_TAG);
        ft.add(R.id.main_layout,subjectFragment,SUBJECT_FRAGMENT_TAG);
        ft.addToBackStack("subjects");
        ft.commit();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    protected DomainController getController()
    {
        return controller;
    }

    /**
     * Method called to when the OK button is pressed in the CreateSubjectDialog
     * @param fragment which triggers the method
     * @param title typed int the CreateSubjectDialog
     */
    @Override
    public void createSubjectPositive(DialogFragment fragment, String title) {
        SubjectFragment subjectFragment = getSubjectFragment();
        if(subjectFragment != null)
            subjectFragment.createSubject(title);
    }

    private SubjectFragment getSubjectFragment(){
        SubjectFragment subjectFragment =
                (SubjectFragment) fragmentManager.findFragmentByTag(SUBJECT_FRAGMENT_TAG);
        if(subjectFragment != null && subjectFragment.isVisible())
            return subjectFragment;
        return null;
    }


    public void setCurrentSubjectId(int currentSubjectId) {
        this.currentSubjectId = currentSubjectId;
    }

    public int getCurrentSubjectId() {
        return currentSubjectId;
    }

    @Override
    public void createNote(String noteTitle, String noteContent) {
        Note note = new Note(
                noteTitle,
                noteContent,
                currentSubjectId);
        controller.addNoteToSubject(note,this);
    }

    public void setCurrentNote(Note currentNote) {
        this.currentNote = currentNote;
    }
    public Note getCurrentNote() {
        return currentNote;
    }


}


