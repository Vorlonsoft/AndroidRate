/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import androidx.annotation.LongDef

/**
 * Time Class - the time units class of the AndroidRate library.
 *
 * Contains time units constants and [TimeUnits] annotation.
 *
 * @constructor Don't create an instance of this class. Use its members directly.
 * @since       1.2.1
 * @version     1.2.1
 * @author      Alexander Savin
 */
open class Time {
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
        const val MILLISECOND: Long = 1L
        /** Time unit representing one second. */
        const val SECOND: Long = MILLISECOND * 1_000L
        /** Time unit representing sixty seconds. */
        const val MINUTE: Long = SECOND * 60L
        /** Time unit representing sixty minutes. */
        const val HOUR: Long = MINUTE * 60L
        /** Time unit representing twenty four hours. */
        const val DAY: Long = HOUR * 24L
        /** Time unit representing seven days. */
        const val WEEK: Long = DAY * 7L
        /** Time unit representing thirty days. */
        const val MONTH: Long = DAY * 30L
        /** Time unit representing three hundred and sixty five days. */
        const val YEAR: Long = DAY * 365L
    }
}