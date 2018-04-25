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

    private static final String PREF_KEY_INSTALL_DATE = "androidrate_install_date";

    private static final String PREF_KEY_LAUNCH_TIMES = "androidrate_launch_times";

    private static final String PREF_KEY_IS_AGREE_SHOW_DIALOG = "androidrate_is_agree_show_dialog";

    private static final String PREF_KEY_REMIND_INTERVAL = "androidrate_remind_interval";

    /**
     * The key prefix for each custom event,
     * so that there is no clash with existing keys (PREF_KEY_LAUNCH_TIMES, PREF_KEY_INSTALL_DATE, etc.)
     */
    private static final String PREF_KEY_CUSTOM_EVENT_PREFIX = "androidrate_custom_event_prefix_";

    private PreferenceHelper() {
    }

    private static SharedPreferences getPreferences(final Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getPreferencesEditor(final Context context) {
        return getPreferences(context).edit();
    }

    /**
     * Clear data in shared preferences.
     *
     * @param context context
     */
    static void clearSharedPreferences(final Context context) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.remove(PREF_KEY_INSTALL_DATE);
        editor.remove(PREF_KEY_LAUNCH_TIMES);
        editor.apply();
    }

    /**
     * Set agree flag about show dialog.<br>
     * If it is false, rate dialog will never shown unless data is cleared.
     *
     * @param context context
     * @param isAgree agree with showing rate dialog
     */
    static void setAgreeShowDialog(final Context context,final boolean isAgree) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putBoolean(PREF_KEY_IS_AGREE_SHOW_DIALOG, isAgree);
        editor.apply();
    }

    static boolean getIsAgreeShowDialog(final Context context) {
        return getPreferences(context).getBoolean(PREF_KEY_IS_AGREE_SHOW_DIALOG, true);
    }

    static void setRemindInterval(final Context context) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putLong(PREF_KEY_REMIND_INTERVAL, new Date().getTime());
        editor.apply();
    }

    static long getRemindInterval(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_REMIND_INTERVAL, 0L);
    }

    static void setInstallDate(final Context context) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putLong(PREF_KEY_INSTALL_DATE, new Date().getTime());
        editor.apply();
    }

    static long getInstallDate(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0L);
    }

    static void setLaunchTimes(final Context context, final short launchTimes) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(PREF_KEY_LAUNCH_TIMES, launchTimes);
        editor.apply();
    }

    static short getLaunchTimes(final Context context) {
        return (short) getPreferences(context).getInt(PREF_KEY_LAUNCH_TIMES, 0);
    }

    static boolean isFirstLaunch(final Context context) {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0L) == 0L;
    }

    static short getCustomEventCount(final Context context, final String eventName) {
        String eventKey = PREF_KEY_CUSTOM_EVENT_PREFIX + eventName;
        return (short) getPreferences(context).getInt(eventKey, 0);
    }

    static void setCustomEventCount(final Context context, final String eventName, final short eventCount) {
        String eventKey = PREF_KEY_CUSTOM_EVENT_PREFIX + eventName;
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(eventKey, eventCount);
        editor.apply();
    }
}