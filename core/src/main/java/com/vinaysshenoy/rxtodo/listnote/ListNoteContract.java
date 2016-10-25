package com.vinaysshenoy.rxtodo.listnote;


import com.vinaysshenoy.rxtodo.local.Note;

import java.util.List;

import rx.Observable;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public interface ListNoteContract {

    interface View {

        Observable<String> observeOpenNoteWithId();

    }

    interface UserActionsObserver {

        Observable<List<Note>> observeDisplayNotes();

        Observable<ShowViewNoteEvent> observeShowViewNote();

        void onViewReady(View view);

        void onViewCleared();
    }

    final class ShowViewNoteEvent {

        final Note note;

        public ShowViewNoteEvent(Note note) {
            this.note = note;
        }
    }

}
