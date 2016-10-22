package com.vinaysshenoy.rxtodo.listnote;

import com.vinaysshenoy.rxtodo.local.model.Note;
import com.vinaysshenoy.rxtodo.local.store.NoteStore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vinaysshenoy on 22/10/16.
 */
public class ListNotePresenterTest {

    @Mock
    NoteStore noteStore;
    @Mock
    ListNoteContract.View view;
    private ListNotePresenter presenter;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        presenter = new ListNotePresenter(noteStore);
        when(view.observeOpenNoteWithId()).thenReturn(Observable.<String>never());
        when(noteStore.allNotes(anyBoolean())).thenReturn(Observable.<List<Note>>never());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAllNotesCalledOnViewReady() throws Exception {
        presenter.onViewReady(view);
        verify(noteStore, times(1)).allNotes(anyBoolean());
    }
}