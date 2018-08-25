package com.vorlonsoft.android.rate;

/**
 * <p>Constants Class - constants class of the AndroidRate library.</p>
 *
 * @since    1.1.8
 * @version  1.1.9
 * @author   Alexander Savin
 */

final class Constants {

    private Constants() {
        throw new AssertionError();
    }

    /**
     * <p>Constants.Date Class - date constants class of the AndroidRate library.</p>
     *
     * @since    1.1.8
     * @version  1.1.9
     * @author   Alexander Savin
     */

    final static class Date {

        static final long DAY_IN_MILLIS = 86400000L;

        static final short YEAR_IN_DAYS = (short) 365;

        private Date() {
            throw new AssertionError();
        }
    }

    /**
     * <p>Constants.Utils Class - utils constants class of the AndroidRate library.</p>
     *
     * @since    1.1.8
     * @version  1.1.9
     * @author   Alexander Savin
     */

    final static class Utils {

        static final String TAG = "ANDROIDRATE";

        private Utils() {
            throw new AssertionError();
        }
    }
}