package com.vinaysshenoy.rxtodo.injection;

import com.vinaysshenoy.rxtodo.data.store.NotesStoreImpl;
import com.vinaysshenoy.rxtodo.local.store.NoteStore;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class Inject {

    private static Inject instance;

    public static Inject get() {

        if(instance == null) {
            synchronized (Inject.class) {
                if(instance == null) {
                    instance = new Inject();
                }
            }
        }

        return instance;
    }

    private final NotesStoreImpl notesStoreImpl;

    private Inject() {
        notesStoreImpl = new NotesStoreImpl();
    }

    public NoteStore noteStore() {
        return notesStoreImpl;
    }
}
