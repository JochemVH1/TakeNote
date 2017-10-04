package com.dev.jvh.takenote.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;

import java.util.List;

/**
 * Created by JochemVanHespen on 9/28/2017.
 */

class NoteCursorAdapter extends CursorAdapter {
    private Context context;
    private DomainController controller;
    private LayoutInflater inflater;

    public NoteCursorAdapter(Context context, Cursor cursor, int flags, DomainController controller) {
        // TODO Create own layout view for notes
        super(context,cursor,flags);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.controller = controller;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.subject_view,parent,false);
    }

    @Override
    public void bindView(View rowView, Context context, Cursor cursor) {
        TextView titleView = (TextView) rowView.findViewById(R.id.title);
        titleView.setText(cursor.getString(cursor.getColumnIndex("title")));
    }
}
