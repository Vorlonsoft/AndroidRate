/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import androidx.annotation.IntDef
import com.vorlonsoft.android.rate.Constants.Utils.Companion.UTILITY_CLASS_MESSAGE

/**
 * StoreType Class - the store types class of the AndroidRate library.
 *
 * @constructor StoreType is a utility class and it can't be instantiated.
 * @since       1.0.0
 * @version     1.2.1
 * @author      Alexander Savin
 * @author      Shintaro Katafuchi
 */
class StoreType private constructor() {
    /** StoreType Class initializer block. */
    init {
        throw UnsupportedOperationException("StoreType$UTILITY_CLASS_MESSAGE")
    }

    /**
     * Denotes that the annotated element of the primitive type int represents a logical type and
     * that its value should be one of the following constants: [AMAZON], [BAZAAR], [CHINESESTORES],
     * [GOOGLEPLAY], [MI], [SAMSUNG], [SLIDEME], [TENCENT], [YANDEX].
     *
     * @since       1.1.6
     * @version     1.2.1
     * @author      Alexander Savin
     */
    @MustBeDocumented
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(AMAZON, BAZAAR, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT, YANDEX)
    annotation class StoreWithoutApplicationId

    /**
     * Denotes that the annotated element of the primitive type int represents a logical type and
     * that its value should be one of the following constants: [APPLE], [BLACKBERRY].
     *
     * @since       1.1.6
     * @version     1.2.1
     * @author      Alexander Savin
     */
    @MustBeDocumented
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(APPLE, BLACKBERRY)
    annotation class StoreWithApplicationId

    /**
     * Denotes that the annotated element of the primitive type int represents a logical type and
     * that its value should be one of the following constants: [AMAZON], [APPLE], [BAZAAR],
     * [BLACKBERRY], [CHINESESTORES], [GOOGLEPLAY], [INTENT], [MI], [OTHER], [SAMSUNG], [SLIDEME],
     * [TENCENT], [YANDEX].
     *
     * @since       1.1.6
     * @version     1.2.1
     * @author      Alexander Savin
     */
    @MustBeDocumented
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(AMAZON, APPLE, BAZAAR, BLACKBERRY, CHINESESTORES, GOOGLEPLAY, INTENT, MI, OTHER,
            SAMSUNG, SLIDEME, TENCENT, YANDEX)
    annotation class AnyStoreType

    /** The singleton contains constants for store types. */
    companion object {
        /** Amazon Appstore. */
        const val AMAZON = 0
        /** Apple App Store. */
        const val APPLE = 1
        /** Cafe Bazaar. */
        const val BAZAAR = 2
        /** BlackBerry World. */
        const val BLACKBERRY = 3
        /** 19 chinese app stores. */
        const val CHINESESTORES = 4
        /** Google Play. */
        const val GOOGLEPLAY = 5
        /** Mi Appstore (Xiaomi Market). */
        const val MI = 6
        /** Samsung Galaxy Apps. */
        const val SAMSUNG = 7
        /** SlideME Marketplace. */
        const val SLIDEME = 8
        /** Tencent App Store. */
        const val TENCENT = 9
        /** Yandex.Store. */
        const val YANDEX = 10
        /** Any custom intents. */
        const val INTENT = 11
        /** Any other Store. */
        const val OTHER = 12
    }
}