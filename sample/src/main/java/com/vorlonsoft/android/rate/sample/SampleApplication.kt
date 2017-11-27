/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate.sample

import android.app.Application
import android.os.Process

import com.squareup.leakcanary.LeakCanary

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        // Normal app init code...
    }

    override fun onTerminate() {
        System.exit(0)
        Process.killProcess(Process.myPid())

        super.onTerminate()
    }
}
