package com.vinaysshenoy.rxtodo.injection;

import android.content.Context;

import com.vinaysshenoy.rxtodo.data.local.greendaostore.DaoMaster;
import com.vinaysshenoy.rxtodo.data.local.greendaostore.DaoSession;
import com.vinaysshenoy.rxtodo.data.local.greendaostore.GreenDaoNoteStore;
import com.vinaysshenoy.rxtodo.local.NoteStore;

/**
 * Created by vinaysshenoy on 05/09/16.
 */
public class Inject {

    private static Context appContext;
    private static Inject instance;
    private final NoteStore noteStore;


    private Inject() {

        final DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(appContext, "greendao_db");
        final DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDb());
        final DaoSession daoSession = daoMaster.newSession();

        noteStore = new GreenDaoNoteStore(daoSession.getGreenDaoNoteDao());
    }

    public static void init(Context context) {
        appContext = context.getApplicationContext();
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
