package com.vinaysshenoy.rxtodo.data.store;

import android.support.v4.util.ArrayMap;

import com.vinaysshenoy.rxtodo.data.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class NotesStore {

    public static final Random NOTE_ID_GENERATOR = new Random();

    private final Map<String, Note> notes;

    public NotesStore() {
        this.notes = new ArrayMap<>((int) (16 * 1.33F));
    }

    public List<Note> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(notes.values()));
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
        note.setId(Long.toHexString(NOTE_ID_GENERATOR.nextLong()));
        notes.put(note.getId(), note);
    }
}
