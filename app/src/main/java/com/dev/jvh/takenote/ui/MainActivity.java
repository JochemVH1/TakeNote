package com.dev.jvh.takenote.ui;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.Domaincontroller;
import com.dev.jvh.takenote.domain.Subject;
import com.dev.jvh.takenote.domain.DatabaseInitializer;

public class MainActivity extends AppCompatActivity implements CreateSubjectDialog.CreateSubjectListener {

    private Domaincontroller controller;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseInitializer db = new DatabaseInitializer(this);
        controller = new Domaincontroller(db);
        //List of subjects gets build after retrieving the data from the shared preferences
        buildListView();
    }

    /**
     * Method called from the CREATE SUBJECT BUTTON
     * @param view
     */
    public void createSubjectDialog(View view)
    {
        CreateSubjectDialog dialog = new CreateSubjectDialog();
        dialog.show(getFragmentManager(),"exit");
    }

    /**
     * Method called to when the OK button is pressed in the CreateSubjectDialog
     * @param fragment
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                goToSettings();
                return true;
            case R.id.menu_about:
                goToAbout();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method called to go to the preference settings
     * NOT YET IMPLEMENTED -- TODO
     */
    private void goToSettings() {
        Toast.makeText(getApplicationContext(),"Go to Settings",Toast.LENGTH_SHORT).show();
    }

    /**
     * method called to go to the about page
     * NOT YET IMPLEMENTED -- TODO
     */
    private void goToAbout() {
        Toast.makeText(getApplicationContext(),"Go to About",Toast.LENGTH_SHORT).show();
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
