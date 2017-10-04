package com.dev.jvh.takenote.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;
import com.dev.jvh.takenote.domain.Subject;

/**
 * Created by JochemVanHespen on 9/28/2017.
 * UI element which represents all the notes a user makes for a certain Subject
 */

public class NoteActivity extends BaseActivity
{
    private DomainController controller;
    private Subject subject;
    private int idSubject;
    private final String TAG = "NOTE_ACTIVITY";
    private final int REQUEST_CODE = 66;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.note_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Bundle bundle = getIntent().getExtras();
        controller = bundle.getParcelable("controller");
        idSubject = bundle.getInt("idSubject");
        subject = controller.getSubjectById(idSubject, this);
        super.setTitle(subject.getTitle());
        buildNoteListView();
    }

    @Override
    public void setAdapter() {
        adapter = new NoteCursorAdapter(
                this,
                controller.getNotesWithSubjectId(this,idSubject),
                0,
                controller);
    }

    private void buildNoteListView() {
        ListView noteListView = (ListView) findViewById(R.id.notesListView);
        setAdapter();
        noteListView.setAdapter(adapter);
    }

    public void createNote(View view)
    {
        Intent intent = new Intent(this,CreateNoteActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Note note = new Note(
                        data.getStringExtra("noteTitle"),
                        data.getStringExtra("noteContent"),
                        idSubject);
                controller.addNoteToSubject(note,this);
                buildNoteListView();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                Uri.parse("content://com.dev.jvh.takenote.contentprovider/notes"),
                new String[]{"_id","title","text","subject_id"},null,null,null);
        return loader;
    }
}
