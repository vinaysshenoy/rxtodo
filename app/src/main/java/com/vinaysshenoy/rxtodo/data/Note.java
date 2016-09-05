package com.vinaysshenoy.rxtodo.data;

import java.util.Date;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class Note {

    public String id;

    public String text;

    public Date created;

    public String getId() {
        return id;
    }

    public Note setId(String id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public Note setText(String text) {
        this.text = text;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public Note setCreated(Date created) {
        this.created = created;
        return this;
    }
}
