package com.vinaysshenoy.rxtodo.listnote;

import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.Relay;
import com.vinaysshenoy.rxtodo.local.Note;
import com.vinaysshenoy.rxtodo.local.NoteStore;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class ListNotePresenter implements ListNoteContract.UserActionsObserver {

    private static final String TAG = "ListNotePresenter";

    private final NoteStore notesStore;
    private final Relay<ListNoteContract.ShowViewNoteEvent, ListNoteContract.ShowViewNoteEvent> showViewNoteEventSubject;
    private final Relay<List<Note>, List<Note>> notesListSubject;
    private CompositeSubscription subscriptions;

    public ListNotePresenter(NoteStore notesStore) {
        this.notesStore = notesStore;
        showViewNoteEventSubject = PublishRelay.<ListNoteContract.ShowViewNoteEvent>create().toSerialized();
        notesListSubject = PublishRelay.<List<Note>>create().toSerialized();
    }

    @Override
    public Observable<List<Note>> observeDisplayNotes() {
        return notesListSubject;
    }

    @Override
    public Observable<ListNoteContract.ShowViewNoteEvent> observeShowViewNote() {
        return showViewNoteEventSubject;
    }

    @Override
    public void onViewReady(ListNoteContract.View view) {

        subscriptions = new CompositeSubscription();

        subscriptions.add(
                view.observeOpenNoteWithId()
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String noteId) {
                                showNoteWithId(noteId);
                            }
                        })
        );

        displayNotes();
    }

    void showNoteWithId(String noteId) {

        Observable.just(noteId)
                .flatMap(new Func1<String, Observable<Note>>() {
                    @Override
                    public Observable<Note> call(String noteId) {
                        return notesStore.findById(noteId);
                    }
                })
                .filter(new Func1<Note, Boolean>() {
                    @Override
                    public Boolean call(Note note) {
                        return note != null;
                    }
                })
                .subscribe(new Action1<Note>() {
                    @Override
                    public void call(Note note) {
                        showViewNoteEventSubject.call(new ListNoteContract.ShowViewNoteEvent(note));
                    }
                });
    }

    void displayNotes() {

        notesStore.allNotes(false)
                .filter(new Func1<List<Note>, Boolean>() {
                    @Override
                    public Boolean call(List<Note> notes) {
                        return notes != null && !notes.isEmpty();
                    }
                })
                .subscribe(new Action1<List<Note>>() {
                    @Override
                    public void call(List<Note> notes) {
                        notesListSubject.call(notes);
                    }
                });
    }

    @Override
    public void onViewCleared() {

        subscriptions.unsubscribe();
        subscriptions.clear();
    }
}
