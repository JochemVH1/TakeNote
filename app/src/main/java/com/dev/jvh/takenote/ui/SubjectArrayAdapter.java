package com.dev.jvh.takenote.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

class SubjectArrayAdapter extends ArrayAdapter<Subject> {

    private Context context;

    private List<Subject> subjects;

    private DomainController controller;

    SubjectArrayAdapter(Context context, List<Subject> subjects, DomainController controller)
    {
        super(context, R.layout.subject_view,R.id.title,subjects);
        this.context = context;
        this.subjects = subjects;
        this.controller = controller;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.subject_view,parent,false);

        TextView titleView = (TextView) rowView.findViewById(R.id.title);
        titleView.setText(subjects.get(position).getTitle());
        TextView descriptionView = (TextView) rowView.findViewById(R.id.description);
        descriptionView.setText(subjects.get(position).getDescription());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,NoteActivity.class);
                intent.putExtra("controller",controller);
                intent.putExtra("indexSubject",position);
                context.startActivity(intent);
            }
        });

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImageButton delete = (ImageButton) v.findViewById(R.id.deleteSubjectButton);
                    delete.setVisibility(View.VISIBLE);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            controller.deleteSubjectFromDatabase(subjects.get(position));
                            subjects.remove(position);
                            notifyDataSetChanged();
                        }
                    });
/*                }*/
                return false;
            }
        });
        return rowView;
    }
}
