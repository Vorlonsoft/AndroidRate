/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate.sample;

/* uncomment to test other locales - start */
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.os.Build;
/* uncomment to test other locales - end */
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.vorlonsoft.android.rate.AppCompatDialogManager;
import com.vorlonsoft.android.rate.AppRate;
import com.vorlonsoft.android.rate.DialogManager;
import com.vorlonsoft.android.rate.StoreType;

import androidx.appcompat.app.AppCompatActivity;

/* uncomment to test other locales */
//import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ANDROIDRATE_SAMPLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /* uncomment to test other locales - start */
        //if ((Build.VERSION.SDK_INT >= 17)&&(Build.VERSION.SDK_INT < 25)) {
        //    String mLang = "fr";    // change to your test language
        //    String mCountry = "FR"; // change to your test country
        //    Configuration mConfig;
        //    Locale mLocale;
        //
        //    if (mCountry.hashCode() == "".hashCode()) {
        //        mLocale = new Locale(mLang);
        //    } else {
        //        mLocale = new Locale(mLang, mCountry);
        //    }
        //    Locale.setDefault(mLocale);
        //
        //    mConfig = getBaseContext().getResources().getConfiguration();
        //    mConfig.setLocale(mLocale);
        //    mConfig.setLayoutDirection(mLocale);
        //
        //    Resources resources = getBaseContext().getResources();
        //    resources.updateConfiguration(mConfig, resources.getDisplayMetrics());
        //}
        /* uncomment to test other locales - end */

        /* comment if you don't want to test AppCompatDialogManager instead DefaultDialogManager */
        DialogManager.Factory appCompatDialogManagerFactory = new AppCompatDialogManager.Factory();

        AppRate.with(this)
                .setStoreType(StoreType.GOOGLEPLAY) /* default is GOOGLEPLAY (Google Play), other options are AMAZON (Amazon Appstore), BAZAAR (Cafe Bazaar),
                                                     *         CHINESESTORES (19 chinese app stores), MI (Mi Appstore (Xiaomi Market)), SAMSUNG (Samsung Galaxy Apps),
                                                     *         SLIDEME (SlideME Marketplace), TENCENT (Tencent App Store), YANDEX (Yandex.Store),
                                                     *         setStoreType(BLACKBERRY, long) (BlackBerry World, long - your application ID),
                                                     *         setStoreType(APPLE, long) (Apple App Store, long - your application ID),
                                                     *         setStoreType(String...) (Any other store/stores, String... - an URI or array of URIs to your app) and
                                                     *         setStoreType(Intent...) (Any custom intent/intents, Intent... - an intent or array of intents) */
                .setInstallDays((byte) 3)           // default is 10, 0 means install day, 10 means app is launched 10 or more days later than installation
                .setLaunchTimes((byte) 10)          // default is 10, 3 means app is launched 3 or more times
                .setRemindInterval((byte) 2)        // default is 1, 1 means app is launched 1 or more days after neutral button clicked
                .setRemindLaunchesNumber((byte) 1)  // default is 0, 1 means app is launched 1 or more times after neutral button clicked
                .setSelectedAppLaunches((byte) 4)   // default is 1, 1 means each launch, 2 means every 2nd launch, 3 means every 3rd launch, etc
                .setShowLaterButton(true)           // default is true, true means to show the Neutral button ("Remind me later").
                .set365DayPeriodMaxNumberDialogLaunchTimes((short) 3) // default is unlimited, 3 means 3 or less occurrences of the display of the Rate Dialog within a 365-day period
                .setVersionCodeCheck(true)          // default is false, true means to re-enable the Rate Dialog if a new version of app with different version code is installed
                .setVersionNameCheck(true)          // default is false, true means to re-enable the Rate Dialog if a new version of app with different version name is installed
                .setDebug(true)                     // default is false, true is for development only, true ensures that the Rate Dialog will be shown each time the app is launched
                .setCancelable(false)               // default false.
                .setOnClickButtonListener(which -> Log.d(TAG, "RateButton: " + Byte.toString(which)))
                /* uncomment to test AppCompatDialogManager instead DefaultDialogManager */
                //.setDialogManagerFactory(appCompatDialogManagerFactory)
                /* comment to use library strings instead app strings - start */
                .setTitle(R.string.new_rate_dialog_title)
                .setTextLater(R.string.new_rate_dialog_later)
                /* comment to use library strings instead app strings - end */
                /* uncomment to use app string instead library string */
                //.setMessage(R.string.new_rate_dialog_message)
                /* comment to use library strings instead app strings - start */
                .setTextNever(R.string.new_rate_dialog_never)
                .setTextRateNow(R.string.new_rate_dialog_ok)
                /* comment to use library strings instead app strings - end */
                .monitor();                         // Monitors the app launch times

        if (AppRate.with(this).getStoreType() == StoreType.GOOGLEPLAY) { // Checks that current app store type from library options is StoreType.GOOGLEPLAY
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) { // Checks that Google Play is available
                AppRate.showRateDialogIfMeetsConditions(this); // Shows the Rate Dialog when conditions are met
            }
        } else {
            AppRate.showRateDialogIfMeetsConditions(this);     // Shows the Rate Dialog when conditions are met
        }
    }
}