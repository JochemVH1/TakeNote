package com.dev.jvh.takenote.ui;

import android.app.DialogFragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;

import java.util.List;

public class MainActivity extends BaseActivity
        implements
        CreateSubjectDialog.CreateSubjectListener,
        LoaderManager.LoaderCallbacks<List<Subject>>
{

    private DomainController controller;
    private TextView titleView;
    private UpdateTitleView updateTitleView;
    private RecyclerView subjectRecyclerView;
    private SubjectRecyclerAdapter subjectRecyclerAdapter;
    private SubjectLoader loader;
    private LoaderManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
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
    }

    /**
     * Method called from the CREATE SUBJECT BUTTON
     * @param view element which fires the method
     */
    public void createSubjectDialog(View view)
    {
        CreateSubjectDialog dialog = new CreateSubjectDialog();
        dialog.show(getFragmentManager(),"exit");
    }
    /**
     * Method called to when the OK button is pressed in the CreateSubjectDialog
     * @param fragment which triggers the method
     * @param title typed int the CreateSubjectDialog
     */
    @Override
    public void createSubjectPositive(DialogFragment fragment, String title) {
        Subject subject = new Subject(title);
        // Save subject to database
        controller.saveSubjectToDatabase(subject, this);
        manager.restartLoader(0,null,this);

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


