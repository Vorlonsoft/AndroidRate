/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate.sample;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.vorlonsoft.android.rate.AppRate;
import com.vorlonsoft.android.rate.OnClickButtonListener;
import com.vorlonsoft.android.rate.StoreType;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends Activity {
    private static final String TAG = "ANDROIDRATE_SAMPLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /*
        // uncomment to test other locales
        if ((Build.VERSION.SDK_INT >= 17)&&(Build.VERSION.SDK_INT < 25)) {
            String mLang = "fr";    // change to your test language
            String mCountry = "FR"; // change to your test country
            Configuration mConfig;
            Locale mLocale;

            if (mCountry.hashCode() == "".hashCode()) {
                mLocale = new Locale(mLang);
            } else {
                mLocale = new Locale(mLang, mCountry);
            }
            Locale.setDefault(mLocale);

            mConfig = getBaseContext().getResources().getConfiguration();
            mConfig.setLocale(mLocale);
            mConfig.setLayoutDirection(mLocale);

            Resources resources = getBaseContext().getResources();
            resources.updateConfiguration(mConfig, resources.getDisplayMetrics());
        }
        */


        AppRate.with(this)
                .setStoreType(StoreType.GOOGLEPLAY) //default is GOOGLEPLAY, other option is AMAZON
                .setInstallDays(3) // default 10, 0 means install day.
                .setLaunchTimes(10) // default 10 times.
                .setRemindInterval(2) // default 1 day.
                .setRemindLaunchTimes (4) // default 1 (each launch).
                .setShowLaterButton(true) // default true.
                .setDebug(true) // default false.
                .setCancelable(false) // default false.
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                // comment to use library strings instead app strings
                .setTitle(R.string.new_rate_dialog_title)
                .setTextLater(R.string.new_rate_dialog_later)
                // uncomment to use app string instead library string
                //.setMessage(R.string.new_rate_dialog_message)
                // comment to use library strings instead app strings
                .setTextNever(R.string.new_rate_dialog_never)
                .setTextRateNow(R.string.new_rate_dialog_ok)
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);
    }
}