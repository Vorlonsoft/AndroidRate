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
    public static final byte AMAZON = (byte) 0;               // Amazon Appstore
    @SuppressWarnings("WeakerAccess")
    public static final byte APPLE = (byte) 1;                // Apple App Store
    @SuppressWarnings("WeakerAccess")
    public static final byte BAZAAR = (byte) 2;               // Cafe Bazaar
    @SuppressWarnings("WeakerAccess")
    public static final byte BLACKBERRY = (byte) 3;           // BlackBerry World
    @SuppressWarnings("WeakerAccess")
    public static final byte CHINESESTORES = (byte) 4;        // 19 chinese app stores
    @SuppressWarnings("WeakerAccess")
    public static final byte GOOGLEPLAY = (byte) 5;           // Google Play
    @SuppressWarnings("WeakerAccess")
    public static final byte MI = (byte) 6;                   // Mi Appstore (Xiaomi Market)
    @SuppressWarnings("WeakerAccess")
    public static final byte SAMSUNG = (byte) 7;              // Samsung Galaxy Apps
    @SuppressWarnings("WeakerAccess")
    public static final byte SLIDEME = (byte) 8;              // SlideME Marketplace
    @SuppressWarnings("WeakerAccess")
    public static final byte TENCENT = (byte) 9;              // Tencent App Store
    @SuppressWarnings("WeakerAccess")
    public static final byte YANDEX = (byte) 10;              // Yandex.Store
    @SuppressWarnings("WeakerAccess")
    public static final byte INTENT = (byte) 11;              // Any custom intents
    @SuppressWarnings("WeakerAccess")
    public static final byte OTHER = (byte) 12;               // Any Other Store

    private StoreType() {
    }

    @SuppressWarnings("WeakerAccess")
    @Retention(RUNTIME)
    @IntDef({AMAZON, BAZAAR, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT, YANDEX})
    public @interface StoreWithoutApplicationId {}

    @SuppressWarnings("WeakerAccess")
    @Retention(RUNTIME)
    @IntDef({APPLE, BLACKBERRY})
    public @interface StoreWithApplicationId {}

    @SuppressWarnings("WeakerAccess")
    @Retention(RUNTIME)
    @IntDef({AMAZON, APPLE, BAZAAR, BLACKBERRY, CHINESESTORES, GOOGLEPLAY, INTENT, MI, OTHER, SAMSUNG, SLIDEME, TENCENT, YANDEX})
    public @interface AnyStoreType {}
}