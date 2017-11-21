package com.dev.jvh.takenote.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.Subject;

import java.util.List;

/**
 * Created by JochemVanHespen on 10/7/2017.
 * Fills SubjectRecycleView with different Subjects
 */

class SubjectRecyclerAdapter extends
        RecyclerView.Adapter<SubjectRecyclerAdapter.SubjectViewHolder> {

    private List<Subject> subjects;
    private OnSubjectClicked onSubjectClicked;

    public interface OnSubjectClicked{
        void onClick(int idSubject);
        void onLongClick(int idSubject);
    }


    SubjectRecyclerAdapter(List<Subject> subjects,
                           OnSubjectClicked onSubjectClicked) {
        this.subjects = subjects;
        this.onSubjectClicked = onSubjectClicked;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_card_view,parent,false);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position) {
        holder.description.setText(subjects.get(position).getDescription());
        holder.title.setText(subjects.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
        notifyDataSetChanged();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,View.OnLongClickListener
    {
        TextView title;
        TextView description;

        SubjectViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSubjectClicked.onClick(subjects.get(getLayoutPosition()).get_id());
        }

        @Override
        public boolean onLongClick(View v) {
            onSubjectClicked.onLongClick(subjects.get(getLayoutPosition()).get_id());
            // returning true prevents onclick event to trigger
            return true;
        }
    }
}
