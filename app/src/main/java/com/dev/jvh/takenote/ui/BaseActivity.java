package com.dev.jvh.takenote.ui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.CursorAdapter;

import com.dev.jvh.takenote.R;

/**
 * Created by JochemVanHespen on 9/28/2017.
 * Base activity which contains the elements shared over every activity
 */

public abstract class BaseActivity extends AppCompatActivity
        implements
        LoaderManager.LoaderCallbacks<Cursor>{

    protected CursorAdapter adapter;

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
            default: return super.onOptionsItemSelected(item);
        }
    }

    public abstract void setAdapter();

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

    @Override
    public abstract Loader<Cursor> onCreateLoader(int id, Bundle args);

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
