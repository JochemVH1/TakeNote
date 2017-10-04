package com.dev.jvh.takenote.ui;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;

public class MainActivity extends BaseActivity
        implements
        CreateSubjectDialog.CreateSubjectListener
{

    private DomainController controller;
    private ListView listSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        controller = new DomainController();
        getLoaderManager().initLoader(0,null,this);
        listSubjects = (ListView) findViewById(R.id.listSubject);
        buildListView();
    }

    @Override
    public void setAdapter() {
        adapter =  new SubjectCursorAdapter(this,
                controller.getSubjectsFromDatabase(this), 0, controller);
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
    }

    /**
     * Fills up the list view with the subjects added by the user
     */
    protected void buildListView() {
        setAdapter();
        listSubjects.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                Uri.parse("content://com.dev.jvh.takenote.contentprovider/subjects"),
                new String[]{"_id","title","description"},null,null,null);
        return loader;
    }

}
