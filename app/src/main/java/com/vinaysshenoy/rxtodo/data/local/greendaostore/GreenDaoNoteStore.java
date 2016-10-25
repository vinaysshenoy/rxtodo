package com.vinaysshenoy.rxtodo.data.local.greendaostore;

import com.vinaysshenoy.rxtodo.local.Note;
import com.vinaysshenoy.rxtodo.local.NoteStore;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by vinaysshenoy on 25/10/16.
 */

public class GreenDaoNoteStore implements NoteStore {

    private final Random noteIdGenerator;
    private final GreenDaoNoteDao noteDao;
    private Query<GreenDaoNote> allNotesSortAscQuery;
    private Query<GreenDaoNote> allNotesSortDescQuery;
    private Query<GreenDaoNote> noteByIdQuery;

    public GreenDaoNoteStore(GreenDaoNoteDao noteDao) {
        noteIdGenerator = new Random();
        this.noteDao = noteDao;
    }

    private Query<GreenDaoNote> allNotesSortAscQuery() {

        if (allNotesSortAscQuery == null) {
            synchronized (this) {
                if (allNotesSortAscQuery == null) {
                    allNotesSortAscQuery = noteDao.queryBuilder().orderAsc(GreenDaoNoteDao.Properties.Created).build();
                }
            }
        }

        return allNotesSortAscQuery;
    }

    private Query<GreenDaoNote> allNotesSortDescuery() {

        if (allNotesSortDescQuery == null) {
            synchronized (this) {
                if (allNotesSortDescQuery == null) {
                    allNotesSortDescQuery = noteDao.queryBuilder().orderDesc(GreenDaoNoteDao.Properties.Created).build();
                }
            }
        }

        return allNotesSortDescQuery;
    }

    private Query<GreenDaoNote> noteByIdQuery(String id) {

        if (noteByIdQuery == null) {
            synchronized (this) {
                if (noteByIdQuery == null) {
                    noteByIdQuery = noteDao.queryBuilder().where(GreenDaoNoteDao.Properties.Id.eq(id)).build();
                }
            }
        }

        noteByIdQuery.setParameter(0, id);
        return noteByIdQuery;
    }

    @Override
    public Observable<List<Note>> allNotes(final boolean sortAscending) {
        return Observable.fromCallable(
                new Callable<List<Note>>() {
                    @Override
                    public List<Note> call() throws Exception {

                        final List<GreenDaoNote> greenDaoNotes = sortAscending ? allNotesSortAscQuery().list() : allNotesSortDescuery().list();
                        return new ArrayList<Note>(greenDaoNotes);

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
                        return noteByIdQuery(id).uniqueOrThrow();
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

                        final GreenDaoNote greenDaoNote = new GreenDaoNote();

                        greenDaoNote.setId(Long.toHexString(noteIdGenerator.nextLong()));
                        greenDaoNote.setText(text);
                        greenDaoNote.setCreated(created);

                        noteDao.insert(greenDaoNote);

                        return greenDaoNote;
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

                        noteDao.deleteAll();
                        return true;
                    }
                }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> delete(final Note note) {
        return Observable.fromCallable(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {

                        noteDao.delete((GreenDaoNote) note);
                        return true;
                    }
                }).subscribeOn(Schedulers.io());
    }
}
