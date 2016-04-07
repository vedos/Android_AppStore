package com.fit.vedads.appstore.helper;

import android.app.Application;
import android.content.Context;

/**
 * Created by vedad on 25.5.2015.
 */
public class MyApp extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
