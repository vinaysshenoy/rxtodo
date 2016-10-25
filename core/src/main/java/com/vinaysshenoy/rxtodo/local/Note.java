package com.vinaysshenoy.rxtodo.local;

import java.util.Date;

/**
 * Created by vinaysshenoy on 22/10/16.
 */

public abstract class Note implements Comparable<Note> {

    public abstract String id();

    public abstract String text();

    public abstract Date created();

    @Override
    public final int compareTo(Note note) {
        return created().compareTo(note.created());
    }
}
