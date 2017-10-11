package com.dev.jvh.takenote.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;

/**
 * Created by JochemVanHespen on 10/11/2017.
 * Show a detailed view of selected subject
 */

public class SubjectDetailActivity extends BaseActivity {
    private DomainController controller;
    private EditText title;
    private EditText description;
    private Subject subject;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_detail_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        title = (EditText) findViewById(R.id.subject_detail_view_title_subject);
        description = (EditText) findViewById(R.id.subject_detail_view_description_subject);
        Bundle bundle = getIntent().getExtras();
        controller = bundle.getParcelable("controller");
        subject = controller.getSubjectById(bundle.getInt("idSubject"),this);
        title.setText(subject.getTitle());
        description.setText(subject.getDescription());
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
                updateSubject();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubject() {
        subject.setTitle(title.getText().toString());
        subject.setDescription(description.getText().toString());
        controller.updateSubject(subject,this);
        setResult(Activity.RESULT_OK,getIntent());
        finish();
    }
}
