package com.vinaysshenoy.rxtodo.createnote;

import com.vinaysshenoy.rxtodo.local.model.Note;

import rx.Observable;

/**
 * Created by vinaysshenoy on 23/10/16.
 */

public interface CreateNoteContract {

    interface View {

        Observable<CreateNoteEvent> observeCreateNotes();
    }

    interface UserActionsObserver {

        Observable<Note> observeNoteCreated();

        void onViewReady(View view);

        void onViewCleared();
    }

    final class CreateNoteEvent {

        public final String text;

        public CreateNoteEvent(String text) {
            this.text = text;
        }
    }
}
