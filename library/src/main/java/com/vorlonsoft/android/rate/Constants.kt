/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

/**
 * Constants Object - the constants object of the AndroidRate library.
 *
 * Contains constants.
 *
 * @since       1.1.8
 * @version     1.2.1
 * @author      Alexander Savin
 */
internal object Constants {
    /**
     * Constants.Date Object - the date constants object of the AndroidRate library.
     *
     * Contains date constants.
     *
     * @since       1.1.8
     * @version     1.2.1
     * @author      Alexander Savin
     */
    internal object Date {
        /** The time unit representing one year in days. */
        internal const val YEAR_IN_DAYS: Short = 365.toShort()
    }

    /**
     * Constants.Utils Object - the utils constants object of the AndroidRate library.
     *
     * Contains utils constants.
     *
     * @since       1.1.8
     * @version     1.2.1
     * @author      Alexander Savin
     */
    internal object Utils {
        /** The empty String. */
        internal const val EMPTY_STRING: String = ""
        /** The empty array of Strings. */
        @JvmField
        internal val EMPTY_STRING_ARRAY: Array<String?> = arrayOfNulls(0)
        /** The part 1 of some log messages of AndroidRate library. */
        internal const val LOG_MESSAGE_PART_1: String = "Failed to rate app, "
        /** The tag of all log messages of AndroidRate library. */
        internal const val TAG: String = "ANDROIDRATE"
        /** The part 2 of a utility class unsupported operation exception message. */
        internal const val UTILITY_CLASS_MESSAGE: String = " is a utility class and it can't be " +
                                                   "instantiated!"
    }
}