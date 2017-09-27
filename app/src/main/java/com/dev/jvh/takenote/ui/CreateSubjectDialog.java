package com.dev.jvh.takenote.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.dev.jvh.takenote.R;

/**
 * Created by JochemVanHespen on 9/22/2017.
 */

public class CreateSubjectDialog extends DialogFragment {
    private final static String TAG = "CREATE_SUBJECT_DIALOG";

    public interface CreateSubjectListener {
        void createSubjectPositive(DialogFragment fragment, String title);
    }
    CreateSubjectListener createSubjectListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View createSubjectView = inflater.inflate(R.layout.create_subject_dialog,null);
        builder.setView(createSubjectView)
            .setTitle(R.string.create_subject)
        .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createSubjectListener.createSubjectPositive(
                        CreateSubjectDialog.this,
                        ((EditText) createSubjectView.findViewById(R.id.editTextSubjectName)).getText().toString());

            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            createSubjectListener = (CreateSubjectListener) context;
        } catch (ClassCastException cce)
        {
            Log.e(TAG, "Class" + context.getClass() + " doesn't implement CreateSubjectInterface");
        }
    }
}
