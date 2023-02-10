/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */
package com.vorlonsoft.android.rate.sample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.vorlonsoft.android.rate.*

/* uncomment to test other locales - start */ //import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.os.Build;
/* uncomment to test other locales - end */ /* uncomment to test other locales */ //import java.util.Locale;
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (AppRate.with(this).getStoreType() == StoreType.GOOGLEPLAY) { // Checks that current app store type from library options is StoreType.GOOGLEPLAY
            if (GoogleApiAvailability.getInstance()
                    .isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING
            ) { // Checks that Google Play is available
                lifecycleScope.launchWhenCreated {
                    AppRate.showRateDialogIfMeetsConditions(this@MainActivity) // Shows the Rate Dialog when conditions are met
                }
            }
        } else {
            lifecycleScope.launchWhenCreated {
                AppRate.showRateDialogIfMeetsConditions(this@MainActivity) // Shows the Rate Dialog when conditions are met
            }
        }

        // setup basic in-app review trigger via button press
        findViewById<AppCompatButton>(R.id.in_app_review_button).setOnClickListener {
            AppRate.with(this@MainActivity)
                .useGoogleInAppReview()
            // starting in app review flow if conditions met
            lifecycleScope.launchWhenCreated {
                AppRate.showRateDialogIfMeetsConditions(this@MainActivity)
                Toast.makeText(this@MainActivity, "In-app review triggered", Toast.LENGTH_SHORT).show()
            }
        }
    }



    companion object {
        private const val TAG = "ANDROIDRATE_SAMPLE"
    }
}