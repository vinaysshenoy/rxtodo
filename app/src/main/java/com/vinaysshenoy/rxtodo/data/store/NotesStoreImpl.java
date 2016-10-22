package com.vinaysshenoy.rxtodo.data.store;

import com.vinaysshenoy.rxtodo.data.NoteImpl;
import com.vinaysshenoy.rxtodo.local.model.Note;
import com.vinaysshenoy.rxtodo.local.store.NoteStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class NotesStoreImpl implements NoteStore {

    public static final Random NOTE_ID_GENERATOR = new Random();

    private final Map<String, NoteImpl> notes;

    public NotesStoreImpl() {
        this.notes = new ConcurrentHashMap<>((int) (16 * 1.33F));
    }

    @Override
    public Observable<List<Note>> allNotes(final boolean sortAscending) {

        return Observable.fromCallable(
                new Callable<List<Note>>() {
                    @Override
                    public List<Note> call() throws Exception {
                        final List<Note> notes = new ArrayList<Note>(NotesStoreImpl.this.notes.values());
                        if (sortAscending) {
                            //Oldest dates first
                            Collections.sort(notes);
                        } else {
                            //Newest dates first
                            Collections.sort(notes, Collections.<Note>reverseOrder());
                        }
                        return Collections.unmodifiableList(notes);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Note> findById(final String id) {
        return Observable.fromCallable(
                new Callable<Note>() {
                    @Override
                    public Note call() throws Exception {
                        return notes.get(id);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Note> create(final String text, final Date created) {
        return Observable.fromCallable(
                new Callable<Note>() {
                    @Override
                    public Note call() throws Exception {

                        final NoteImpl note = new NoteImpl();
                        note.setId(Long.toHexString(NOTE_ID_GENERATOR.nextLong()));
                        note.setText(text);
                        note.setCreated(created);

                        notes.put(note.id(), note);
                        return note;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.fromCallable(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {

                        notes.clear();
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> delete(final Note note) {
        return Observable.fromCallable(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {

                        notes.remove(note.id());
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

}
