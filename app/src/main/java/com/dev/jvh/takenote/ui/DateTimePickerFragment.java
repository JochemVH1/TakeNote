package com.dev.jvh.takenote.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.dev.jvh.takenote.R;

/**
 * Created by JochemVanHespen on 12/2/2017.
 * Fragment for picking a date and time
 */

public class DateTimePickerFragment extends Fragment {

    private Button btnDate;
    private Button btnTime;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private DateTimePickerListener dateTimePickerListener;

    public interface DateTimePickerListener{
        void setNotificationTime(int day,int month,int year,int hour,int minute);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            dateTimePickerListener = (DateTimePickerListener) getFragmentManager().findFragmentByTag("Note_create_fragment");
        }catch (ClassCastException cce)
        {
            Log.e("Date_time_picker", "Class" + context.getClass() + " doesn't implement CreateNoteListener");
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dateTimePickerView = inflater.inflate(R.layout.datetimepicker_view, container, false);
        btnDate = dateTimePickerView.findViewById(R.id.datetimepicker_view_date);
        btnTime = dateTimePickerView.findViewById(R.id.datetimepicker_view_time);
        datePicker = dateTimePickerView.findViewById(R.id.datetimepicker_view_datePicker);
        timePicker = dateTimePickerView.findViewById(R.id.datetimepicker_view_timePicker);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.setVisibility(View.INVISIBLE);
                datePicker.setVisibility(View.VISIBLE);
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.VISIBLE);
            }
        });
        return dateTimePickerView;
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
        menuInflater.inflate(R.menu.context_menu_create_note,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.create:
                setTime();
                return false;
        }
        return true;
    }

    private void setTime() {
        dateTimePickerListener.setNotificationTime(datePicker.getDayOfMonth(),datePicker.getMonth(),datePicker.getYear(),timePicker.getCurrentHour(),timePicker.getCurrentMinute());
        getFragmentManager().popBackStack();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
