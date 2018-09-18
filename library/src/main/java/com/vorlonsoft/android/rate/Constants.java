package com.vorlonsoft.android.rate;

import static com.vorlonsoft.android.rate.Constants.Utils.UTILITY_CLASS_MESSAGE;

/**
 * <p>Constants Class - constants class of the AndroidRate library.</p>
 *
 * @since    1.1.8
 * @version  1.2.1
 * @author   Alexander Savin
 */

final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Constants" + UTILITY_CLASS_MESSAGE);
    }

    /**
     * <p>Constants.Date Class - date constants class of the AndroidRate library.</p>
     *
     * @since    1.1.8
     * @version  1.2.1
     * @author   Alexander Savin
     */

    static final class Date {

        static final short YEAR_IN_DAYS = (short) 365;

        private Date() {
            throw new UnsupportedOperationException("Constants.Date" + UTILITY_CLASS_MESSAGE);
        }
    }

    /**
     * <p>Constants.Utils Class - utils constants class of the AndroidRate library.</p>
     *
     * @since    1.1.8
     * @version  1.2.1
     * @author   Alexander Savin
     */

    static final class Utils {

        static final String EMPTY_STRING = "";

        static final String[] EMPTY_STRING_ARRAY = new String[0];

        static final String LOG_MESSAGE_PART_1 = "Failed to rate app, ";

        static final String TAG = "ANDROIDRATE";

        static final String UTILITY_CLASS_MESSAGE = " is a utility class and can't be instantiated!";

        private Utils() {
            throw new UnsupportedOperationException("Constants.Utils" + UTILITY_CLASS_MESSAGE);
        }
    }
}