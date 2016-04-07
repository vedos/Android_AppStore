package com.fit.vedads.appstore.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by vedad on 25.5.2015.
 */
public class MyGson {
    public static Gson build()
    {
        GsonBuilder builder = new GsonBuilder();
        return builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

    }
}
