/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public final class StoreType {

    @SuppressWarnings("WeakerAccess")
    public static final byte AMAZON = 0;               // Amazon Appstore
    @SuppressWarnings("WeakerAccess")
    public static final byte APPLE = 1;                // Apple App Store
    @SuppressWarnings("WeakerAccess")
    public static final byte BAZAAR = 2;               // Cafe Bazaar
    @SuppressWarnings("WeakerAccess")
    public static final byte BLACKBERRY = 3;           // BlackBerry World
    @SuppressWarnings("WeakerAccess")
    public static final byte CHINESESTORES = 4;        // 19 chinese app stores
    @SuppressWarnings("WeakerAccess")
    public static final byte GOOGLEPLAY = 5;           // Google Play
    @SuppressWarnings("WeakerAccess")
    public static final byte MI = 6;                   // Mi Appstore (Xiaomi Market)
    @SuppressWarnings("WeakerAccess")
    public static final byte SAMSUNG = 7;              // Samsung Galaxy Apps
    @SuppressWarnings("WeakerAccess")
    public static final byte SLIDEME = 8;              // SlideME Marketplace
    @SuppressWarnings("WeakerAccess")
    public static final byte TENCENT = 9;              // Tencent App Store
    @SuppressWarnings("WeakerAccess")
    public static final byte YANDEX = 10;              // Yandex.Store
    @SuppressWarnings("WeakerAccess")
    public static final byte INTENT = 11;              // Any custom intents
    @SuppressWarnings("WeakerAccess")
    public static final byte OTHER = 12;               // Any Other Store

    private StoreType() {
    }

    @SuppressWarnings("WeakerAccess")
    @Retention(RUNTIME)
    @IntDef({AMAZON, BAZAAR, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT, YANDEX})
    public @interface Store {}

    @SuppressWarnings("WeakerAccess")
    @Retention(RUNTIME)
    @IntDef({APPLE, BLACKBERRY})
    public @interface StoreWithId {}

    @SuppressWarnings("WeakerAccess")
    @Retention(RUNTIME)
    @IntDef({AMAZON, APPLE, BAZAAR, BLACKBERRY, CHINESESTORES, GOOGLEPLAY, MI, OTHER, SAMSUNG, SLIDEME, TENCENT, YANDEX})
    public @interface Return {}
}