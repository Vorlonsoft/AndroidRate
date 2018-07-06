package com.vorlonsoft.android.rate;

final class Constants {

    private Constants() {
        throw new AssertionError();
    }

    final class Date {

        static final long DAY_IN_MILLIS = 86400000L;

        static final short YEAR_IN_DAYS = (short) 365;

        private Date() {
            throw new AssertionError();
        }
    }

    final class Utils {

        static final String TAG = "ANDROIDRATE";

        private Utils() {
            throw new AssertionError();
        }
    }
}