package com.dev.jvh.takenote.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;
import com.dev.jvh.takenote.domain.Subject;

import java.util.List;

/**
 * Created by JochemVanHespen on 10/29/2017.
 * Fragment which displays different notes
 */

public class NoteFragment extends Fragment
    implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<List<Note>>,
        NoteRecyclerAdapter.OnNoteClicked
{

    private DomainController controller;
    private MainActivity mainActivity;
    private NoteRecyclerAdapter noteRecyclerAdapter;
    private RecyclerView noteRecyclerView;
    private int idSubject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View noteView = inflater.inflate(R.layout.note_view, container, false);
        mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getController();
        idSubject = mainActivity.getCurrentSubjectId();
        Subject subject = controller.getSubjectById(idSubject, getContext());
        mainActivity.setTitle(subject.getTitle());
        noteRecyclerView = noteView.findViewById(R.id.noteRecycleView);
        noteRecyclerView.setHasFixedSize(true);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LoaderManager manager = getActivity().getSupportLoaderManager();
        manager.initLoader(1,null,this);
        buildNoteListView();
        FloatingActionButton floatingActionButton =
                noteView.findViewById(R.id.floating_action_button_create_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                NoteFragment noteFragment =
                        (NoteFragment) getFragmentManager().findFragmentByTag(mainActivity.NOTE_FRAGMENT_TAG);
                ft.detach(noteFragment);
                NoteCreateFragment noteCreateFragment = new NoteCreateFragment();
                ft.add(R.id.main_layout,noteCreateFragment,mainActivity.NOTE_CREATE_FRAGMENT_TAG);
                ft.addToBackStack(noteCreateFragment.getClass().getName());
                ft.commit();
            }
        });
        return noteView;
    }

    private void buildNoteListView() {
        noteRecyclerAdapter = new NoteRecyclerAdapter(
                controller.getNotesWithSubjectId(getContext(),idSubject)
                ,this);
        noteRecyclerView.setAdapter(noteRecyclerAdapter);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.menu_settings);
        menu.removeItem(R.id.menu_about);
        menu.removeItem(R.id.menu_logout);
    }

    @Override
    public Loader<List<Note>> onCreateLoader(int id, Bundle args) {
        return new NoteLoader(getContext(), controller, idSubject);
    }

    @Override
    public void onLoadFinished(Loader<List<Note>> loader, List<Note> data) {
        if(noteRecyclerAdapter != null)
            noteRecyclerAdapter.setNotes(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Note>> loader) {
        noteRecyclerAdapter.setNotes(null);
    }

    @Override
    public void onClick(Note note) {
        mainActivity.setCurrentNote(note);
        launchFragment(new NoteCreateFragment(), mainActivity.NOTE_CREATE_FRAGMENT_TAG);
    }

    private void launchFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.add(R.id.main_layout,fragment,tag);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();
    }
}
