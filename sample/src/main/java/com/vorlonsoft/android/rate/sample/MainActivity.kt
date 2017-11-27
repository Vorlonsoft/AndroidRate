/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate.sample

import android.app.Activity
import android.os.Bundle

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

import com.vorlonsoft.android.rate.AppRate
import com.vorlonsoft.android.rate.StoreType

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        /*
        // uncomment to test other locales
        if (Build.VERSION.SDK_INT in 17..24) {
            val mLang = "fr"    // change to your test language
            val mCountry = "FR" // change to your test country
            val mConfig: Configuration
            val mLocale: Locale

            if (mCountry.hashCode() == "".hashCode()) {
                mLocale = Locale(mLang)
            } else {
                mLocale = Locale(mLang, mCountry)
            }
            Locale.setDefault(mLocale)

            mConfig = baseContext.resources.configuration
            mConfig.setLocale(mLocale)
            mConfig.setLayoutDirection(mLocale)

            val resources = baseContext.resources
            resources.updateConfiguration(mConfig, resources.displayMetrics)
        }
        */

        val storeType = StoreType.GOOGLEPLAY // options: GOOGLEPLAY or AMAZON

        AppRate.with(this)
                ?.setStoreType(storeType) // default is GOOGLEPLAY, other option is AMAZON
                ?.setInstallDays(3) // default 10, 0 means install day.
                ?.setLaunchTimes(10) // default 10 times.
                ?.setRemindInterval(2) // default 1 day.
                ?.setRemindLaunchTimes(4) // default 1 (each launch).
                ?.setShowLaterButton(true) // default true.
                ?.setDebug(true) // default false.
                ?.setCancelable(false) // default false.
                // comment to use Kotlin library strings instead app strings
                ?.setTitle(R.string.new_rate_dialog_title)
                ?.setTextLater(R.string.new_rate_dialog_later)
                // uncomment to use app string instead Kotlin library string
                //?.setMessage(R.string.new_rate_dialog_message)
                // comment to use Kotlin library strings instead app strings
                ?.setTextNever(R.string.new_rate_dialog_never)
                ?.setTextRateNow(R.string.new_rate_dialog_ok)
                ?.monitor()

        if (storeType == StoreType.GOOGLEPLAY) {
            //Check that Google Play is available
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) {
                // Show a dialog if meets conditions
                AppRate.showRateDialogIfMeetsConditions(this)
            }
        } else {
            // Show a dialog if meets conditions
            AppRate.showRateDialogIfMeetsConditions(this)
        }
    }

    companion object {
        private val TAG = "ANDROIDRATEKOTLIN_SAMPLE"
    }
}