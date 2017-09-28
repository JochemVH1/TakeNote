package com.dev.jvh.takenote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DatabaseInitializer;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;

/**
 * Created by JochemVanHespen on 9/28/2017.
 * UI element which represents all the notes a user makes for a certain Subject
 */

public class NoteActivity extends BaseActivity {
    private DomainController controller;
    private Subject subject;
    private int indexSubject;
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
        controller.setDb(new DatabaseInitializer(this));
        indexSubject = bundle.getInt("indexSubject");
        subject = controller.getSubjectFromList(indexSubject);
        super.setTitle(subject.getTitle());
        buildNoteListView();
    }

    private void buildNoteListView() {
        ListView noteListView = (ListView) findViewById(R.id.notesListView);
        noteListView.setAdapter(new NoteArrayAdapter(this,controller,controller.getSubjects().get(indexSubject).getNotes()));
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
                controller.addNoteToSubject(indexSubject,data.getStringExtra("noteTitle"),data.getStringExtra("noteContent"));
                buildNoteListView();
            }
        }
    }
}
