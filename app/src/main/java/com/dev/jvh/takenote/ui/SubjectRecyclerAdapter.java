package com.dev.jvh.takenote.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Subject;

import java.util.List;

/**
 * Created by JochemVanHespen on 10/7/2017.
 * Fills SubjectRecycleView with different Subjects
 */

class SubjectRecyclerAdapter extends
        RecyclerView.Adapter<SubjectRecyclerAdapter.SubjectViewHolder>

{
    private List<Subject> subjects;
    private Context context;
    private DomainController controller;


    SubjectRecyclerAdapter(List<Subject> subjects, Context context, DomainController controller) {
        this.subjects = subjects;
        this.context = context;
        this.controller = controller;
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
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent= new Intent(context,NoteActivity.class);
            intent.putExtra("controller",controller);
            intent.putExtra("idSubject", subjects.get(getLayoutPosition()).get_id());
            context.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(((SubjectActivity)context))
                            .toBundle());
        }

        @Override
        public boolean onLongClick(View v) {
            ((SubjectActivity) context).startDetailActivity(subjects.get(getLayoutPosition()).get_id());
            // returning true prevents onclick event to trigger
            return true;
        }
    }
}
