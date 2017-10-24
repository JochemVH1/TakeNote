package com.dev.jvh.takenote.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class SubjectActivity extends BaseActivity
        implements
        CreateSubjectDialog.CreateSubjectListener,
        LoaderManager.LoaderCallbacks<List<Subject>>
{
    private FirebaseAuth mAuth;
    private DomainController controller;
    private TextView titleView;
    private UpdateTitleView updateTitleView;
    private RecyclerView subjectRecyclerView;
    private SubjectRecyclerAdapter subjectRecyclerAdapter;
    private SubjectLoader loader;
    private LoaderManager manager;
    private FirebaseUser user;
    public static final int REQUEST_CODE = 66;

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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        controller = new DomainController();
        manager = getSupportLoaderManager();
        manager.initLoader(0,null,this);
        subjectRecyclerView = (RecyclerView) findViewById(R.id.subjectRecycleView);
        subjectRecyclerView.setHasFixedSize(true);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        titleView = (TextView) findViewById(R.id.titleViewMainActivity);
        updateTitleView = new UpdateTitleView();
        updateTitleView.execute();
        buildListView();
        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.floating_action_button_create_subject);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSubjectDialog dialog = new CreateSubjectDialog();
                dialog.show(getFragmentManager(),"exit");
            }
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /**
     * Method called to when the OK button is pressed in the CreateSubjectDialog
     * @param fragment which triggers the method
     * @param title typed int the CreateSubjectDialog
     */
    @Override
    public void createSubjectPositive(DialogFragment fragment, String title) {
        if(title.isEmpty())
            return;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        title = preferences.getBoolean(SettingsActivity.PREF_SUBJECT_AUTO_CAPATALIZE_TITLE,true)
                        ? title.substring(0,1).toUpperCase() + title.substring(1) : title;
        Subject subject = new Subject(title);
        // Save subject to database
        controller.saveSubjectToDatabase(subject, this);
        manager.restartLoader(0,null,this);

    }

    public void startDetailActivity(int idSubject)
    {
        Intent intent= new Intent(this,SubjectDetailActivity.class);
        intent.putExtra("controller",controller);
        intent.putExtra("idSubject", idSubject);
        startActivityForResult(intent,REQUEST_CODE);
    }

    /**
     * Fills up the list view with the subjects added by the user
     */
    protected void buildListView() {
        subjectRecyclerAdapter = new SubjectRecyclerAdapter(
                controller.getSubjectsFromDatabase(this),this,controller);
        subjectRecyclerView.setAdapter(subjectRecyclerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if(!data.getBooleanExtra("deleteAction",false))
                    subjectRecyclerAdapter.notifyItemChanged(data.getIntExtra("idSubject",0));
                else
                    subjectRecyclerAdapter.notifyItemRemoved(data.getIntExtra("idSubject",0));
                manager.restartLoader(0,null,this);
            }
        }
    }

    @Override
    public Loader<List<Subject>> onCreateLoader(int id, Bundle args) {
        loader = new SubjectLoader(this,controller);
        return loader;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateTitleView = new UpdateTitleView();
        updateTitleView.execute();
    }

    @Override
    public void onLoadFinished(Loader<List<Subject>> loader, List<Subject> data) {
        subjectRecyclerAdapter.setSubjects(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Subject>> loader) {
        subjectRecyclerAdapter.setSubjects(null);
    }

    private class UpdateTitleView extends AsyncTask<Void,String,Void>
    {
        private String[] titles = getResources().getStringArray(R.array.app_info_titles);
        private int counter;
        UpdateTitleView()
        {
            counter = 0;
        }
        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                do {
                    publishProgress(titles[counter++]);
                    if(counter == titles.length)
                        counter = 0;
                    Thread.sleep(20000);
                }while(true);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        // called from doInBackground with publishProgress, can modify UI
        @Override
        protected void onProgressUpdate(String... parameters) {
            titleView.setText(parameters[0]);
        }
    }

}


