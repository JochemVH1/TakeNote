package com.dev.jvh.takenote.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;

import java.util.List;

/**
 * Created by JochemVanHespen on 9/19/2017.
 * UI element that fills up the Subject ListView with different Subjects
 */

class SubjectCursorAdapter extends CursorAdapter {


    private LayoutInflater inflater;
    private DomainController controller;

    SubjectCursorAdapter(Context context, Cursor cursor, int flags, DomainController controller)
    {
        super(context, cursor, flags);
        this.controller = controller;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.subject_view,parent,false);

    }

    @Override
    public void bindView(final View rowView, final Context context, final Cursor cursor) {
        TextView titleView = (TextView) rowView.findViewById(R.id.title);

        titleView.setText(cursor.getString(cursor.getColumnIndex("title")));
        TextView descriptionView = (TextView) rowView.findViewById(R.id.description);
        descriptionView.setText(cursor.getString(cursor.getColumnIndex("description")));
        rowView.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,NoteActivity.class);
                intent.putExtra("controller",controller);
                intent.putExtra("idSubject", (int) v.getTag());
                context.startActivity(intent);
            }
        });

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImageButton delete = (ImageButton) v.findViewById(R.id.deleteSubjectButton);
                delete.setVisibility(View.VISIBLE);
                delete.setTag(v.getTag());
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        controller.deleteSubjectFromDatabase(context,(int) v.getTag());
                        ((MainActivity) context).buildListView();
                    }
                });
                return false;
            }
        });
    }
}
