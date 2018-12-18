package com.av.m.sa3edny.ui.newRequest;

import android.support.multidex.MultiDexApplication;

import net.gotev.uploadservice.BuildConfig;
import net.gotev.uploadservice.UploadService;

/**
 * Created by Maiada on 5/3/2018.
 */

public class Initializer extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // setup the broadcast action namespace string which will
        // be used to notify upload status.
        // Gradle automatically generates proper variable as below.
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Or, you can define it manually.
        UploadService.NAMESPACE = "com.av.m.sa3edny";
    }

}
