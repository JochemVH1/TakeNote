package com.dev.jvh.takenote.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;
import java.util.List;

/**
 * Created by JochemVanHespen on 10/9/2017.
 * Loader implementation for loading new Notes in background thread
 */

class NoteLoader extends AsyncTaskLoader<List<Note>> {
    private DomainController controller;
    private int idSubject;

    NoteLoader(Context context, DomainController controller, int idSubject) {
        super(context);
        this.controller = controller;
        this.idSubject = idSubject;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Note> loadInBackground() {
        return controller.getNotesWithSubjectId(getContext(),idSubject);
    }
}
