package com.vinaysshenoy.rxtodo.createnote;

import com.vinaysshenoy.rxtodo.RxSchedulersOverrideRule;
import com.vinaysshenoy.rxtodo.local.Note;
import com.vinaysshenoy.rxtodo.local.NoteStore;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by vinaysshenoy on 23/10/16.
 */
public class CreateNotePresenterTest {

    @Rule
    public final RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();
    @Mock
    NoteStore noteStore;
    @Mock
    CreateNoteContract.View view;
    @Mock
    Note note;
    private CreateNotePresenter presenter;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        when(view.observeCreateNotes()).thenReturn(Observable.<CreateNoteContract.CreateNoteEvent>never());
        when(noteStore.create(anyString(), any(Date.class))).thenReturn(Observable.fromCallable(new Callable<Note>() {
            @Override
            public Note call() throws Exception {
                return note;
            }
        }));

        presenter = new CreateNotePresenter(noteStore);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testNoteEventThrownOnNoteCreated() throws Exception {

        final TestSubscriber<Note> testSubscriber = TestSubscriber.create();
        presenter.observeNoteCreated().subscribe(testSubscriber);
        presenter.createNote("Some Text");
        testSubscriber.assertValueCount(1);
    }
}