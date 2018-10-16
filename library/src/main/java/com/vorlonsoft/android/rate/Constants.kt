/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import com.vorlonsoft.android.rate.Constants.Utils.Companion.UTILITY_CLASS_MESSAGE

/**
 * Constants Class - the constants class of the AndroidRate library.
 *
 * @constructor Constants is a utility class and it can't be instantiated.
 * @since       1.1.8
 * @version     1.2.1
 * @author      Alexander Savin
 */
internal class Constants private constructor() {
    /** Constants Class initializer block. */
    init {
        throw UnsupportedOperationException("Constants$UTILITY_CLASS_MESSAGE")
    }

    /**
     * Constants.Date Class - the date constants class of the AndroidRate library.
     *
     * @constructor Constants.Date is a utility class and it can't be instantiated.
     * @since       1.1.8
     * @version     1.2.1
     * @author      Alexander Savin
     */
    internal class Date private constructor() {
        /** Constants.Date Class initializer block. */
        init {
            throw UnsupportedOperationException("Constants.Date$UTILITY_CLASS_MESSAGE")
        }

        /** Contains date constants. */
        companion object {
            /** The time unit representing one year in days. */
            const val YEAR_IN_DAYS = 365.toShort()
        }
    }

    /**
     * Constants.Utils Class - the utils constants class of the AndroidRate library.
     *
     * @constructor Constants.Utils is a utility class and it can't be instantiated.
     * @since       1.1.8
     * @version     1.2.1
     * @author      Alexander Savin
     */
    internal class Utils private constructor() {
        /** Constants.Utils Class initializer block. */
        init {
            throw UnsupportedOperationException("Constants.Utils$UTILITY_CLASS_MESSAGE")
        }

        /** Contains utils constants. */
        companion object {
            /** The empty String. */
            const val EMPTY_STRING = ""
            /** The empty array of Strings. */
            @JvmField val EMPTY_STRING_ARRAY = arrayOfNulls<String>(0)
            /** The part 1 of some log messages of AndroidRate library. */
            const val LOG_MESSAGE_PART_1 = "Failed to rate app, "
            /** The tag of all log messages of AndroidRate library. */
            const val TAG = "ANDROIDRATE"
            /** The part 2 of a utility class unsupported operation exception message. */
            const val UTILITY_CLASS_MESSAGE = " is a utility class and it can't be instantiated!"
        }
    }
}