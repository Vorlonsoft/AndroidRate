/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

import java.util.Date

internal object PreferenceHelper {

    private val PREF_FILE_NAME = "androidratekotlin_pref_file"

    private val PREF_KEY_INSTALL_DATE = "androidratekotlin_install_date"

    private val PREF_KEY_LAUNCH_TIMES = "androidratekotlin_launch_times"

    private val PREF_KEY_IS_AGREE_SHOW_DIALOG = "androidratekotlin_is_agree_show_dialog"

    private val PREF_KEY_REMIND_INTERVAL = "androidratekotlin_remind_interval"

    private val PREF_CUSTOM_EVENT_KEY_PREFIX = "androidratekotlin_custom_event_prefix_"

    fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun getPreferencesEditor(context: Context): Editor {
        return getPreferences(context).edit()
    }

    /**
     * Clear data in shared preferences.<br></br>
     *
     * @param context context
     */
    fun clearSharedPreferences(context: Context) {
        val editor = getPreferencesEditor(context)
        editor.remove(PREF_KEY_INSTALL_DATE)
        editor.remove(PREF_KEY_LAUNCH_TIMES)
        editor.apply()
    }

    /**
     * Set agree flag about show dialog.<br></br>
     * If it is false, rate dialog will never shown unless data is cleared.
     *
     * @param context context
     * @param isAgree agree with showing rate dialog
     */
    fun setAgreeShowDialog(context: Context, isAgree: Boolean) {
        val editor = getPreferencesEditor(context)
        editor.putBoolean(PREF_KEY_IS_AGREE_SHOW_DIALOG, isAgree)
        editor.apply()
    }

    fun getIsAgreeShowDialog(context: Context): Boolean {
        return getPreferences(context).getBoolean(PREF_KEY_IS_AGREE_SHOW_DIALOG, true)
    }

    fun setRemindInterval(context: Context) {
        val editor = getPreferencesEditor(context)
        editor.remove(PREF_KEY_REMIND_INTERVAL)
        editor.putLong(PREF_KEY_REMIND_INTERVAL, Date().time)
        editor.apply()
    }

    fun getRemindInterval(context: Context): Long {
        return getPreferences(context).getLong(PREF_KEY_REMIND_INTERVAL, 0)
    }

    fun setInstallDate(context: Context) {
        val editor = getPreferencesEditor(context)
        editor.putLong(PREF_KEY_INSTALL_DATE, Date().time)
        editor.apply()
    }

    fun getInstallDate(context: Context): Long {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0)
    }

    fun setLaunchTimes(context: Context, launchTimes: Int) {
        val editor = getPreferencesEditor(context)
        editor.putInt(PREF_KEY_LAUNCH_TIMES, launchTimes)
        editor.apply()
    }

    fun getLaunchTimes(context: Context): Int {
        return getPreferences(context).getInt(PREF_KEY_LAUNCH_TIMES, 0)
    }

    fun isFirstLaunch(context: Context): Boolean {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0) == 0L
    }

    /**
     * Add a prefix for the key for each custom event,
     * so that there is no clash with existing keys (PREF_KEY_LAUNCH_TIME, PREF_KEY_INSTALL_DATE, etc.)
     */
    fun getCustomEventCount(context: Context, eventName: String): Long {
        val eventKey = PREF_CUSTOM_EVENT_KEY_PREFIX + eventName
        return getPreferences(context).getLong(eventKey, 0)
    }

    fun setCustomEventCount(context: Context, eventName: String, eventCount: Long) {
        val eventKey = PREF_CUSTOM_EVENT_KEY_PREFIX + eventName
        val editor = getPreferencesEditor(context)
        editor.putLong(eventKey, eventCount)
        editor.apply()
    }
}