/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.test.AndroidTestCase
import org.junit.Assert

/**
 * Unit test for [com.vorlonsoft.android.rate.UriHelper]
 */
class UriHelperTest : AndroidTestCase() {

    fun testGetGooglePlayUri() {
        run {
            val uri = UriHelper.getGooglePlay("")
            Assert.assertEquals(uri!!.toString(), GOOGLE_PLAY)
        }
        run {
            val uri = UriHelper.getGooglePlay(null)
            Assert.assertNull(uri)
        }
        run {
            val packageName = "com.vorlonsoft.android.rate"
            val uri = UriHelper.getGooglePlay(packageName)
            Assert.assertEquals(uri!!.toString(), GOOGLE_PLAY + packageName)
        }
    }

    companion object {

        private val GOOGLE_PLAY = "https://play.google.com/store/apps/details?id="
    }
}