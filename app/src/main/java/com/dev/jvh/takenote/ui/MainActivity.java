package com.dev.jvh.takenote.ui;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;
import com.dev.jvh.takenote.domain.DatabaseInitializer;

public class MainActivity extends BaseActivity implements CreateSubjectDialog.CreateSubjectListener {

    private DomainController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        DatabaseInitializer db = new DatabaseInitializer(this);
        controller = new DomainController(db);
        //List of subjects gets build after retrieving the data from the shared preferences
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
        controller.saveSubjectToDatabase(subject);
        // List view is build after the new subject is added to the list
        buildListView();
    }



    /**
     * Fills up the list view with the subjects added by the user
     */
    private void buildListView() {
        ListView view = (ListView) findViewById(R.id.listSubject);
        SubjectArrayAdapter adapter = new SubjectArrayAdapter(this, controller.getSubjectsFromDatabase(), controller);
        view.setAdapter(adapter);
    }
}
