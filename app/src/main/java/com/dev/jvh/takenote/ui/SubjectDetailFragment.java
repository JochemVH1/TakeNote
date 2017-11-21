package com.dev.jvh.takenote.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;

/**
 * Created by JochemVanHespen on 10/29/2017.
 * Fragment which shows details of a certain subject
 */

public class SubjectDetailFragment extends Fragment {
    private EditText title;
    private EditText description;
    private DomainController controller;
    private Subject subject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View subjectDetailView = inflater.inflate(R.layout.subject_detail_fragment, container, false);
        title = subjectDetailView.findViewById(R.id.subject_detail_view_title_subject);
        description = subjectDetailView.findViewById(R.id.subject_detail_view_description_subject);
        //Bundle bundle = getIntent().getExtras();
        MainActivity mainActivity = (MainActivity) getActivity();
        controller = mainActivity.getController();
        subject = controller.getSubjectById(mainActivity.getCurrentSubjectId(),getContext());
        title.setText(subject.getTitle());
        description.setText(subject.getDescription());
        return subjectDetailView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.menu_settings);
        menu.removeItem(R.id.menu_about);
        menu.removeItem(R.id.menu_logout);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {
        menuInflater.inflate(R.menu.context_menu_edit_subject,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.create:
                updateSubject();
                return true;
            case R.id.delete:
                deleteSubject();
                return true;
            default: return true;
        }
    }

    private void deleteSubject() {
        controller.deleteSubjectFromDatabase(getContext(),subject.get_id());
        //mainActivity.refreshSubjectAdapterAfterDelete();
        getFragmentManager().popBackStack();
    }

    private void updateSubject() {
        subject.setTitle(title.getText().toString());
        subject.setDescription(description.getText().toString());
        controller.updateSubject(subject,getContext());
        //mainActivity.refreshSubjectAdapterAfterUpdate();
        getFragmentManager().popBackStack();
    }
}
