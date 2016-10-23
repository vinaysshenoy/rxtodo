package com.vinaysshenoy.rxtodo.injection;

import com.vinaysshenoy.rxtodo.data.store.InMemoryNoteStore;
import com.vinaysshenoy.rxtodo.local.store.NoteStore;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class Inject {

    private static Inject instance;
    private final NoteStore noteStore;

    private Inject() {
        noteStore = new InMemoryNoteStore();
    }

    public static Inject get() {

        if (instance == null) {
            synchronized (Inject.class) {
                if (instance == null) {
                    instance = new Inject();
                }
            }
        }

        return instance;
    }

    public NoteStore noteStore() {
        return noteStore;
    }
}
