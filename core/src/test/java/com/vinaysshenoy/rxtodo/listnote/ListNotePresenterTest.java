package com.vinaysshenoy.rxtodo.listnote;

import com.vinaysshenoy.rxtodo.RxSchedulersOverrideRule;
import com.vinaysshenoy.rxtodo.local.model.Note;
import com.vinaysshenoy.rxtodo.local.store.NoteStore;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by vinaysshenoy on 22/10/16.
 */
public class ListNotePresenterTest {

    @Rule
    public final RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock
    NoteStore noteStore;
    @Mock
    ListNoteContract.View view;
    @Mock
    Note note;
    private ListNotePresenter presenter;
    private NoteStoreListNotesAnswer noteStoreListNotesAnswer;
    private NoteStoreFindByIdAnswer noteStoreFindByIdAnswer;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        presenter = new ListNotePresenter(noteStore);
        noteStoreListNotesAnswer = new NoteStoreListNotesAnswer();
        noteStoreFindByIdAnswer = new NoteStoreFindByIdAnswer();
        when(view.observeOpenNoteWithId()).thenReturn(Observable.<String>never());
        when(noteStore.allNotes(anyBoolean())).thenAnswer(noteStoreListNotesAnswer);
        when(noteStore.findById(anyString())).thenAnswer(noteStoreFindByIdAnswer);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testNotesEventThrownWhenDisplayNotesCalled() throws Exception {

        noteStoreListNotesAnswer.notes = Collections.singletonList(note);

        final TestSubscriber<List<Note>> testSubscriber = TestSubscriber.create();
        presenter.observeDisplayNotes().subscribe(testSubscriber);

        presenter.displayNotes();
        testSubscriber.assertValueCount(1);
        final List<Note> notes = testSubscriber.getOnNextEvents().get(0);
        assertEquals(1, notes.size());
        assertSame(note, notes.get(0));

    }

    @Test
    public void testNotesEventNotThrownWhenNotesStoreHasNoNotes() throws Exception {

        noteStoreListNotesAnswer.notes = null;
        final TestSubscriber<List<Note>> testSubscriber = TestSubscriber.create();
        presenter.observeDisplayNotes().subscribe(testSubscriber);

        presenter.displayNotes();
        testSubscriber.assertValueCount(0);

    }

    @Test
    public void testShotNoteEventThrownOnFindNoteById() throws Exception {

        noteStoreFindByIdAnswer.note = note;
        noteStoreFindByIdAnswer.noteIdToMatch = "note";

        final TestSubscriber<ListNoteContract.ShowViewNoteEvent> testSubscriber = TestSubscriber.create();
        presenter.observeShowViewNote().subscribe(testSubscriber);

        presenter.showNoteWithId("note");
        testSubscriber.assertValueCount(1);
        final ListNoteContract.ShowViewNoteEvent showViewNoteEvent = testSubscriber.getOnNextEvents().get(0);
        assertSame(note, showViewNoteEvent.note);
    }

    @Test
    public void testShotNoteEventNotThrownOnFindNoteByIdFail() throws Exception {

        noteStoreFindByIdAnswer.note = note;
        noteStoreFindByIdAnswer.noteIdToMatch = "note2";

        final TestSubscriber<ListNoteContract.ShowViewNoteEvent> testSubscriber = TestSubscriber.create();
        presenter.observeShowViewNote().subscribe(testSubscriber);

        presenter.showNoteWithId("note");
        testSubscriber.assertValueCount(0);
    }

    private static final class NoteStoreListNotesAnswer implements Answer<Observable<List<Note>>> {

        public List<Note> notes;

        @Override
        public Observable<List<Note>> answer(InvocationOnMock invocation) throws Throwable {
            return notes != null ? Observable.just(notes) : Observable.just(Collections.<Note>emptyList());
        }
    }

    private static final class NoteStoreFindByIdAnswer implements Answer<Observable<Note>> {

        public String noteIdToMatch;

        public Note note;

        @Override
        public Observable<Note> answer(InvocationOnMock invocation) throws Throwable {

            final String noteId = invocation.getArgument(0);
            return noteId.equals(noteIdToMatch) ? Observable.just(note) : Observable.<Note>just(null);
        }
    }

}