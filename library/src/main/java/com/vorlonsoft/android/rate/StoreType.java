/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import androidx.annotation.IntDef;

import static com.vorlonsoft.android.rate.Constants.Utils.UTILITY_CLASS_MESSAGE;
import static java.lang.annotation.RetentionPolicy.CLASS;

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
    /** <p>Amazon Appstore.</p> */
    public static final int AMAZON = 0;
    /** <p>Apple App Store.</p> */
    public static final int APPLE = 1;
    /** <p>Cafe Bazaar.</p> */
    public static final int BAZAAR = 2;
    /** <p>BlackBerry World.</p> */
    public static final int BLACKBERRY = 3;
    /** <p>19 chinese app stores.</p> */
    public static final int CHINESESTORES = 4;
    /** <p>Google Play.</p> */
    public static final int GOOGLEPLAY = 5;
    /** <p>Mi Appstore (Xiaomi Market).</p> */
    public static final int MI = 6;
    /** <p>Samsung Galaxy Apps.</p> */
    public static final int SAMSUNG = 7;
    /** <p>SlideME Marketplace.</p> */
    public static final int SLIDEME = 8;
    /** <p>Tencent App Store.</p> */
    public static final int TENCENT = 9;
    /** <p>Yandex.Store.</p> */
    public static final int YANDEX = 10;
    /** <p>Any custom intents.</p> */
    public static final int INTENT = 11;
    /** <p>Any other Store.</p> */
    public static final int OTHER = 12;

    private StoreType() {
        throw new UnsupportedOperationException("StoreType" + UTILITY_CLASS_MESSAGE);
    }

    /**
     * <p>Annotates element of integer type.</p>
     * <p>Annotated element, represents a logical type and its value should be one of the following
     * constants: AMAZON, BAZAAR, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT,
     * YANDEX.</p>
     */
    @Documented
    @Retention(CLASS)
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
    @Retention(CLASS)
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
    @Retention(CLASS)
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