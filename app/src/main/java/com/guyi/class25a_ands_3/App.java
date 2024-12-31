package com.guyi.class25a_ands_3;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MSPS.init(this);
    }
}
