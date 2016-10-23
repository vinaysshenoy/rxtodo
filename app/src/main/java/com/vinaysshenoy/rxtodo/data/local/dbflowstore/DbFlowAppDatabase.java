package com.vinaysshenoy.rxtodo.data.local.dbflowstore;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by vinaysshenoy on 23/10/16.
 */
@Database(name = DbFlowAppDatabase.NAME, version = DbFlowAppDatabase.VERSION)
public class DbFlowAppDatabase {
    public static final String NAME = "DbFlowDatabase";
    public static final int VERSION = 1;
}
