/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import androidx.annotation.LongDef;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * <p>Time Class - time units class of the AndroidRate library.</p>
 *
 * @since    1.2.1
 * @version  1.2.1
 * @author   Alexander Savin
 */

@SuppressWarnings("WeakerAccess")
public class Time {
    /** Constant for unit of duration in milliseconds: millisecond */
    public static final long MILLISECOND = 1;
    /** Constant for unit of duration in milliseconds: second */
    public static final long SECOND = MILLISECOND * 1000;
    /** Constant for unit of duration in milliseconds: minute */
    public static final long MINUTE = SECOND * 60;
    /** Constant for unit of duration in milliseconds: hour */
    public static final long HOUR = MINUTE * 60;
    /** Constant for unit of duration in milliseconds: day */
    public static final long DAY = HOUR * 24;
    /** Constant for unit of duration in milliseconds: week */
    public static final long WEEK = DAY * 7;
    /** Constant for unit of duration in milliseconds: month */
    public static final long MONTH = DAY * 30;
    /** Constant for unit of duration in milliseconds: year */
    public static final long YEAR = DAY * 365;

    private Time() {
        throw new AssertionError();
    }

    /**
     * <p>Annotates element of long type.</p>
     * <p>Annotated element, represents a logical type and its value should be one of the following
     * constants: MILLISECOND, SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR.</p>
     */
    @Documented
    @Retention(SOURCE)
    @LongDef({
            MILLISECOND,
            SECOND,
            MINUTE,
            HOUR,
            DAY,
            WEEK,
            MONTH,
            YEAR
    })
    public @interface TimeUnits {
    }
}