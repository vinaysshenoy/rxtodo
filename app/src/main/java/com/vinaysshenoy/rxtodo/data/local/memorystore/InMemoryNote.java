package com.vinaysshenoy.rxtodo.data.local.memorystore;

import com.vinaysshenoy.rxtodo.local.model.Note;

import java.util.Date;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class InMemoryNote extends Note {

    private String id;

    private String text;

    private Date created;


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

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }
}
