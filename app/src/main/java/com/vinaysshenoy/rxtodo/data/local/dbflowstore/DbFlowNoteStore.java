package com.vinaysshenoy.rxtodo.data.local.dbflowstore;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.vinaysshenoy.rxtodo.local.model.Note;
import com.vinaysshenoy.rxtodo.local.store.NoteStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by vinaysshenoy on 23/10/16.
 */

public class DbFlowNoteStore implements NoteStore {

    private final Random noteIdGenerator;
    private final ModelAdapter<DbFlowNote> modelAdapter;

    public DbFlowNoteStore() {
        noteIdGenerator = new Random();
        modelAdapter = FlowManager.getModelAdapter(DbFlowNote.class);
    }

    @Override
    public Observable<List<Note>> allNotes(final boolean sortAscending) {

        return Observable.fromCallable(
                new Callable<List<DbFlowNote>>() {
                    @Override
                    public List<DbFlowNote> call() throws Exception {

                        return SQLite.select()
                                .from(DbFlowNote.class)
                                .orderBy(DbFlowNote_Table.created, sortAscending)
                                .queryList();


                    }
                })
                .observeOn(Schedulers.computation())
                .map(new Func1<List<DbFlowNote>, List<Note>>() {
                    @Override
                    public List<Note> call(List<DbFlowNote> dbFlowNotes) {

                        if (dbFlowNotes.isEmpty()) {
                            return Collections.emptyList();
                        }

                        final List<Note> notes = new ArrayList<>(dbFlowNotes.size());
                        for (DbFlowNote dbFlowNote : dbFlowNotes) {
                            notes.add(dbFlowNote);
                        }

                        return notes;
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
                        return SQLite.select()
                                .from(DbFlowNote.class)
                                .where(DbFlowNote_Table.id.eq(id))
                                .querySingle();
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

                        final DbFlowNote dbFlowNote = new DbFlowNote();
                        dbFlowNote.setId(Long.toHexString(noteIdGenerator.nextLong()));
                        dbFlowNote.setText(text);
                        dbFlowNote.setCreated(created);

                        modelAdapter.save(dbFlowNote);

                        return dbFlowNote;
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

                        Delete.table(DbFlowNote.class);
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

                        Delete.table(DbFlowNote.class, DbFlowNote_Table.id.eq(note.id()));
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

}
