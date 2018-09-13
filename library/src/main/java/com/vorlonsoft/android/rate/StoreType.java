/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import androidx.annotation.IntDef;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * <p>StoreType Class - store type class of the AndroidRate library.</p>
 *
 * @since    1.0.0
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */

@SuppressWarnings("WeakerAccess")
public class StoreType {
    /** Amazon Appstore */
    public static final int AMAZON = 0;
    /** Apple App Store */
    public static final int APPLE = 1;
    /** Cafe Bazaar */
    public static final int BAZAAR = 2;
    /** BlackBerry World */
    public static final int BLACKBERRY = 3;
    /** 19 chinese app stores */
    public static final int CHINESESTORES = 4;
    /** Google Play */
    public static final int GOOGLEPLAY = 5;
    /** Mi Appstore (Xiaomi Market) */
    public static final int MI = 6;
    /** Samsung Galaxy Apps */
    public static final int SAMSUNG = 7;
    /** SlideME Marketplace */
    public static final int SLIDEME = 8;
    /** Tencent App Store */
    public static final int TENCENT = 9;
    /** Yandex.Store */
    public static final int YANDEX = 10;
    /** Any custom intents */
    public static final int INTENT = 11;
    /** Any other Store */
    public static final int OTHER = 12;

    private StoreType() {
        throw new AssertionError();
    }

    /**
     * <p>Annotates element of integer type.</p>
     * <p>Annotated element, represents a logical type and its value should be one of the following
     * constants: AMAZON, BAZAAR, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT,
     * YANDEX.</p>
     */
    @Documented
    @Retention(SOURCE)
    @IntDef({
            AMAZON,
            BAZAAR,
            CHINESESTORES,
            GOOGLEPLAY,
            MI,
            SAMSUNG,
            SLIDEME,
            TENCENT,
            YANDEX
    })
    public @interface StoreWithoutApplicationId {
    }

    /**
     * <p>Annotates element of integer type.</p>
     * <p>Annotated element, represents a logical type and its value should be one of the following
     * constants: APPLE, BLACKBERRY.</p>
     */
    @Documented
    @Retention(SOURCE)
    @IntDef({
            APPLE,
            BLACKBERRY
    })
    public @interface StoreWithApplicationId {
    }

    /**
     * <p>Annotates element of integer type.</p>
     * <p>Annotated element, represents a logical type and its value should be one of the following
     * constants: AMAZON, APPLE, BAZAAR, BLACKBERRY, CHINESESTORES, GOOGLEPLAY, INTENT, MI, OTHER,
     * SAMSUNG, SLIDEME, TENCENT, YANDEX.</p>
     */
    @Documented
    @Retention(SOURCE)
    @IntDef({
            AMAZON,
            APPLE,
            BAZAAR,
            BLACKBERRY,
            CHINESESTORES,
            GOOGLEPLAY,
            INTENT,
            MI,
            OTHER,
            SAMSUNG,
            SLIDEME,
            TENCENT,
            YANDEX
    })
    public @interface AnyStoreType {
    }
}