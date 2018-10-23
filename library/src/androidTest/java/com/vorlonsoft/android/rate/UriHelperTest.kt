/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.net.Uri
import com.vorlonsoft.android.rate.Constants.Utils.EMPTY_STRING
import com.vorlonsoft.android.rate.StoreType.Companion.CHINESESTORES
import com.vorlonsoft.android.rate.StoreType.Companion.GOOGLEPLAY
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Unit test for [com.vorlonsoft.android.rate.UriHelper] object.
 *
 * @constructor Empty constructor of unit test for [com.vorlonsoft.android.rate.UriHelper] object.
 * @since    0.1.3
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */
internal class UriHelperTest {
    /** Test for [com.vorlonsoft.android.rate.UriHelper.getStoreUri] function. */
    @Test
    internal fun getStoreUri() {
        run {
            val uri: Uri? = UriHelper.getStoreUri(CHINESESTORES, EMPTY_STRING)
            assertEquals(uri?.toString(), CHINESESTORES_URI)
        }
        run {
            val uri: Uri? = UriHelper.getStoreUri(GOOGLEPLAY, EMPTY_STRING)
            assertEquals(uri?.toString(), GOOGLEPLAY_URI)
        }
        run {
            val uri: Uri? = UriHelper.getStoreUri(CHINESESTORES, SAMPLE_APP_PACKAGE)
            assertEquals(uri?.toString(), CHINESESTORES_URI + SAMPLE_APP_PACKAGE)
        }
        run {
            val uri: Uri? = UriHelper.getStoreUri(GOOGLEPLAY, SAMPLE_APP_PACKAGE)
            assertEquals(uri?.toString(), GOOGLEPLAY_URI + SAMPLE_APP_PACKAGE)
        }
    }

    /** Test for [com.vorlonsoft.android.rate.UriHelper.getStoreWebUri] function. */
    @Test
    internal fun getStoreWebUri() {
        run {
            val uri: Uri? = UriHelper.getStoreWebUri(CHINESESTORES, EMPTY_STRING)
            assertNull(uri)
        }
        run {
            val uri: Uri? = UriHelper.getStoreWebUri(GOOGLEPLAY, EMPTY_STRING)
            assertEquals(uri?.toString(), GOOGLEPLAY_WEB_URI)
        }
        run {
            val uri: Uri? = UriHelper.getStoreWebUri(CHINESESTORES, SAMPLE_APP_PACKAGE)
            assertNull(uri)
        }
        run {
            val uri: Uri? = UriHelper.getStoreWebUri(GOOGLEPLAY, SAMPLE_APP_PACKAGE)
            assertEquals(uri?.toString(), GOOGLEPLAY_WEB_URI + SAMPLE_APP_PACKAGE)
        }
    }

    private companion object {
        /** 19 chinese app stores URI. */
        private const val CHINESESTORES_URI: String = "market://details?id="
        /** Google Play URI. */
        private const val GOOGLEPLAY_URI: String = CHINESESTORES_URI
        /** Google Play web URI. */
        private const val GOOGLEPLAY_WEB_URI: String = "https://play.google.com/store/apps/details?id="
        /** AndroidRate Sample app package. */
        private const val SAMPLE_APP_PACKAGE: String = "com.vorlonsoft.android.rate.sample"
    }
}