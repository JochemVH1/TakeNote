package com.dev.jvh.takenote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

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
    private TextView wordCount;
    private TextView letterCount;
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
/*        wordCount = (TextView) findViewById(R.id.subject_detail_view_wordCount);
        letterCount = (TextView) findViewById(R.id.subject_detail_view_letterCount);
        String letterCountTemp = letterCount.getText().toString() + String.valueOf(subject.letterCount());
        String wordCountTemp = wordCount.getText().toString() + String.valueOf(subject.wordCount());
        wordCount.setText(wordCountTemp);
        letterCount.setText(letterCountTemp);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.context_menu_edit_subject,menu);
        return true;
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
        controller.deleteSubjectFromDatabase(this,subject.get_id());
        Intent intent = getIntent();
        intent.putExtra("deleteAction",true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void updateSubject() {
        subject.setTitle(title.getText().toString());
        subject.setDescription(description.getText().toString());
        Intent intent = getIntent();
        intent.putExtra("deleteAction",false);
        controller.updateSubject(subject,this);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
