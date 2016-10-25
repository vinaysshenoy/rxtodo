package com.vinaysshenoy.rxtodo.data.local.greendaostore;

import com.vinaysshenoy.rxtodo.local.Note;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by vinaysshenoy on 25/10/16.
 */
@Entity(
        nameInDb = "NOTES",
        generateConstructors = false
)
public class GreenDaoNote extends Note {

    @NotNull
    @Index(unique = true)
    String id;

    @NotNull
    String text;

    @NotNull
    Date created;

    @Id(autoincrement = true)
    private Long rowId;

    public GreenDaoNote() {
        rowId = null;
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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getRowId() {
        return this.rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }
}
