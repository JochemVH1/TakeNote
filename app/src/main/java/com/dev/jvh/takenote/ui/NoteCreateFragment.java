package com.dev.jvh.takenote.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JochemVanHespen on 10/29/2017.
 * Fragment for performing operations on notes
 */

public class NoteCreateFragment extends Fragment
    implements DateTimePickerFragment.DateTimePickerListener
{

    private EditText noteTitle;
    private EditText noteContent;
    private Switch notifyMe;
    private boolean checked;
    private NoteActivity noteActivity;
    private DomainController controller;
    private final static String TAG = "CREATE_NOTE_FRAGMENT";
    private NoteListener noteListener;
    private Note currentNote;
    private FragmentManager fragmentManager;
    private String notificationTime;

    @Override
    public void setNotificationTime(int day, int month, int year, int hour, int minute) {
        String tempMinute = String.valueOf(minute).length() == 1? String.format("0%d",minute) : String.valueOf(minute);
        notificationTime = String.format("%d-%d-%d  %d:%s",day,month + 1,year,hour,tempMinute);
        if(noteActivity.getCurrentNote() != null)
        {
            noteActivity.getCurrentNote().setNotificationTime(notificationTime);
        }else
            notifyMe.setText(String.format("%s: %s", notifyMe.getText(), notificationTime));
    }


    public interface NoteListener{
        void createNote(String noteTitle,String noteContent,boolean notifyMe, String notificationTime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            noteListener = (NoteListener) context;
        }catch (ClassCastException cce)
        {
            Log.e(TAG, "Class" + context.getClass() + " doesn't implement CreateNoteListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View noteCreateView = inflater.inflate(R.layout.create_note_view, container, false);
        noteActivity = (NoteActivity) getActivity();
        noteActivity.setTitle("");
        controller = noteActivity.getController();
        noteTitle = noteCreateView.findViewById(R.id.noteTitle);
        noteContent = noteCreateView.findViewById(R.id.noteContent);
        notifyMe = noteCreateView.findViewById(R.id.create_note_view_notify_me);
        fragmentManager = getFragmentManager();
        notifyMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checked = b;
                if(checked && compoundButton.isPressed())
                {
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    DateTimePickerFragment dateTimePickerFragment = new DateTimePickerFragment();
                    ft.detach(fragmentManager.findFragmentByTag(((NoteActivity) getActivity()).NOTE_CREATE_FRAGMENT_TAG));
                    ft.add(R.id.note_layout,dateTimePickerFragment,((NoteActivity) getActivity()).DATE_TIME_PICKER_FRAGMENT_TAG);
                    ft.addToBackStack(((NoteActivity) getActivity()).DATE_TIME_PICKER_FRAGMENT_TAG);
                    ft.commit();
                }
                if(!checked)
                {
                    notifyMe.setText(R.string.create_note_view_notify_me);
                }
            }
        });
        if(noteActivity.getCurrentNote() != null){
            currentNote = noteActivity.getCurrentNote();
            notificationTime = currentNote.getNotificationTime();
            noteTitle.setText(currentNote.getTitle());
            noteContent.setText(currentNote.getText());
            notifyMe.setChecked(currentNote.isNotifyMe());
            notifyMe.setText(String.format("%s: %s", notifyMe.getText(), currentNote.getNotificationTime()));
        }
        return noteCreateView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.menu_search);
        menu.removeItem(R.id.menu_settings);
        menu.removeItem(R.id.menu_about);
        menu.removeItem(R.id.menu_logout);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {
        if(currentNote == null)
            menuInflater.inflate(R.menu.context_menu_create_note,menu);
        else
            menuInflater.inflate(R.menu.context_menu_edit_subject,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.create:
                if(currentNote == null)
                    createNote();
                else
                    updateNote();
                return true;
            case R.id.delete:
                deleteNote();
            default: return true;
        }
    }

    private void deleteNote() {
        controller.deleteNote(currentNote,getContext());
        getFragmentManager().popBackStack();
        noteActivity.setCurrentNote(null);
    }

    private void updateNote() {
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        String currentDateTime = df.format(new Date());
        currentNote.setTitle(noteTitle.getText().toString());
        currentNote.setText(noteContent.getText().toString());
        currentNote.setDateUpdated(currentDateTime);
        currentNote.setNotifyMe(checked);
        currentNote.setNotificationTime(notificationTime);
        controller.updateNote(currentNote,getContext());
        getFragmentManager().popBackStack();
        noteActivity.setCurrentNote(null);
    }

    private void createNote() {
        noteListener.createNote(noteTitle.getText().toString(),noteContent.getText().toString(),checked, notificationTime);
        getFragmentManager().popBackStack();
    }

}
