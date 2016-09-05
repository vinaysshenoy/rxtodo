package com.vinaysshenoy.rxtodo.data.store;

import android.support.v4.util.ArrayMap;

import com.vinaysshenoy.rxtodo.data.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class NotesStore {

    public static final Random NOTE_ID_GENERATOR = new Random();

    private final Map<String, Note> notes;

    public NotesStore() {
        this.notes = new ArrayMap<>((int) (16 * 1.33F));
        initializeDummyData();
    }

    private void initializeDummyData() {

        final Date now = new Date();
        final Note note1 = new Note().setCreated(new Date(now.getTime() - TimeUnit.DAYS.toMillis(10L))).setText("This is the very first note!");
        final Note note2 = new Note().setCreated(new Date(now.getTime() - TimeUnit.DAYS.toMillis(5L))).setText("This is the second note!!!");
        final Note note3 = new Note().setCreated(now).setText("Last note! :-(");

        add(note1);
        add(note2);
        add(note3);
    }

    public List<Note> getAll(boolean ascending) {

        final ArrayList<Note> notesList = new ArrayList<>(this.notes.values());

        if (ascending) {
            //Oldest dates first
            Collections.sort(notesList);
        } else {
            //Newest dates first
            Collections.sort(notesList, Collections.<Note>reverseOrder());
        }
        return notesList;
    }

    public Note getById(String id) {
        return notes.get(id);
    }

    public void deleteAll() {
        notes.clear();
    }

    public void delete(String id) {
        notes.remove(id);
    }

    public void add(Note note) {

        if (note.getId() == null) {
            note.setId(Long.toHexString(NOTE_ID_GENERATOR.nextLong()));
        }
        notes.put(note.getId(), note);
    }

}
