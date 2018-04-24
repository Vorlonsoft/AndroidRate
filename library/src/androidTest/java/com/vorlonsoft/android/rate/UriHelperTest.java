/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.net.Uri;
import android.test.AndroidTestCase;

import static com.vorlonsoft.android.rate.StoreType.CHINESESTORES;
import static com.vorlonsoft.android.rate.StoreType.GOOGLEPLAY;

/**
 * Unit test for {@link com.vorlonsoft.android.rate.UriHelper}
 */
public class UriHelperTest extends AndroidTestCase {

    private static final String CHINESE_STORES = "market://details?id=";

    private static final String GOOGLE_PLAY = "market://details?id=";

    private static final String GOOGLE_PLAY_WEB = "https://play.google.com/store/apps/details?id=";

    @SuppressWarnings("ConstantConditions")
    public void testGetStoreUri() {
        {
            Uri uri = UriHelper.getStoreUri(CHINESESTORES,"");
            assertEquals(uri.toString(), CHINESE_STORES);
        }
        {
            Uri uri = UriHelper.getStoreUri(GOOGLEPLAY,"");
            assertEquals(uri.toString(), GOOGLE_PLAY);
        }
        {
            Uri uri = UriHelper.getStoreUri(CHINESESTORES,null);
            assertNull(uri);
        }
        {
            Uri uri = UriHelper.getStoreUri(GOOGLEPLAY,null);
            assertNull(uri);
        }
        {
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreUri(CHINESESTORES, paramName);
            assertEquals(uri.toString(), CHINESE_STORES + paramName);
        }
        {
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreUri(GOOGLEPLAY, paramName);
            assertEquals(uri.toString(), GOOGLE_PLAY + paramName);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void testGetStoreWebUri() {
        {
            Uri uri = UriHelper.getStoreWebUri(CHINESESTORES,"");
            assertNull(uri);
        }
        {
            Uri uri = UriHelper.getStoreWebUri(GOOGLEPLAY,"");
            assertEquals(uri.toString(), GOOGLE_PLAY_WEB);
        }
        {
            Uri uri = UriHelper.getStoreWebUri(CHINESESTORES,null);
            assertNull(uri);
        }
        {
            Uri uri = UriHelper.getStoreWebUri(GOOGLEPLAY,null);
            assertNull(uri);
        }
        {
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreWebUri(CHINESESTORES, paramName);
            assertNull(uri);
        }
        {
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreWebUri(GOOGLEPLAY, paramName);
            assertEquals(uri.toString(), GOOGLE_PLAY_WEB + paramName);
        }
    }
}