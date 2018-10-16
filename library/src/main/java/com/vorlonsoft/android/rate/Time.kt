/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import androidx.annotation.LongDef
import com.vorlonsoft.android.rate.Constants.Utils.Companion.UTILITY_CLASS_MESSAGE

/**
 * Time Class - the time units class of the AndroidRate library.
 *
 * @constructor Time is a utility class and it can't be instantiated.
 * @since       1.2.1
 * @version     1.2.1
 * @author      Alexander Savin
 */
class Time private constructor() {
    /** Time Class initializer block. */
    init {
        throw UnsupportedOperationException("Time$UTILITY_CLASS_MESSAGE")
    }

    /**
     * Denotes that the annotated element of the primitive type long represents a logical type and
     * that its value should be one of the following constants: [MILLISECOND], [SECOND], [MINUTE],
     * [HOUR], [DAY], [WEEK], [MONTH], [YEAR].
     *
     * @since    1.2.1
     * @version  1.2.1
     * @author   Alexander Savin
     */
    @MustBeDocumented
    @Retention(AnnotationRetention.SOURCE)
    @LongDef(MILLISECOND, SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR)
    annotation class TimeUnits

    /** Contains constants for units of time in milliseconds. */
    companion object {
        /** Time unit representing one thousandth of a second. */
        const val MILLISECOND: Long = 1
        /** Time unit representing one second. */
        const val SECOND = MILLISECOND * 1_000
        /** Time unit representing sixty seconds. */
        const val MINUTE = SECOND * 60
        /** Time unit representing sixty minutes. */
        const val HOUR = MINUTE * 60
        /** Time unit representing twenty four hours. */
        const val DAY = HOUR * 24
        /** Time unit representing seven days. */
        const val WEEK = DAY * 7
        /** Time unit representing thirty days. */
        const val MONTH = DAY * 30
        /** Time unit representing three hundred and sixty five days. */
        const val YEAR = DAY * 365
    }
}