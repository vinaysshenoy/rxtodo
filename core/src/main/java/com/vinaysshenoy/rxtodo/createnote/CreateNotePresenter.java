package com.vinaysshenoy.rxtodo.createnote;

import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.Relay;
import com.vinaysshenoy.rxtodo.local.model.Note;
import com.vinaysshenoy.rxtodo.local.store.NoteStore;

import java.util.Date;

import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by vinaysshenoy on 23/10/16.
 */

public class CreateNotePresenter implements CreateNoteContract.UserActionsObserver {

    private final NoteStore noteStore;
    private final Relay<Note, Note> noteCreatedRelay;
    private CompositeSubscription subscriptions;

    public CreateNotePresenter(NoteStore noteStore) {
        this.noteStore = noteStore;
        noteCreatedRelay = PublishRelay.<Note>create().toSerialized();
    }

    @Override
    public Observable<Note> observeNoteCreated() {
        return noteCreatedRelay;
    }

    @Override
    public void onViewReady(CreateNoteContract.View view) {

        subscriptions = new CompositeSubscription();
        subscriptions.add(
                view.observeCreateNotes()
                        .subscribe(new Action1<CreateNoteContract.CreateNoteEvent>() {
                            @Override
                            public void call(CreateNoteContract.CreateNoteEvent createNoteEvent) {
                                createNote(createNoteEvent.text);
                            }
                        })
        );

    }

    @Override
    public void onViewCleared() {

        subscriptions.unsubscribe();
        subscriptions.clear();
    }

    void createNote(String text) {

        noteStore.create(text, new Date())
                .subscribe(new Action1<Note>() {
                    @Override
                    public void call(Note note) {
                        noteCreatedRelay.call(note);
                    }
                });
    }
}
