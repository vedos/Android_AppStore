package com.fit.vedads.appstore.helper;

import java.io.Serializable;

/**
 * Created by vedad on 25.5.2015.
 */
public interface MyRunnable<T> extends Serializable{
    void run(T result);
}
