/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import androidx.annotation.IntDef

/**
 * StoreType Class - the store types class of the AndroidRate library.
 *
 * Contains store types constants and [StoreWithoutApplicationId], [StoreWithApplicationId],
 * [AnyStoreType] annotations.
 *
 * @constructor Don't create an instance of this class. Use its members directly.
 * @since       1.0.0
 * @version     2.0.0
 * @author      Alexander Savin
 * @author      Shintaro Katafuchi
 */
open class StoreType {
    /**
     * Denotes that the annotated element of the primitive type int represents a logical type and
     * that its value should be one of the following constants: [AMAZON], [BAZAAR], [CHINESESTORES],
     * [GOOGLEPLAY], [MI], [SAMSUNG], [SLIDEME], [TENCENT], [YANDEX].
     *
     * @since       1.1.6
     * @version     2.0.0
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
     * @version     2.0.0
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
     * @version     2.0.0
     * @author      Alexander Savin
     */
    @MustBeDocumented
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(AMAZON, APPLE, BAZAAR, BLACKBERRY, CHINESESTORES, GOOGLEPLAY, INTENT, MI, OTHER,
            SAMSUNG, SLIDEME, TENCENT, YANDEX)
    annotation class AnyStoreType

    /** Contains constants for store types. */
    companion object {
        /** Amazon Appstore. */
        const val AMAZON: Int = 0
        /** Apple App Store. */
        const val APPLE: Int = 1
        /** Cafe Bazaar. */
        const val BAZAAR: Int = 2
        /** BlackBerry World. */
        const val BLACKBERRY: Int = 3
        /** 19 chinese app stores. */
        const val CHINESESTORES: Int = 4
        /** Google Play. */
        const val GOOGLEPLAY: Int = 5
        /** Mi Appstore (Xiaomi Market). */
        const val MI: Int = 6
        /** Samsung Galaxy Apps. */
        const val SAMSUNG: Int = 7
        /** SlideME Marketplace. */
        const val SLIDEME: Int = 8
        /** Tencent App Store. */
        const val TENCENT: Int = 9
        /** Yandex.Store. */
        const val YANDEX: Int = 10
        /** Any custom intents. */
        const val INTENT: Int = 11
        /** Any other Store. */
        const val OTHER: Int = 12
    }
}