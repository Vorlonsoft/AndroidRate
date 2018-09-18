/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import androidx.annotation.LongDef;

import static com.vorlonsoft.android.rate.Constants.Utils.UTILITY_CLASS_MESSAGE;
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
    /** <p>Constant for unit of duration in milliseconds: millisecond.</p> */
    public static final long MILLISECOND = 1;
    /** <p>Constant for unit of duration in milliseconds: second.</p> */
    public static final long SECOND = MILLISECOND * 1000;
    /** <p>Constant for unit of duration in milliseconds: minute.</p> */
    public static final long MINUTE = SECOND * 60;
    /** <p>Constant for unit of duration in milliseconds: hour.</p> */
    public static final long HOUR = MINUTE * 60;
    /** <p>Constant for unit of duration in milliseconds: day.</p> */
    public static final long DAY = HOUR * 24;
    /** <p>Constant for unit of duration in milliseconds: week.</p> */
    public static final long WEEK = DAY * 7;
    /** <p>Constant for unit of duration in milliseconds: month.</p> */
    public static final long MONTH = DAY * 30;
    /** <p>Constant for unit of duration in milliseconds: year.</p> */
    public static final long YEAR = DAY * 365;

    private Time() {
        throw new UnsupportedOperationException("Time" + UTILITY_CLASS_MESSAGE);
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