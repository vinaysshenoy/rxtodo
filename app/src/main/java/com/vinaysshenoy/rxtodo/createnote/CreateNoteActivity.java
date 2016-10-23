package com.vinaysshenoy.rxtodo.createnote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.Relay;
import com.vinaysshenoy.rxtodo.R;
import com.vinaysshenoy.rxtodo.injection.Inject;
import com.vinaysshenoy.rxtodo.local.model.Note;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by vinaysshenoy on 23/10/16.
 */

public class CreateNoteActivity extends AppCompatActivity implements CreateNoteContract.View {

    private static final String TAG = "CreateNoteActivity";

    private CompositeSubscription subscriptions;

    private CreateNoteContract.UserActionsObserver presenter;

    private Relay<CreateNoteContract.CreateNoteEvent, CreateNoteContract.CreateNoteEvent> createNoteEventRelay;

    private EditText noteTextEditText;

    public static void start(@NonNull Context context) {
        context.startActivity(new Intent(context, CreateNoteActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CreateNotePresenter(Inject.get().noteStore());
        createNoteEventRelay = PublishRelay.<CreateNoteContract.CreateNoteEvent>create().toSerialized();
        setContentView(R.layout.view_create_note);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        noteTextEditText = (EditText) findViewById(R.id.textField_noteText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.action_done == item.getItemId()) {

            if (isEnteredNoteInfoValid()) {
                createNote();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNote() {
        createNoteEventRelay.call(new CreateNoteContract.CreateNoteEvent(noteTextEditText.getText().toString()));
    }

    private boolean isEnteredNoteInfoValid() {
        return !TextUtils.isEmpty(noteTextEditText.getText().toString());
    }

    @Override
    public Observable<CreateNoteContract.CreateNoteEvent> observeCreateNotes() {
        return createNoteEventRelay;
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscriptions = new CompositeSubscription();
        subscriptions.add(presenter.observeNoteCreated()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<Note>() {
                            @Override
                            public void call(Note note) {
                                handleNoteCreated(note);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Could not create Note!", throwable);
                            }
                        })
        );
        presenter.onViewReady(this);
    }

    private void handleNoteCreated(Note note) {
        Toast.makeText(CreateNoteActivity.this, getString(R.string.note_created, note.id()), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.unsubscribe();
        subscriptions.clear();
        presenter.onViewCleared();
    }
}
