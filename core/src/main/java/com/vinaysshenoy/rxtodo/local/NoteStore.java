package com.vinaysshenoy.rxtodo.local;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by vinaysshenoy on 22/10/16.
 */

public interface NoteStore {

    Observable<List<Note>> allNotes(boolean sortAscending);

    Observable<Note> findById(String id);

    Observable<Note> create(String text, Date created);

    Observable<Boolean> deleteAll();

    Observable<Boolean> delete(Note note);
}
