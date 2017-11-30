package com.dev.jvh.takenote.ui;

import android.app.ActivityOptions;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity
    implements
        CreateSubjectDialog.CreateSubjectListener

{
    private FirebaseAuth mAuth;
    private DomainController controller;
    private FirebaseUser user;
    private FragmentManager fragmentManager;
    protected final String SUBJECT_FRAGMENT_TAG = "Subject_fragment";
    protected final String HEADER_FRAGMENT_TAG = "Header_fragment";
    protected final String NOTE_FRAGMENT_TAG = "Note_fragment";
    protected final String SUBJECT_DETAIL_FRAGMENT_TAG = "Subject_detail_fragment";
    protected final String NOTE_CREATE_FRAGMENT_TAG = "Note_create_fragment";
    protected final int NUM_PAGES = 2;
    private int currentSubjectId;
    private Note currentNote;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null)
        {
            /*
            *  TODO After pressing the back button in the login activity and reentering the app through
            *  TODO menu of android. The back button shows the same screen twice after the login
            * */
            startLoginActivity();
        }else{
            super.onCreate(savedInstanceState);
            super.setContentView(R.layout.subject_view);
            Toolbar myToolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            Bundle bundle = getIntent().getExtras();
            assert bundle != null;
            Parcelable temp = bundle.getParcelable("controller");
            if(temp == null)
                controller = new DomainController();
            else
                controller = (DomainController) temp;
            fragmentManager = getSupportFragmentManager();
            //FragmentTransaction ft = fragmentManager.beginTransaction();
            viewPager = findViewById(R.id.view_pager);
            pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(1);
            //ft.add(R.id.main_layout,headerFragment,HEADER_FRAGMENT_TAG);
            //ft.add(R.id.main_layout,subjectFragment,SUBJECT_FRAGMENT_TAG);
            //ft.addToBackStack(headerFragment.getClass().getName());
            //ft.addToBackStack(subjectFragment.getClass().getName());
            //ft.commit();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.menu_search);
        return true;
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    protected DomainController getController()
    {
        return controller;
    }

    /**
     * Method called to when the OK button is pressed in the CreateSubjectDialog
     * @param fragment which triggers the method
     * @param title typed int the CreateSubjectDialog
     */
    @Override
    public void createSubjectPositive(DialogFragment fragment, String title, int colorId) {
        SubjectFragment subjectFragment = getSubjectFragment();
        if(subjectFragment != null)
            subjectFragment.createSubject(title, colorId);
    }

    private SubjectFragment getSubjectFragment(){
        SubjectFragment subjectFragment =
                (SubjectFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
        if(subjectFragment != null && subjectFragment.isVisible())
            return subjectFragment;
        return null;
    }


    public void setCurrentSubjectId(int currentSubjectId) {
        this.currentSubjectId = currentSubjectId;
    }

    public int getCurrentSubjectId() {
        return currentSubjectId;
    }



    public void setCurrentNote(Note currentNote) {
        this.currentNote = currentNote;
    }
    public Note getCurrentNote() {
        return currentNote;
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private int currentPosition;
        private Fragment currentFragment;

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            currentPosition = position;
            switch(position)
            {
                case 0:
                    currentFragment = new HeaderFragment();
                    return currentFragment;
                case 1:
                    currentFragment = new SubjectFragment();
                    return currentFragment;
            }
            return null;
        }

        public String getItemTag()
        {
            switch(currentPosition)
            {
                case 0: return HEADER_FRAGMENT_TAG;
                case 1: return SUBJECT_FRAGMENT_TAG;
            }
            return null;
        }
        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}


