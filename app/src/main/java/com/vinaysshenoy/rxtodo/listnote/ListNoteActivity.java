package com.vinaysshenoy.rxtodo.listnote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vinaysshenoy.rxtodo.R;
import com.vinaysshenoy.rxtodo.data.Note;
import com.vinaysshenoy.rxtodo.injection.Inject;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.internal.util.SubscriptionList;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class ListNoteActivity extends AppCompatActivity implements ListNoteContract.View {

    private static final String TAG = "ListNoteActivity";

    //This should ideally be injected using a DI framework
    private ListNoteContract.UserActionsObserver presenter;

    private Subject<String, String> clickedNotesSubject;

    private RecyclerView notesRecyclerView;

    private ListNoteAdapter listNoteAdapter;

    private SubscriptionList subscriptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ListNotePresenter(Inject.get().notesStore());
        clickedNotesSubject = new SerializedSubject<>(PublishSubject.<String>create());
        setContentView(R.layout.view_listnotes);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        findViewById(R.id.action_addNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add Note screen
            }
        });
        notesRecyclerView = (RecyclerView) findViewById(R.id.list_notes);
        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(ListNoteActivity.this, LinearLayoutManager.VERTICAL, false));

        listNoteAdapter = new ListNoteAdapter(null);
        listNoteAdapter.setOnNoteClicked(new ListNoteAdapter.OnNoteClicked() {
            @Override
            public void clicked(Note note) {
                clickedNotesSubject.onNext(note.getId());
            }
        });

        notesRecyclerView.setAdapter(listNoteAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        subscriptions = new SubscriptionList();

        subscriptions.add(presenter.observeDisplayNotes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Note>>() {
                    @Override
                    public void call(List<Note> notes) {
                        listNoteAdapter.setNotes(notes);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "Error fetching notes", throwable);
                    }
                }));

        subscriptions.add(presenter.observeShowViewNote()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListNoteContract.ShowViewNoteEvent>() {
                    @Override
                    public void call(ListNoteContract.ShowViewNoteEvent showViewNoteEvent) {
                        Toast.makeText(ListNoteActivity.this, getString(R.string.show_note, showViewNoteEvent.note.getId()), Toast.LENGTH_SHORT).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "Error Showing View Note", throwable);
                    }
                }));

        presenter.onViewReady(ListNoteActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.unsubscribe();
        subscriptions.clear();
    }

    @Override
    public Observable<String> observeClickedNote() {
        return clickedNotesSubject;
    }
}
