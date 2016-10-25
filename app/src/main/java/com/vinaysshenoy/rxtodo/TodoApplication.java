package com.vinaysshenoy.rxtodo;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.vinaysshenoy.rxtodo.injection.Inject;

/**
 * Created by vinaysshenoy on 23/10/16.
 */

public class TodoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(TodoApplication.this).build());
        Inject.init(this);
    }
}
