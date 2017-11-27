/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.content.Context
import android.content.Intent

import com.vorlonsoft.android.rate.UriHelper.getGooglePlay
import com.vorlonsoft.android.rate.UriHelper.getAmazonAppstore
import com.vorlonsoft.android.rate.UriHelper.isPackageExists

internal object IntentHelper {

    private val GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending"

    fun createIntentForGooglePlay(context: Context): Intent {
        val packageName = context.packageName
        val intent = Intent(Intent.ACTION_VIEW, getGooglePlay(packageName))
        if (isPackageExists(context, GOOGLE_PLAY_PACKAGE_NAME)) {
            intent.`package` = GOOGLE_PLAY_PACKAGE_NAME
        }
        return intent
    }

    fun createIntentForAmazonAppstore(context: Context): Intent {
        val packageName = context.packageName
        return Intent(Intent.ACTION_VIEW, getAmazonAppstore(packageName))
    }

}
