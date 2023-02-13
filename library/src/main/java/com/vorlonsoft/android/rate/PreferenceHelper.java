/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import static com.vorlonsoft.android.rate.Constants.Date.YEAR_IN_DAYS;
import static com.vorlonsoft.android.rate.Constants.Utils.EMPTY_STRING;
import static com.vorlonsoft.android.rate.Constants.Utils.UTILITY_CLASS_MESSAGE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

/**
 * <p>PreferenceHelper Class - preference helper class of the AndroidRate library.</p>
 *
 * @since    0.1.3
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */
final class PreferenceHelper {

    private static final String PREF_FILE_NAME = "androidrate_pref_file";

    private static final String PREF_KEY_365_DAY_PERIOD_DIALOG_LAUNCH_TIMES = "androidrate_365_day_period_dialog_launch_times";
    /** The key prefix for each custom event, so that there is no clash with existing keys (PREF_KEY_INSTALL_DATE etc.) */
    private static final String PREF_KEY_CUSTOM_EVENT_PREFIX = "androidrate_custom_event_prefix_";

    private static final String PREF_KEY_DIALOG_FIRST_LAUNCH_TIME = "androidrate_dialog_first_launch_time";

    private static final String PREF_KEY_INSTALL_DATE = "androidrate_install_date";

    private static final String PREF_KEY_AGREED_OR_DECLINED = "androidrate_agreed_or_declined";

    private static final String PREF_KEY_LAUNCH_TIMES = "androidrate_launch_times";

    private static final String PREF_KEY_LAST_TIME_SHOWN = "androidrate_last_time_shown";

    private static final String PREF_KEY_REMIND_LAUNCHES_NUMBER = "androidrate_remind_launches_number";

    private static final String PREF_KEY_VERSION_CODE = "androidrate_version_code";

    private static final String PREF_KEY_VERSION_NAME = "androidrate_version_name";

    private static final String NUMERIC_MASK = "(0|[1-9][0-9]*)";

    private static final String DEFAULT_DIALOG_LAUNCH_TIMES = ":0y0-0:";

    private static final String CURRENT_DAY_LAUNCHES_MASK = "-" + NUMERIC_MASK + ":";

    private PreferenceHelper() {
        throw new UnsupportedOperationException("PreferenceHelper" + UTILITY_CLASS_MESSAGE);
    }

    private static SharedPreferences getPreferences(final Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getPreferencesEditor(final Context context) {
        return getPreferences(context).edit();
    }

    private static void setCurrentDayDialogLaunchTimes(@NonNull final Context context,
                                                       @Nullable final String dialogLaunchTimes,
                                                       final byte currentYear,
                                                       final short currentDay,
                                                       final short currentDayCount) {
        String putDialogLaunchTimes = (dialogLaunchTimes != null) ?
                dialogLaunchTimes.replaceAll(":" + currentDay + "y" + currentYear + CURRENT_DAY_LAUNCHES_MASK,
                                             ":" + currentDay + "y" + currentYear + "-" + currentDayCount + ":") :
                ":" + currentDay + "y" + currentYear + "-" + currentDayCount + ":";
        // since 3rd year deletes data for the current day that older than 2 years
        if (currentYear > 1) {
            for (byte b = 0; b < currentYear - 1; b++) {
                putDialogLaunchTimes = putDialogLaunchTimes.replaceAll(":" + currentDay + "y" + b + CURRENT_DAY_LAUNCHES_MASK,
                                                                       ":");
            }
        }

        getPreferencesEditor(context)
                .putString(PREF_KEY_365_DAY_PERIOD_DIALOG_LAUNCH_TIMES, putDialogLaunchTimes)
                .apply();
    }

    /**
     * <p>Clears data in shared preferences.</p>
     *
     * @param context context
     */
    static void clearSharedPreferences(final Context context) {
        getPreferencesEditor(context)
                .clear()
                .apply();
    }

    static boolean isFirstLaunch(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0L) == 0L;
    }

    static void setFirstLaunchSharedPreferences(final Context context) {
        final SharedPreferences.Editor preferencesEditor = getPreferencesEditor(context);
        preferencesEditor.putString(PREF_KEY_365_DAY_PERIOD_DIALOG_LAUNCH_TIMES, DEFAULT_DIALOG_LAUNCH_TIMES)
                .putLong(PREF_KEY_DIALOG_FIRST_LAUNCH_TIME, 0L)
                .putLong(PREF_KEY_INSTALL_DATE, new Date().getTime())
                .putInt(PREF_KEY_LAUNCH_TIMES, 1)
                .putLong(PREF_KEY_LAST_TIME_SHOWN, 0L)
                .putInt(PREF_KEY_REMIND_LAUNCHES_NUMBER, 0)
                .putLong(PREF_KEY_VERSION_CODE, AppInformation.getLongVersionCode(context))
                .putString(PREF_KEY_VERSION_NAME, AppInformation.getVersionName(context))
                .apply();
        if (!getAgreedOrDeclined(context)) { //if (get() == false) set(false); - NOT error!
            setAgreedOrDeclined(context, false);
        }
    }

    static void increment365DayPeriodDialogLaunchTimes(@NonNull final Context context) {
        short currentDay = (short) ((new Date().getTime() - getDialogFirstLaunchTime(context)) / Time.DAY);
        final byte currentYear = (byte) (currentDay / YEAR_IN_DAYS);
        final String currentDialogLaunchTimes = getPreferences(context)
                .getString(PREF_KEY_365_DAY_PERIOD_DIALOG_LAUNCH_TIMES, DEFAULT_DIALOG_LAUNCH_TIMES);

        if (currentYear > 0) {
            currentDay = (short) (currentDay % YEAR_IN_DAYS);
        }

        if ((currentDialogLaunchTimes != null) &&
            currentDialogLaunchTimes.matches("(.*):" + currentDay + "y" + currentYear + CURRENT_DAY_LAUNCHES_MASK)) {
            final short length = (short) currentDialogLaunchTimes.length();
            String currentDayCount = EMPTY_STRING + currentDialogLaunchTimes.charAt(length - 2);
            for (short s = (short) (length - 3); s > 0; s--) {
                if (Character.isDigit(currentDialogLaunchTimes.charAt(s))) {
                    currentDayCount = currentDialogLaunchTimes.charAt(s) + currentDayCount;
                } else {
                    break;
                }
            }
            setCurrentDayDialogLaunchTimes(context, currentDialogLaunchTimes, currentYear,
                                           currentDay, (short) (Short.parseShort(currentDayCount) + 1));
        } else {
            setCurrentDayDialogLaunchTimes(context, currentDialogLaunchTimes, currentYear,
                                           currentDay, (short) 1);
        }
    }

    static short get365DayPeriodDialogLaunchTimes(@NonNull final Context context) {
        short currentDay = (short) ((new Date().getTime() - getDialogFirstLaunchTime(context)) / Time.DAY);
        final byte currentYear = (byte) (currentDay / YEAR_IN_DAYS);
        String dialogLaunchTimes = getPreferences(context)
                .getString(PREF_KEY_365_DAY_PERIOD_DIALOG_LAUNCH_TIMES, DEFAULT_DIALOG_LAUNCH_TIMES);

        dialogLaunchTimes = dialogLaunchTimes != null ?
                dialogLaunchTimes.replaceAll(":" + NUMERIC_MASK + "y" + currentYear + "-", ":") :
                DEFAULT_DIALOG_LAUNCH_TIMES;
        if (currentYear > 0) {
            currentDay = (short) (currentDay % YEAR_IN_DAYS);
            for (short s = currentDay; s < YEAR_IN_DAYS; s++) {
                dialogLaunchTimes = dialogLaunchTimes.replaceAll(":" + s + "y" + (currentYear - 1) + "-",
                                                                 ":");
            }
        }
        dialogLaunchTimes = dialogLaunchTimes.replaceAll(":" + NUMERIC_MASK + "y" + NUMERIC_MASK + CURRENT_DAY_LAUNCHES_MASK,
                                                         ":");
        if (dialogLaunchTimes.length() > 2) {
            dialogLaunchTimes = dialogLaunchTimes.substring(1, dialogLaunchTimes.length() - 1);
        }

        short dialogLaunchTimesCount = 0;
        final String[] dialogLaunchTimesSplit = dialogLaunchTimes.split(":");
        for (String aDialogLaunchTimesSplit : dialogLaunchTimesSplit) {
            dialogLaunchTimesCount = (short) (dialogLaunchTimesCount + Short.parseShort(aDialogLaunchTimesSplit));
        }

        return dialogLaunchTimesCount;
    }

    static void setCustomEventCount(final Context context, final String eventName, final short eventCount) {
        getPreferencesEditor(context)
                .putInt(PREF_KEY_CUSTOM_EVENT_PREFIX + eventName, eventCount)
                .apply();
    }

    static short getCustomEventCount(final Context context, final String eventName) {
        return (short) getPreferences(context).getInt(PREF_KEY_CUSTOM_EVENT_PREFIX + eventName, 0);
    }

    static void setDialogFirstLaunchTime(final Context context) {
        getPreferencesEditor(context)
                .putLong(PREF_KEY_DIALOG_FIRST_LAUNCH_TIME, new Date().getTime())
                .apply();
    }

    static long getDialogFirstLaunchTime(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_DIALOG_FIRST_LAUNCH_TIME, 0L);
    }

    static long getInstallDate(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0L);
    }

    /**
     * <p>Sets the Rate Dialog agreed or declined flag.</p>
     * <p>If true, the user has either agreed or declined to rating the app.
     * Meaning the rating dialog shouldn't be shown again.</p>
     *
     * @param context context
     * @param agreedOrDeclined the Rate Dialog agree flag
     */
    static void setAgreedOrDeclined(final Context context, final boolean agreedOrDeclined) {
        getPreferencesEditor(context)
                .putBoolean(PREF_KEY_AGREED_OR_DECLINED, agreedOrDeclined)
                .apply();
    }

    static boolean getAgreedOrDeclined(final Context context) {
        return getPreferences(context).getBoolean(PREF_KEY_AGREED_OR_DECLINED, false);
    }

    /**
     * <p>Sets number of times the app has been launched.</p>
     *
     * @param context context
     * @param launchTimes number of launch times to set
     */
    static void setLaunchTimes(final Context context, final short launchTimes) {
        getPreferencesEditor(context)
                .putInt(PREF_KEY_LAUNCH_TIMES, launchTimes)
                .apply();
    }

    static short getLaunchTimes(final Context context) {
        return (short) getPreferences(context).getInt(PREF_KEY_LAUNCH_TIMES, 0);
    }

    static void setLastTimeShown(final Context context) {
        getPreferencesEditor(context)
                .putLong(PREF_KEY_LAST_TIME_SHOWN, new Date().getTime())
                .apply();
    }

    static long getLastTimeShown(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_LAST_TIME_SHOWN, 0L);
    }

    /**
     * <p>Sets to {@link #PREF_KEY_REMIND_LAUNCHES_NUMBER} the current number of app launches.</p>
     * <p>The Library calls this method when the neutral button is clicked.</p>
     *
     * @param context context
     */
    static void setRemindLaunchesNumber(final Context context) {
        getPreferencesEditor(context)
                .putInt(PREF_KEY_REMIND_LAUNCHES_NUMBER, getLaunchTimes(context))
                .apply();
    }

    static short getRemindLaunchesNumber(final Context context) {
        return (short) getPreferences(context).getInt(PREF_KEY_REMIND_LAUNCHES_NUMBER, 0);
    }

    /**
     * <p>Clears shared preferences that were set up by clicking the Remind Button.</p>
     *
     * @param context context
     */
    static void clearRemindButtonClick(final Context context) {
        getPreferencesEditor(context)
                .putLong(PREF_KEY_LAST_TIME_SHOWN, 0L)
                .putInt(PREF_KEY_REMIND_LAUNCHES_NUMBER, 0)
                .apply();
    }

    static void setVersionCode(final Context context) {
        getPreferencesEditor(context)
                .putLong(PREF_KEY_VERSION_CODE, AppInformation.getLongVersionCode(context))
                .apply();
    }

    static long getVersionCode(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_VERSION_CODE, 0L);
    }

    static void setVersionName(final Context context) {
        getPreferencesEditor(context)
                .putString(PREF_KEY_VERSION_NAME, AppInformation.getVersionName(context))
                .apply();
    }

    static String getVersionName(final Context context) {
        return getPreferences(context).getString(PREF_KEY_VERSION_NAME, EMPTY_STRING);
    }

    static void dialogShown(final Context context) {
        if (getDialogFirstLaunchTime(context) == 0L) {
            setDialogFirstLaunchTime(context);
        }
        increment365DayPeriodDialogLaunchTimes(context);
    }

    static void setReminderToShowAgain(final Context context) {
        setLastTimeShown(context);
        setRemindLaunchesNumber(context);
    }
}