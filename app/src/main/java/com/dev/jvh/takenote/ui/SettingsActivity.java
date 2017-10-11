package com.dev.jvh.takenote.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dev.jvh.takenote.R;

/**
 * Created by JochemVanHespen on 9/28/2017.
 * Launches when Settings in chosen out of the menu
 */

public class SettingsActivity extends AppCompatActivity {
    public final static String PREF_SILENT_MODE = "pref_silent_mode";
    public final static String PREF_SUBJECT_AUTO_CAPATALIZE_TITLE = "pref_subject_auto_capatalize_title";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.application_preferences);
        }
    }
}
