package com.dev.jvh.takenote.ui;

import android.app.ActivityOptions;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.View;
import android.view.ViewGroup;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;
import com.dev.jvh.takenote.domain.User;
import com.dev.jvh.takenote.util.IabBroadcastReceiver;
import com.dev.jvh.takenote.util.IabHelper;
import com.dev.jvh.takenote.util.IabResult;
import com.dev.jvh.takenote.util.Inventory;
import com.dev.jvh.takenote.util.Purchase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity
    implements
        CreateSubjectDialog.CreateSubjectListener,
        IabBroadcastReceiver.IabBroadcastListener

{
    private FirebaseAuth mAuth;
    private User mUser;
    private DomainController controller;
    private FirebaseUser user;
    private FragmentManager fragmentManager;
    protected final String SUBJECT_FRAGMENT_TAG = "Subject_fragment";
    protected final String HEADER_FRAGMENT_TAG = "Header_fragment";
    protected final int NUM_PAGES = 2;
    private int currentSubjectId;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private IabHelper iabHelper;
    String base64 = ""
    public final String TAG = "MAIN_ACTIVITY";
    public final int RC_REQUEST = 7777;
    private final String SKU_PREMIUM = "premium";

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null)
        {
            startLoginActivity();
        }else{
            super.onCreate(savedInstanceState);
            super.setContentView(R.layout.subject_view);
            Toolbar myToolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            Bundle bundle = getIntent().getExtras();
            mUser = new User(user.getDisplayName(), user.getEmail());
            assert bundle != null;
            Parcelable temp = bundle.getParcelable("controller");
            if(temp == null)
                controller = new DomainController();
            else
                controller = (DomainController) temp;
            fragmentManager = getSupportFragmentManager();
            viewPager = findViewById(R.id.view_pager);
            pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(1);
            iabHelper = new IabHelper(this,base64);
            iabHelper.enableDebugLogging(true);
            iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
                @Override
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        complain("Problem setting up in-app billing: " + result);
                        return;
                    }

                    // Have we been disposed of in the meantime? If so, quit.
                    if (iabHelper == null) return;

                    // Important: Dynamically register for broadcast messages about updated purchases.
                    // We register the receiver here instead of as a <receiver> in the Manifest
                    // because we always call getPurchases() at startup, so therefore we can ignore
                    // any broadcasts sent while the app isn't running.
                    // Note: registering this listener in an Activity is a bad idea, but is done here
                    // because this is a SAMPLE. Regardless, the receiver must be registered after
                    // IabHelper is setup, but before first call to getPurchases().
                    mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
                    IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                    registerReceiver(mBroadcastReceiver, broadcastFilter);

                    // IAB is fully set up. Now, let's get an inventory of stuff we own.
                    Log.d(TAG, "Setup successful. Querying inventory.");
                    try {
                        iabHelper.queryInventoryAsync(mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error querying inventory. Another async operation in progress.");
                    }
                }
            });
        }

    }

    public FirebaseUser getUser() {
        return user;
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


    public void buyPremium(View view)
    {
        launchPurchaseFlow();
    }

    private void launchPurchaseFlow() {
        try {
            iabHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
                    mPurchaseFinishedListener,mUser.getEmail());
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain(e.getMessage());
        }
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase premium = inventory.getPurchase(SKU_PREMIUM);
            if (premium != null && verifyDeveloperPayload(premium)) {
                mUser.setPremium(true);
            }

        }
    };

    public void complain(String message) {
        Log.e(TAG, "**** TakeNote Error: " + message);
    }

    public IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + info);

            if (iabHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }

            if (info.getSku().equals(SKU_PREMIUM)) {
                mUser.setPremium(true);
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (iabHelper == null) return;
        // Pass on the activity result to the helper for handling
        if (!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void receivedBroadcast() {
        try {
            iabHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        if(p.getSku().equals(SKU_PREMIUM))
        {
            String payload = p.getDeveloperPayload();
            return payload.equals(mUser.getEmail());
        }
        return false;
    }
}


