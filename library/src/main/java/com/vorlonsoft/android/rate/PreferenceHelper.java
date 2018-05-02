/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

final class PreferenceHelper {

    private static final String PREF_FILE_NAME = "androidrate_pref_file";

    /**
     * The key prefix for each custom event,
     * so that there is no clash with existing keys (PREF_KEY_INSTALL_DATE etc.)
     */
    private static final String PREF_KEY_CUSTOM_EVENT_PREFIX = "androidrate_custom_event_prefix_";

    private static final String PREF_KEY_INSTALL_DATE = "androidrate_install_date";

    private static final String PREF_KEY_IS_AGREE_SHOW_DIALOG = "androidrate_is_agree_show_dialog";

    private static final String PREF_KEY_LAUNCH_TIMES = "androidrate_launch_times";

    private static final String PREF_KEY_REMIND_INTERVAL = "androidrate_remind_interval";

    private PreferenceHelper() {
        throw new AssertionError();
    }

    private static SharedPreferences getPreferences(final Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getPreferencesEditor(final Context context) {
        return getPreferences(context).edit();
    }

    /**
     * Clear data in shared preferences.<br>
     *
     * @param context context
     */
    static void clearSharedPreferences(final Context context) {
        final SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.clear();
        editor.apply();
    }

    static boolean isFirstLaunch(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0L) == 0L;
    }

    static void setFirstLaunchSharedPreferences(final Context context) {
        final SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putLong(PREF_KEY_INSTALL_DATE, new Date().getTime());
        if (getIsAgreeShowDialog(context)) {                          //if (get() == true) set(true); - NOT error!
            editor.putBoolean(PREF_KEY_IS_AGREE_SHOW_DIALOG, true);
        }
        editor.putInt(PREF_KEY_LAUNCH_TIMES, 1);
        editor.putLong(PREF_KEY_REMIND_INTERVAL, 0);
        editor.apply();
    }

    static void setCustomEventCount(final Context context, final String eventName, final short eventCount) {
        final String eventKey = PREF_KEY_CUSTOM_EVENT_PREFIX + eventName;
        final SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(eventKey, eventCount);
        editor.apply();
    }

    static short getCustomEventCount(final Context context, final String eventName) {
        final String eventKey = PREF_KEY_CUSTOM_EVENT_PREFIX + eventName;
        return (short) getPreferences(context).getInt(eventKey, 0);
    }

    @SuppressWarnings("unused")
    static void setInstallDate(final Context context) {
        final SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putLong(PREF_KEY_INSTALL_DATE, new Date().getTime());
        editor.apply();
    }

    static long getInstallDate(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0L);
    }

    /**
     * Set agree flag about show dialog.<br>
     * If it is false, rate dialog will never shown unless data is cleared.
     *
     * @param context context
     * @param isAgree agree with showing rate dialog
     */
    static void setIsAgreeShowDialog(final Context context, final boolean isAgree) {
        final SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putBoolean(PREF_KEY_IS_AGREE_SHOW_DIALOG, isAgree);
        editor.apply();
    }

    static boolean getIsAgreeShowDialog(final Context context) {
        return getPreferences(context).getBoolean(PREF_KEY_IS_AGREE_SHOW_DIALOG, true);
    }

    static void setLaunchTimes(final Context context, final short launchTimes) {
        final SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(PREF_KEY_LAUNCH_TIMES, launchTimes);
        editor.apply();
    }

    static short getLaunchTimes(final Context context) {
        return (short) getPreferences(context).getInt(PREF_KEY_LAUNCH_TIMES, 0);
    }


    static void setRemindInterval(final Context context) {
        final SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putLong(PREF_KEY_REMIND_INTERVAL, new Date().getTime());
        editor.apply();
    }

    static long getRemindInterval(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_REMIND_INTERVAL, 0L);
    }

}