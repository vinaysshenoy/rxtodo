package com.vinaysshenoy.rxtodo.injection;

import com.vinaysshenoy.rxtodo.data.store.NotesStore;

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

    private final NotesStore notesStore;

    private Inject() {
        notesStore = new NotesStore();
    }

    public NotesStore notesStore() {
        return notesStore;
    }
}
