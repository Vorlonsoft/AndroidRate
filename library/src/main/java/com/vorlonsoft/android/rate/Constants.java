package com.vorlonsoft.android.rate;

/**
 * <p>AndroidRate is a library to help you promote your Android app
 * by prompting users to rate the app after using it for a few days.</p>
 * <p>Constants Class - constants class of the AndroidRate library.</p>
 *
 * @author   Alexander Savin
 * @version  1.1.9
 * @since    1.1.8
 */

final class Constants {

    private Constants() {
        throw new AssertionError();
    }

    /**
     * <p>AndroidRate is a library to help you promote your Android app
     * by prompting users to rate the app after using it for a few days.</p>
     * <p>Constants.Date Class - date constants class of the AndroidRate library.</p>
     *
     * @author   Alexander Savin
     * @version  1.1.9
     * @since    1.1.8
     */

    final static class Date {

        static final long DAY_IN_MILLIS = 86400000L;

        static final short YEAR_IN_DAYS = (short) 365;

        private Date() {
            throw new AssertionError();
        }
    }

    /**
     * <p>AndroidRate is a library to help you promote your Android app
     * by prompting users to rate the app after using it for a few days.</p>
     * <p>Constants.Utils Class - utils constants class of the AndroidRate library.</p>
     *
     * @author   Alexander Savin
     * @version  1.1.9
     * @since    1.1.8
     */

    final static class Utils {

        static final String TAG = "ANDROIDRATE";

        private Utils() {
            throw new AssertionError();
        }
    }
}