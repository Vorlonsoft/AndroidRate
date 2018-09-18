package com.vorlonsoft.android.rate;

/**
 * <p>Constants Class - constants class of the AndroidRate library.</p>
 *
 * @since    1.1.8
 * @version  1.2.1
 * @author   Alexander Savin
 */

final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Constants is a utility class and can't be instantiated!");
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
            throw new UnsupportedOperationException("Constants.Date is a utility class and can't be instantiated!");
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

        static final String TAG = "ANDROIDRATE";

        private Utils() {
            throw new UnsupportedOperationException("Constants.Utils is a utility class and can't be instantiated!");
        }
    }
}