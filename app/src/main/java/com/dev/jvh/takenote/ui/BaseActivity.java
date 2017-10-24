package com.dev.jvh.takenote.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.dev.jvh.takenote.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by JochemVanHespen on 9/28/2017.
 * Base activity which contains the elements shared over every activity
 */

public class BaseActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
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
            case R.id.menu_logout:
                logOut();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    /**
     * Method called to go to the preference settings
     */
    private void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * method called to go to the about page
     * NOT YET IMPLEMENTED -- TODO
     */
    private void goToAbout() {
        //Toast.makeText(getApplicationContext(),"Go to About",Toast.LENGTH_SHORT).show();
    }


}
