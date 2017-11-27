/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import com.vorlonsoft.android.rate.PreferenceHelper.getCustomEventCount
import com.vorlonsoft.android.rate.PreferenceHelper.getInstallDate
import com.vorlonsoft.android.rate.PreferenceHelper.getIsAgreeShowDialog

import java.util.Date
import java.util.HashMap

import com.vorlonsoft.android.rate.PreferenceHelper.getLaunchTimes
import com.vorlonsoft.android.rate.PreferenceHelper.getRemindInterval
import com.vorlonsoft.android.rate.PreferenceHelper.isFirstLaunch
import com.vorlonsoft.android.rate.PreferenceHelper.setCustomEventCount
import com.vorlonsoft.android.rate.PreferenceHelper.setInstallDate
import java.lang.ref.Reference

class AppRate private constructor(context: Context) {

    private val context: Context

    private val options = DialogOptions()

    private var installDate = 10

    private var launchTimes = 10

    private var remindInterval = 1

    private val customEventCounts = HashMap<String, Long>()

    private var remindLaunchTimes = 1

    private var isDebug = false

    private var dialogManagerFactory: DialogManager.Factory = DefaultDialogManager.Factory()

    private val isOverLaunchTimes: Boolean
        get() = getLaunchTimes(context) >= launchTimes

    private val isOverRemindLaunchTimes: Boolean
        get() = remindLaunchTimes != 0 && getLaunchTimes(context) % remindLaunchTimes == 0

    private val isOverInstallDate: Boolean
        get() = isOverDate(getInstallDate(context), installDate)

    private val isOverRemindDate: Boolean
        get() = isOverDate(getRemindInterval(context), remindInterval)

    private val isOverCustomEventRequirements: Boolean
        get() {
            for ((key, value) in customEventCounts) {
                val currentCount = getCustomEventCount(context, key)
                if (currentCount < value) {
                    return false
                }
            }
            return true
        }

    init {
        this.context = context.applicationContext
    }

    fun setLaunchTimes(launchTimes: Int): AppRate {
        this.launchTimes = launchTimes
        return this
    }

    fun setInstallDays(installDate: Int): AppRate {
        this.installDate = installDate
        return this
    }

    fun setRemindInterval(remindInterval: Int): AppRate {
        this.remindInterval = remindInterval
        return this
    }

    fun setMinimumEventCount(eventName: String, minimumCount: Long): AppRate {
        this.customEventCounts.put(eventName, minimumCount)
        return this
    }

    fun setRemindLaunchTimes(remindLaunchTimes: Int): AppRate {
        this.remindLaunchTimes = remindLaunchTimes
        return this
    }

    fun setShowLaterButton(isShowNeutralButton: Boolean): AppRate {
        options.setShowNeutralButton(isShowNeutralButton)
        return this
    }

    fun setShowNeverButton(isShowNeverButton: Boolean): AppRate {
        options.setShowNegativeButton(isShowNeverButton)
        return this
    }

    fun setShowTitle(isShowTitle: Boolean): AppRate {
        options.setShowTitle(isShowTitle)
        return this
    }

    fun clearAgreeShowDialog(): AppRate {
        PreferenceHelper.setAgreeShowDialog(context, true)
        return this
    }

    fun clearSettingsParam(): AppRate {
        PreferenceHelper.setAgreeShowDialog(context, true)
        PreferenceHelper.clearSharedPreferences(context)
        return this
    }

    fun setAgreeShowDialog(clear: Boolean): AppRate {
        PreferenceHelper.setAgreeShowDialog(context, clear)
        return this
    }

    fun setView(view: View): AppRate {
        options.view = view
        return this
    }

    fun setOnClickButtonListener(listener: Reference<OnClickButtonListener>?): AppRate {
        options.listener = listener
        return this
    }

    fun setTitle(resourceId: Int): AppRate {
        options.titleResId = resourceId
        return this
    }

    fun setTitle(title: String): AppRate {
        options.setTitleText(title)
        return this
    }

    fun setMessage(resourceId: Int): AppRate {
        options.messageResId = resourceId
        return this
    }

    fun setMessage(message: String): AppRate {
        options.setMessageText(message)
        return this
    }

    fun setTextRateNow(resourceId: Int): AppRate {
        options.textPositiveResId = resourceId
        return this
    }

    fun setTextRateNow(positiveText: String): AppRate {
        options.setPositiveText(positiveText)
        return this
    }

    fun setTextLater(resourceId: Int): AppRate {
        options.textNeutralResId = resourceId
        return this
    }

    fun setTextLater(neutralText: String): AppRate {
        options.setNeutralText(neutralText)
        return this
    }

    fun setTextNever(resourceId: Int): AppRate {
        options.textNegativeResId = resourceId
        return this
    }

    fun setTextNever(negativeText: String): AppRate {
        options.setNegativeText(negativeText)
        return this
    }

    fun setCancelable(cancelable: Boolean): AppRate {
        options.cancelable = cancelable
        return this
    }

    fun setStoreType(appstore: StoreType): AppRate {
        options.storeType = appstore
        return this
    }

    fun incrementEventCount(eventName: String): AppRate {
        return setEventCountValue(eventName, getCustomEventCount(context, eventName) + 1)
    }

    fun setEventCountValue(eventName: String, countValue: Long): AppRate {
        setCustomEventCount(context, eventName, countValue)
        return this
    }

    fun setThemeResId(pThemeResId: Int?): AppRate {
        options.themeResId = pThemeResId
        return this
    }

    fun setDialogManagerFactory(dialogManagerFactory: DialogManager.Factory): AppRate {
        this.dialogManagerFactory = dialogManagerFactory
        return this
    }

    fun monitor() {
        if (isFirstLaunch(context)) {
            setInstallDate(context)
        }
        PreferenceHelper.setLaunchTimes(context, getLaunchTimes(context) + 1)
    }

    fun showRateDialog(activity: Activity) {
        if (!activity.isFinishing) {
            dialogManagerFactory.createDialogManager(activity, options).createDialog().show()
        }
    }

    fun shouldShowRateDialog(): Boolean {
        return getIsAgreeShowDialog(context) &&
                isOverLaunchTimes &&
                isOverRemindLaunchTimes &&
                isOverInstallDate &&
                isOverRemindDate &&
                isOverCustomEventRequirements
    }

    fun isDebug(): Boolean {
        return isDebug
    }

    fun setDebug(isDebug: Boolean): AppRate {
        this.isDebug = isDebug
        return this
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var singleton: AppRate? = null

        fun with(context: Context): AppRate? {
            if (singleton == null) {
                synchronized(AppRate::class.java) {
                    if (singleton == null) {
                        singleton = AppRate(context)
                    }
                }
            }
            return singleton
        }

        fun showRateDialogIfMeetsConditions(activity: Activity): Boolean {
            val isMeetsConditions = singleton!!.isDebug || singleton!!.shouldShowRateDialog()
            if (isMeetsConditions) {
                singleton!!.showRateDialog(activity)
            }
            return isMeetsConditions
        }

        private fun isOverDate(targetDate: Long, threshold: Int): Boolean {
            return Date().time - targetDate >= threshold * 24 * 60 * 60 * 1000
        }
    }

}
