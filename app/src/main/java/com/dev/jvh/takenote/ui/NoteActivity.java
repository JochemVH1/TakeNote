package com.dev.jvh.takenote.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;
import com.dev.jvh.takenote.util.FragmentType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.dev.jvh.takenote.BuildConfig.DEBUG;

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
    public final String NOTE_CREATE_FRAGMENT_TAG = "Note_create_fragment";
    protected final String SUBJECT_DETAIL_FRAGMENT_TAG = "Subject_detail_fragment";
    protected final String TAG = "Note_activity";
    protected final String DATE_TIME_PICKER_FRAGMENT_TAG = "Date_time_picker_fragment";
    private Note currentNote;
    private FragmentType type;
    private NoteFragment noteFragment;
    private String notificationTime;

    @Override
    public void startActivity(Intent intent) {
        // check if search intent
        /*if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            getIntent().putExtra("controller",controller);
            getIntent().putExtra("currentSubjectId",currentSubjectId);
            getIntent().putExtra("type", type);
        }*/

        super.startActivity(intent);
    }
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
            noteFragment = new NoteFragment();
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

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent)
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    private void search(String query) {
        if(DEBUG) Log.i(TAG, "search: " + query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(type == FragmentType.NOTEFRAGMENT)
        {
            // Get the SearchView and set the searchable configuration
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            // Assumes current activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    noteFragment.getAdapter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    noteFragment.getAdapter().filter(newText);
                    return true;
                }
            });
        }
        return true;// Do not iconify the widget; expand it by default
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
    public boolean onSearchRequested() {
        //pauseSomeStuff();
        return super.onSearchRequested();
    }
    @Override
    public void createNote(String noteTitle, String noteContent, boolean notifyMeChecked, String notificationTime) {
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        String currentDateTime = df.format(new Date());
        this.notificationTime = notificationTime;
        Note note = new Note(
                noteTitle,
                noteContent,
                currentSubjectId,
                currentDateTime,
                currentDateTime,
                notifyMeChecked,
                notifyMeChecked ? notificationTime : "");
        controller.addNoteToSubject(note,this);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().findFragmentByTag(NOTE_CREATE_FRAGMENT_TAG) != null)
        {
            currentNote = null;
        }
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


    public String getNotificationTime() {
        return notificationTime;
    }
}
