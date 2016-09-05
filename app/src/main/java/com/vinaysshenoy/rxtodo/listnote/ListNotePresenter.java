package com.vinaysshenoy.rxtodo.listnote;

import com.vinaysshenoy.rxtodo.data.Note;
import com.vinaysshenoy.rxtodo.data.store.NotesStore;

import java.util.List;

import rx.Observable;
import rx.Single;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class ListNotePresenter implements ListNoteContract.UserActionsObserver {

    private static final String TAG = "ListNotePresenter";

    private final NotesStore notesStore;
    private final Subject<ListNoteContract.ShowViewNoteEvent, ListNoteContract.ShowViewNoteEvent> showViewNoteEventSubject;
    private final Subject<List<Note>, List<Note>> notesListSubject;
    private SubscriptionList subscriptions;

    public ListNotePresenter(NotesStore notesStore) {
        this.notesStore = notesStore;
        showViewNoteEventSubject = new SerializedSubject<>(PublishSubject.<ListNoteContract.ShowViewNoteEvent>create());
        notesListSubject = new SerializedSubject<>(PublishSubject.<List<Note>>create());
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

        subscriptions = new SubscriptionList();
        subscriptions.add(
                view.observeClickedNote()
                        .observeOn(Schedulers.io()) //Execute on IO thread since there is DB access
                        .map(new Func1<String, Note>() {
                            @Override
                            public Note call(String noteId) {
                                return notesStore.getById(noteId);
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
                                showViewNoteEventSubject.onNext(new ListNoteContract.ShowViewNoteEvent(note));
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                showViewNoteEventSubject.onError(throwable);
                            }
                        }));

        displayNotes();
    }

    private void displayNotes() {

        Single.just(notesStore)
                .observeOn(Schedulers.io())
                .map(new Func1<NotesStore, List<Note>>() {
                    @Override
                    public List<Note> call(NotesStore notesStore) {
                        return notesStore.getAll(false);
                    }
                })
                .subscribe(new Action1<List<Note>>() {
                    @Override
                    public void call(List<Note> notes) {
                        notesListSubject.onNext(notes);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        notesListSubject.onError(throwable);
                    }
                });
    }

    @Override
    public void onViewCleared() {

        subscriptions.unsubscribe();
        subscriptions.clear();
    }
}
