package com.vinaysshenoy.rxtodo.data.local.memorystore;

import com.vinaysshenoy.rxtodo.local.Note;

import java.util.Date;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class InMemoryNote extends Note {

    String id;

    String text;

    Date created;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public Date created() {
        return created;
    }
}
