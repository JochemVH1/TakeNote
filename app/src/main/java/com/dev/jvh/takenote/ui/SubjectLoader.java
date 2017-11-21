package com.dev.jvh.takenote.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;
import java.util.List;

/**
 * Created by JochemVanHespen on 10/7/2017.
 * Loader implementation for loading new Subjects in background thread
 */

class SubjectLoader extends AsyncTaskLoader<List<Subject>> {

    private DomainController controller;

    SubjectLoader(Context context, DomainController controller) {
        super(context);
        this.controller = controller;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Subject> loadInBackground() {
        return controller.getSubjectsFromDatabase(getContext());
    }
}
