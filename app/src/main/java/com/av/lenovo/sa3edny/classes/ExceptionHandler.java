package com.av.lenovo.sa3edny.classes;

import android.content.Context;

/**
 * Created by Mina on 4/9/2017.
 */

public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {

    Context context;

    public ExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
       e.printStackTrace();
    }
}
