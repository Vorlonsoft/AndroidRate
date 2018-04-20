/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.net.Uri;
import android.test.AndroidTestCase;

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
            Uri uri = UriHelper.getStoreUri(StoreType.CHINESESTORES,"");
            assertEquals(uri.toString(), CHINESE_STORES);
        }
        {
            Uri uri = UriHelper.getStoreUri(StoreType.GOOGLEPLAY,"");
            assertEquals(uri.toString(), GOOGLE_PLAY);
        }
        {
            Uri uri = UriHelper.getStoreUri(StoreType.CHINESESTORES,null);
            assertNull(uri);
        }
        {
            Uri uri = UriHelper.getStoreUri(StoreType.GOOGLEPLAY,null);
            assertNull(uri);
        }
        {
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreUri(StoreType.CHINESESTORES, paramName);
            assertEquals(uri.toString(), CHINESE_STORES + paramName);
        }
        {
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreUri(StoreType.GOOGLEPLAY, paramName);
            assertEquals(uri.toString(), GOOGLE_PLAY + paramName);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void testGetStoreWebUri() {
        {
            Uri uri = UriHelper.getStoreWebUri(StoreType.CHINESESTORES,"");
            assertNull(uri);
        }
        {
            Uri uri = UriHelper.getStoreWebUri(StoreType.GOOGLEPLAY,"");
            assertEquals(uri.toString(), GOOGLE_PLAY_WEB);
        }
        {
            Uri uri = UriHelper.getStoreWebUri(StoreType.CHINESESTORES,null);
            assertNull(uri);
        }
        {
            Uri uri = UriHelper.getStoreWebUri(StoreType.GOOGLEPLAY,null);
            assertNull(uri);
        }
        {
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreWebUri(StoreType.CHINESESTORES, paramName);
            assertNull(uri);
        }
        {
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreWebUri(StoreType.GOOGLEPLAY, paramName);
            assertEquals(uri.toString(), GOOGLE_PLAY_WEB + paramName);
        }
    }
}