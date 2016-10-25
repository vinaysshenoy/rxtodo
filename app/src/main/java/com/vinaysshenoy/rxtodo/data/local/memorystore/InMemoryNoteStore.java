package com.vinaysshenoy.rxtodo.data.local.memorystore;

import com.vinaysshenoy.rxtodo.local.Note;
import com.vinaysshenoy.rxtodo.local.NoteStore;

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
public class InMemoryNoteStore implements NoteStore {

    private final Random noteIdGenerator = new Random();

    private final Map<String, InMemoryNote> notes;

    public InMemoryNoteStore() {
        this.notes = new ConcurrentHashMap<>((int) (16 * 1.33F));
    }

    @Override
    public Observable<List<Note>> allNotes(final boolean sortAscending) {

        return Observable.fromCallable(
                new Callable<List<Note>>() {
                    @Override
                    public List<Note> call() throws Exception {
                        final List<Note> notes = new ArrayList<Note>(InMemoryNoteStore.this.notes.values());
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

                        final InMemoryNote note = new InMemoryNote();
                        note.id = Long.toHexString(noteIdGenerator.nextLong());
                        note.text = text;
                        note.created = created;

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
