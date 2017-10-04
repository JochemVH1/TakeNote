package com.dev.jvh.takenote.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.dev.jvh.takenote.R;

/**
 * Created by JochemVanHespen on 9/28/2017.
 * Used to create a note :)
 */

public class CreateNoteActivity extends BaseActivity {
    private EditText noteTitle;
    private EditText noteContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        noteTitle = (EditText) findViewById(R.id.noteTitle);
        noteContent = (EditText) findViewById(R.id.noteContent);
        Bundle bundle = getIntent().getExtras();
        if(bundle.get("title") != null)
            noteTitle.setText((String) bundle.get("title"));
        if(bundle.get("text") != null)
            noteContent.setText((String) bundle.get("text"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.context_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.create:
                createNote();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setAdapter(){}

    private void createNote() {
        Intent intent = getIntent();
        intent.putExtra("noteTitle", noteTitle.getText().toString());
        intent.putExtra("noteContent", noteContent.getText().toString());
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){return null;}
}
