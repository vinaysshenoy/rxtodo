package com.vinaysshenoy.rxtodo.data.local.dbflowstore;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.Model;
import com.vinaysshenoy.rxtodo.local.model.Note;

import java.util.Date;

/**
 * Created by vinaysshenoy on 23/10/16.
 */
@Table(database = DbFlowAppDatabase.class, name = "NOTES")
public class DbFlowNote extends Note implements Model {

    @PrimaryKey
    @Column(name = "id")
    String id;

    @NotNull
    @Column(name = "text")
    String text;

    @NotNull
    @Column(name = "created")
    Date created;

    @Override
    public void save() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exists() {
        throw new UnsupportedOperationException();
    }

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
