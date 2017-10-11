package com.dev.jvh.takenote.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.jvh.takenote.R;
import com.dev.jvh.takenote.domain.DomainController;
import com.dev.jvh.takenote.domain.Note;

import java.util.List;

/**
 * Created by JochemVanHespen on 10/9/2017.
 * Fills NoteRecycleView with different Notes
 */

class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder> {
    private List<Note> notes;
    private Context context;
    private DomainController controller;

    NoteRecyclerAdapter(List<Note> notes, Context context, DomainController controller) {
        this.notes = notes;
        this.context = context;
        this.controller = controller;
    }

    public void setNotes(List<Note> notes) {
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
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;

        NoteViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Note note = notes.get(getLayoutPosition());
            Intent intent = new Intent(context,CreateNoteActivity.class);
            intent.putExtra("_id",String.valueOf(note.get_id()));
            intent.putExtra("title",note.getTitle());
            intent.putExtra("text",note.getText());
            ((NoteActivity) context).startActivityForResult(intent,NoteActivity.REQUEST_CODE + 1);
        }
    }
}
