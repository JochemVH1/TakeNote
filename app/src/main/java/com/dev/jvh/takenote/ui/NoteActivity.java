package com.dev.jvh.takenote.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;
import com.dev.jvh.takenote.domain.Subject;
import java.util.List;

/**
 * Created by JochemVanHespen on 9/28/2017.
 * UI element which represents all the notes a user makes for a certain Subject
 */

public class NoteActivity extends BaseActivity implements
    android.support.v4.app.LoaderManager.LoaderCallbacks<List<Note>>
{
    private DomainController controller;
    private NoteRecyclerAdapter noteRecyclerAdapter;
    private RecyclerView noteRecyclerView;
    private Subject subject;
    private NoteLoader loader;
    private int idSubject;
    private final String TAG = "NOTE_ACTIVITY";
    public static final int REQUEST_CODE = 66;
    private LoaderManager manager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.note_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        noteRecyclerView = (RecyclerView) findViewById(R.id.noteRecycleView);
        noteRecyclerView.setHasFixedSize(true);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle bundle = getIntent().getExtras();
        controller = bundle.getParcelable("controller");
        idSubject = bundle.getInt("idSubject");
        subject = controller.getSubjectById(idSubject, this);
        super.setTitle(subject.getTitle());
        manager = getSupportLoaderManager();
        manager.initLoader(0,null,this);
        buildNoteListView();
        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.floating_action_button_create_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this,CreateNoteActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    private void buildNoteListView() {
        noteRecyclerAdapter = new NoteRecyclerAdapter(
                controller.getNotesWithSubjectId(this,idSubject),
                this,controller);
        noteRecyclerView.setAdapter(noteRecyclerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Note note;
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                note = new Note(
                        data.getStringExtra("noteTitle"),
                        data.getStringExtra("noteContent"),
                        idSubject);
                controller.addNoteToSubject(note,this);
                manager.restartLoader(0,null,this);
            }
        }
        if(requestCode == REQUEST_CODE + 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                note = new Note(
                        Integer.parseInt(data.getStringExtra("_id")),
                        data.getStringExtra("noteTitle"),
                        data.getStringExtra("noteContent"),
                        idSubject);
                controller.updateNote(note,this);
                manager.restartLoader(0,null,this);
            }
        }
    }

    @Override
    public Loader<List<Note>> onCreateLoader(int id, Bundle args) {
        loader = new NoteLoader(this,controller,idSubject);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Note>> loader, List<Note> data) {
        noteRecyclerAdapter.setNotes(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Note>> loader) {
        noteRecyclerAdapter.setNotes(null);
    }
}
