/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate.sample;

import android.app.Application;
import android.os.Process;

import com.squareup.leakcanary.LeakCanary;

@SuppressWarnings("WeakerAccess")
public class SampleApplication extends Application {

    public SampleApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //noinspection ConstantConditions
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        //noinspection ResultOfMethodCallIgnored
        LeakCanary.install(this);
        // Normal app init code...
    }

    @Override
    public void onTerminate() {
        System.exit(0);
        Process.killProcess(Process.myPid());

        super.onTerminate();
    }
}
