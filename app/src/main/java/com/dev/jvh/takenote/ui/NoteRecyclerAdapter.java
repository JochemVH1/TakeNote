package com.dev.jvh.takenote.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.Note;

import java.util.List;

/**
 * Created by JochemVanHespen on 10/9/2017.
 * Fills NoteRecycleView with different Notes
 */

class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder> {
    private List<Note> notes;
    private OnNoteClicked onNoteClicked;

    public interface OnNoteClicked{
        void onClick(Note note);
    }

    NoteRecyclerAdapter(List<Note> notes, OnNoteClicked onNoteClicked) {
        this.notes = notes;
        this.onNoteClicked = onNoteClicked;
    }

    void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_card_view,parent,false);
        return new NoteRecyclerAdapter.NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
        holder.content.setText(notes.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView content;
        NoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteClicked.onClick(notes.get(getLayoutPosition()));
        }
    }
}
