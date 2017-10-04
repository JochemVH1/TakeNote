package com.dev.jvh.takenote.ui;

import android.content.Context;
import android.content.Intent;
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
 * UI element that fills up the Note ListView with different Notes
 */

class NoteCursorAdapter extends CursorAdapter {
    private LayoutInflater inflater;

    NoteCursorAdapter(Context context, Cursor cursor, int flags) {
        // TODO Create own layout view for notes
        super(context,cursor,flags);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.subject_view,parent,false);
    }

    @Override
    public void bindView(View rowView, final Context context, Cursor cursor) {
        TextView titleView = (TextView) rowView.findViewById(R.id.title);
        titleView.setText(cursor.getString(cursor.getColumnIndex("title")));

        rowView.setTag(new String[]{
                String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))),
                cursor.getString(cursor.getColumnIndex("title")),
                cursor.getString(cursor.getColumnIndex("text"))
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CreateNoteActivity.class);
                intent.putExtra("_id",((String[]) v.getTag())[0]);
                intent.putExtra("title",((String[]) v.getTag())[1]);
                intent.putExtra("text",((String[]) v.getTag())[2]);
                ((NoteActivity) context).startActivityForResult(intent,NoteActivity.REQUEST_CODE + 1);
            }
        });
    }
}
