package com.vinaysshenoy.rxtodo.listnote;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinaysshenoy.rxtodo.R;
import com.vinaysshenoy.rxtodo.local.model.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class ListNoteAdapter extends RecyclerView.Adapter<ListNoteAdapter.ListNoteViewHolder>  {

    private List<Note> notes;

    private OnNoteClicked onNoteClicked;

    public ListNoteAdapter(List<Note> notes) {
        this.notes = notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public void setOnNoteClicked(OnNoteClicked onNoteClicked) {
        this.onNoteClicked = onNoteClicked;
    }

    @Override
    public ListNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListNoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(ListNoteViewHolder holder, int position) {
        holder.bindNode(notes.get(position), onNoteClicked);
    }

    @Override
    public int getItemCount() {
        return notes == null ? 0 : notes.size();
    }

    public interface OnNoteClicked {

        void clicked(Note note);
    }

    public static final class ListNoteViewHolder extends RecyclerView.ViewHolder {

        private static final String DATE_FORMAT_STRING = "EEE, MMM d, yyyy";

        private final TextView noteTextView;

        private final TextView dateTextView;

        private final DateFormat dateFormat;

        private Note note;

        private OnNoteClicked onNoteClicked;

        public ListNoteViewHolder(View itemView) {
            super(itemView);
            noteTextView = (TextView) itemView.findViewById(R.id.tv_noteText);
            dateTextView = (TextView) itemView.findViewById(R.id.tv_noteDate);
            dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(note != null && onNoteClicked != null) {
                        onNoteClicked.clicked(note);
                    }
                }
            });
        }

        public void bindNode(Note note, OnNoteClicked onNoteClicked) {
            this.note = note;
            this.onNoteClicked = onNoteClicked;
            noteTextView.setText(note.text());
            dateTextView.setText(dateFormat.format(note.created()));
        }
    }
}
