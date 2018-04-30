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

    @SuppressWarnings({"ConstantConditions", "UnnecessaryLocalVariable"})
    public void testGetStoreUri() {
        {
            final int appStore = CHINESESTORES;
            Uri uri = UriHelper.getStoreUri(appStore,"");
            assertEquals(uri.toString(), CHINESE_STORES);
        }
        {
            final int appStore = GOOGLEPLAY;
            Uri uri = UriHelper.getStoreUri(appStore,"");
            assertEquals(uri.toString(), GOOGLE_PLAY);
        }
        {
            final int appStore = CHINESESTORES;
            Uri uri = UriHelper.getStoreUri(appStore,null);
            assertNull(uri);
        }
        {
            final int appStore = GOOGLEPLAY;
            Uri uri = UriHelper.getStoreUri(appStore,null);
            assertNull(uri);
        }
        {
            final int appStore = CHINESESTORES;
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreUri(appStore, paramName);
            assertEquals(uri.toString(), CHINESE_STORES + paramName);
        }
        {
            final int appStore = GOOGLEPLAY;
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreUri(appStore, paramName);
            assertEquals(uri.toString(), GOOGLE_PLAY + paramName);
        }
    }

    @SuppressWarnings({"ConstantConditions", "UnnecessaryLocalVariable"})
    public void testGetStoreWebUri() {
        {
            final int appStore = CHINESESTORES;
            Uri uri = UriHelper.getStoreWebUri(appStore,"");
            assertNull(uri);
        }
        {
            final int appStore = GOOGLEPLAY;
            Uri uri = UriHelper.getStoreWebUri(appStore,"");
            assertEquals(uri.toString(), GOOGLE_PLAY_WEB);
        }
        {
            final int appStore = CHINESESTORES;
            Uri uri = UriHelper.getStoreWebUri(appStore,null);
            assertNull(uri);
        }
        {
            final int appStore = GOOGLEPLAY;
            Uri uri = UriHelper.getStoreWebUri(appStore,null);
            assertNull(uri);
        }
        {
            final int appStore = CHINESESTORES;
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreWebUri(appStore, paramName);
            assertNull(uri);
        }
        {
            final int appStore = GOOGLEPLAY;
            final String paramName = "com.vorlonsoft.android.rate";
            Uri uri = UriHelper.getStoreWebUri(appStore, paramName);
            assertEquals(uri.toString(), GOOGLE_PLAY_WEB + paramName);
        }
    }
}