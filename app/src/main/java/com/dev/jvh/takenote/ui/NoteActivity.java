package com.dev.jvh.takenote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
    private final String TAG = "NOTE_ACTIVITY";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.note_view);
        Bundle bundle = getIntent().getExtras();
        controller = bundle.getParcelable("controller");
        controller.setDb(new DatabaseInitializer(this));
        subject = controller.getSubjectFromList(bundle.getInt("indexSubject"));
        super.setTitle(subject.getTitle());
    }

    public void createNote(View view)
    {

    }
}
