package com.dev.jvh.takenote.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;
import com.dev.jvh.takenote.util.FragmentType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by JochemVanHespen on 10/29/2017.
 * Fragment which shows the subjects
 */

public class SubjectFragment extends Fragment
    implements
        LoaderManager.LoaderCallbacks<List<Subject>>,
        SubjectRecyclerAdapter.OnSubjectClicked
{

    private RecyclerView subjectRecyclerView;
    private SubjectRecyclerAdapter subjectRecyclerAdapter;
    private LoaderManager manager;
    private DomainController controller;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View subjectView = inflater.inflate(R.layout.subject_fragment, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.setTitle("TakeNote");
        controller = mainActivity.getController();
        manager = getActivity().getSupportLoaderManager();
        manager.initLoader(0, null, this);
        subjectRecyclerView = subjectView.findViewById(R.id.subjectRecycleView);
        subjectRecyclerView.setHasFixedSize(true);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        buildListView();
        FloatingActionButton floatingActionButton =
                subjectView.findViewById(R.id.floating_action_button_create_subject);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSubjectDialog dialog = new CreateSubjectDialog();
                dialog.show(mainActivity.getFragmentManager(),"exit");
            }
        });
        return subjectView;
    }

    /**
     * Fills up the list view with the subjects added by the user
     */
    protected void buildListView() {
        subjectRecyclerAdapter = new SubjectRecyclerAdapter(
                controller.getSubjectsFromDatabase(getContext()),
                this);
        subjectRecyclerView.setAdapter(subjectRecyclerAdapter);
    }


    public void restartLoader() {
        manager.restartLoader(0, null, this);
    }

    @Override
    public Loader<List<Subject>> onCreateLoader(int id, Bundle args) {
        return new SubjectLoader(getContext(), controller);
    }

    @Override
    public void onLoadFinished(Loader<List<Subject>> loader, List<Subject> data) {
        subjectRecyclerAdapter.setSubjects(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Subject>> loader) {
        subjectRecyclerAdapter.setSubjects(null);
    }

    public void createSubject(String title, int colorId) {
        if(title.isEmpty())
            return;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        title = preferences.getBoolean(SettingsActivity.PREF_SUBJECT_AUTO_CAPATALIZE_TITLE,true)
                ? title.substring(0,1).toUpperCase() + title.substring(1) : title;

        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        String currentDateTime = df.format(new Date());
        Subject subject = new Subject(title, colorId, currentDateTime, currentDateTime);
        // Save subject to database
        controller.saveSubjectToDatabase(subject, getContext());
        restartLoader();
    }

    @Override
    public void onClick(int idSubject) {
        mainActivity.setCurrentSubjectId(idSubject);
        launchNoteActivity(FragmentType.NOTEFRAGMENT);
    }

    @Override
    public void onLongClick(int idSubject) {
        mainActivity.setCurrentSubjectId(idSubject);
        launchNoteActivity(FragmentType.SUBJECTDETAILFRAGMENT);
    }

    private void launchNoteActivity(FragmentType type)
    {
        getFragmentManager().popBackStack();
        Intent intent = new Intent(getActivity(),NoteActivity.class);
        intent.putExtra("currentSubjectId",mainActivity.getCurrentSubjectId());
        intent.putExtra("controller",mainActivity.getController());
        intent.putExtra("type",type);
        startActivity(intent);

        /*FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.add(R.id.main_layout,fragment,tag);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();*/
    }
}