/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */
package com.vorlonsoft.android.rate

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.annotation.StyleRes
import com.vorlonsoft.android.rate.AppInformation.getLongVersionCode
import com.vorlonsoft.android.rate.AppInformation.getVersionName
import com.vorlonsoft.android.rate.Constants.Utils.LOG_MESSAGE_PART_1
import com.vorlonsoft.android.rate.DialogType.AnyDialogType
import com.vorlonsoft.android.rate.StoreType.*
import com.vorlonsoft.android.rate.Time.TimeUnits
import java.lang.ref.WeakReference
import java.util.*

/**
 *
 * AppRate Class - main class of the AndroidRate library, an thread-safe and fast singleton
 * implementation.
 *
 * @since    0.0.4
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */
class AppRate private constructor(context: Context) {

    companion object {
        private val TAG = AppRate::class.java.simpleName

        /**
         * The [AppRate] singleton object.
         * */
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: AppRate? = null

        @JvmStatic
        fun with(context: Context): AppRate = INSTANCE ?: synchronized(this) {
            INSTANCE ?: AppRate(context).also { INSTANCE = it }
        }

        /**
         *
         * Shows the Rate Dialog when conditions are met.
         *
         * Call this method at the end of your onCreate() method to determine whether
         * to show the Rate Dialog or not. It will check if the conditions are met and
         * show Rate Dialog if yes.
         *
         * @param activity your activity, use "this" in most cases
         * @return true if the Rate Dialog is shown, false otherwise
         */
        fun showRateDialogIfMeetsConditions(activity: Activity): Boolean {
            INSTANCE?.let {
                if (it.isDebug || it.shouldShowRateDialog()) {
                    it.showRateDialog(activity)
                    return true
                }
            }
            return false
        }

        /**
         *
         * QuickStart with the AndroidRate library's defaults.
         *
         * Monitors the app launch times and shows the Rate Dialog when **library's default**
         * conditions are met.
         *
         * @param activity your activity, use "this" in most cases
         * @return true if the Rate Dialog is shown, false otherwise
         * @see .monitor
         * @see .showRateDialogIfMeetsConditions
         */
        fun quickStart(activity: Activity): Boolean =
            with(activity).run {
                this.monitor()
                showRateDialogIfMeetsConditions(activity)
            }
    }

    private var customEventsCounts: MutableMap<String, Short> = mutableMapOf()

    /**
     * The context of the single, global Application object of the current process.
     * */
    private val context: Context
    private val dialogOptions = DialogOptions()
    private val storeOptions = StoreOptions()

    /**
     *
     * Checks if the library is in Debug mode. **For development only!**
     *
     * @return true if the library is in Debug mode, false otherwise
     */
    var isDebug = false
        private set

    private var isVersionCodeCheck = false
    private var isVersionNameCheck = false
    private var installDate = Time.DAY * 10L
    private var appLaunchTimes = 10.toByte()
    private var remindInterval = Time.DAY
    private var remindLaunchesNumber = 0.toByte()
    private var selectedAppLaunches = 1.toByte()

    /** Short.MAX_VALUE means unlimited occurrences of the display of the dialog within a 365-day period  */
    private var dialogLaunchTimes = Short.MAX_VALUE

    init {
        this.context = context.applicationContext
    }

    /**
     * Weak reference to the dialog object.
     * */
    private var dialog: WeakReference<Dialog?>? = null
    private var dialogManagerFactory: DialogManager.Factory = DefaultDialogManager.Factory()

    /**
     *
     * Sets weak reference to the dialog object.
     *
     * @param dialog weak reference to the dialog object
     */
    private fun setRateDialog(dialog: WeakReference<Dialog?>) {
        this.dialog = dialog
    }

    /**
     *
     * Clears weak reference dialog object. Invoking this method will not cause this
     * object to be enqueued.
     *
     * This method is invoked only by Java code; when the garbage collector
     * clears references it does so directly, without invoking this method.
     */
    private fun clearRateDialog() {
        dialog?.clear()
    }

    /**
     *
     * Sets the max number of occurrences of the display of the Rate Dialog within a 365-day
     * period.
     *
     * @param dialogLaunchTimes the max number of the display of the Rate Dialog within a 365-day
     * period, default is `Short.MAX_VALUE`, `Short.MAX_VALUE`
     * means unlimited occurrences
     * @return the [AppRate] singleton object
     */
    fun set365DayPeriodMaxNumberDialogLaunchTimes(dialogLaunchTimes: Short): AppRate = apply {
        this.dialogLaunchTimes = dialogLaunchTimes
    }

    /**
     *
     * Sets the minimum number of launches until the Rate Dialog pops up for
     * the first time.
     *
     * @param appLaunchTimes number of launches, default is 10, 3 means app is launched 3 or
     * more times
     * @return the [AppRate] singleton object
     */
    fun setLaunchTimes(appLaunchTimes: Byte): AppRate = apply {
        this.appLaunchTimes = appLaunchTimes
    }

    /**
     *
     * Sets the minimal number of days until the Rate Dialog pops up for the first time.
     *
     * @param installDate number of days, default is 10, 0 means install day, 10 means app is
     * launched 10 or more days later than installation
     * @return the [AppRate] singleton object
     * @see .setTimeToWait
     */
    fun setInstallDays(installDate: Byte): AppRate = apply {
        setTimeToWait(Time.DAY, installDate.toShort())
    }

    /**
     *
     * Sets the minimal number of time units until the Rate Dialog pops up for the first time.
     *
     * Default is 10 [days][Time.DAY], 0 means install millisecond, 10 means app is launched
     * 10 or more time units later than installation.
     *
     * @param timeUnit one of the values defined by [Time.TimeUnits]
     * @param timeUnitsNumber time units number
     * @return the [AppRate] singleton object
     * @see .setInstallDays
     * @see Time.TimeUnits
     */
    fun setTimeToWait(@TimeUnits timeUnit: Long, timeUnitsNumber: Short): AppRate = apply {
        installDate = timeUnit * timeUnitsNumber
    }

    /**
     *
     * Sets the minimal number of days until the Rate Dialog pops up for the next time after
     * neutral button clicked.
     *
     * @param remindInterval number of days, default is 1, 1 means app is launched 1 or more days
     * after neutral button clicked
     * @return the [AppRate] singleton object
     * @see .setRemindTimeToWait
     */
    fun setRemindInterval(remindInterval: Byte): AppRate = apply {
        setRemindTimeToWait(Time.DAY, remindInterval.toShort())
    }

    /**
     *
     * Sets the minimal number of time units until the Rate Dialog pops up for the next time
     * after neutral button clicked.
     *
     * Default is 1 [day][Time.DAY], 1 means app is launched 1 or more time units after
     * neutral button clicked.
     *
     * @param timeUnit one of the values defined by [Time.TimeUnits]
     * @param timeUnitsNumber time units number
     * @return the [AppRate] singleton object
     * @see .setRemindInterval
     * @see Time.TimeUnits
     */
    fun setRemindTimeToWait(@TimeUnits timeUnit: Long, timeUnitsNumber: Short): AppRate = apply {
        remindInterval = timeUnit * timeUnitsNumber
    }

    /**
     *
     * Sets the minimal number of app's launches after neutral button clicked until the Rating
     * Dialog pops up next time.
     *
     * @param remindLaunchesNumber number of app launches, default is 0, 1 means app is launched 1
     * or more times after neutral button clicked
     * @return the [AppRate] singleton object
     */
    fun setRemindLaunchesNumber(remindLaunchesNumber: Byte): AppRate = apply {
        this.remindLaunchesNumber = remindLaunchesNumber
    }

    /**
     *
     * Clears shared preferences that were set up by clicking the Remind Button.
     *
     * @return the [AppRate] singleton object
     */
    fun clearRemindButtonClick(): AppRate = apply {
        PreferenceHelper.clearRemindButtonClick(context)
    }

    fun setMinimumEventCount(eventName: String, minimumCount: Short): AppRate = apply {
        customEventsCounts[eventName] = minimumCount
    }

    /**
     *
     * Selects App launches.
     *
     * Method sets divisor for division of app launches with a remainder. This condition is
     * satisfied if `appLaunches % divisorAppLaunches == 0`
     *
     * @param selectedAppLaunches default is 1, 1 means each launch, 2 means every 2nd launch,
     * 3 means every 3rd launch, etc
     * @return the [AppRate] singleton object
     * @since 1.2.0
     */
    fun setSelectedAppLaunches(selectedAppLaunches: Byte): AppRate = apply {
        this.selectedAppLaunches = selectedAppLaunches
    }

    /**
     *
     * Selects App launches.
     *
     * Method sets divisor for division of app launches with a remainder. This condition is
     * satisfied if `appLaunches % divisorAppLaunches == 0`
     *
     * @param selectedAppLaunches default is 1, 1 means each launch, 2 means every 2nd launch,
     * 3 means every 3rd launch, etc
     * @return the [AppRate] singleton object
     * @see .setSelectedAppLaunches
     */
    @Deprecated("since 1.2.0", ReplaceWith("apply { setSelectedAppLaunches(selectedAppLaunches) }"))
    fun setRemindLaunchTimes(selectedAppLaunches: Byte): AppRate = apply {
        setSelectedAppLaunches(selectedAppLaunches)
    }

    /**
     *
     * Decides whether the Neutral button ("Remind Me Later") appears in the Rate Dialog or
     * not.
     *
     * @param isShowNeutralButton default is true, true means to show the Neutral button
     * @return the [AppRate] singleton object
     * @see .setTextLater
     * @see .setTextLater
     */
    fun setShowLaterButton(isShowNeutralButton: Boolean): AppRate = apply {
        dialogOptions.isShowNeutralButton = isShowNeutralButton
    }

    /**
     *
     * Decides if the Negative button appears in the Rate Dialog or not.
     *
     * @param isShowNeverButton default is true, true means to show the Negative button
     * @return the [AppRate] singleton object
     * @see .setTextNever
     * @see .setTextNever
     */
    fun setShowNeverButton(isShowNeverButton: Boolean): AppRate = apply {
        dialogOptions.isShowNegativeButton = isShowNeverButton
    }

    /**
     *
     * Sets whether the Rate Dialog should show the title.
     *
     * @param isShowTitle true to show the title, false otherwise
     * @return the [AppRate] singleton object
     * @see .setTitle
     * @see .setTitle
     */
    fun setShowTitle(isShowTitle: Boolean): AppRate = apply {
        dialogOptions.isShowTitle = isShowTitle
    }

    /**
     *
     * Sets whether the Rate Dialog should show the message.
     *
     * @param isShowMessage true to show the message, false otherwise
     * @return the [AppRate] singleton object
     * @see .setMessage
     * @see .setMessage
     */
    fun setShowMessage(isShowMessage: Boolean): AppRate = apply {
        dialogOptions.isShowMessage = isShowMessage
    }

    /**
     *
     * Sets whether the Rate Dialog should show the icon.
     *
     * @param isShowDialogIcon true to show the icon, false otherwise, default values are false for
     * [DialogType.CLASSIC] and true for
     * [DialogType.APPLE] and
     * [DialogType.MODERN]
     * @return the [AppRate] singleton object
     * @see .setDialogIcon
     * @see .setDialogIcon
     */
    fun setShowDialogIcon(isShowDialogIcon: Boolean): AppRate = apply {
        dialogOptions.isShowIcon = isShowDialogIcon
    }

    fun clearSettingsParam(): AppRate = apply {
        PreferenceHelper.clearSharedPreferences(context)
    }

    /**
     *
     * Clears agree to show Rate Dialog flag.
     *
     * @return the [AppRate] singleton object
     */
    fun clearAgreeShowDialog(): AppRate = apply {
        setAgreeShowDialog(true)
    }

    /**
     *
     * Returns agree to show Rate Dialog flag.
     *
     * @return agree to show Rate Dialog flag value
     */
    fun getAgreeShowDialog(): Boolean = PreferenceHelper.getIsAgreeShowDialog(context)

    /**
     *
     * Sets agree to show Rate Dialog flag.
     *
     * @param isAgree agree to show Rate Dialog flag
     * @return the [AppRate] singleton object
     */
    fun setAgreeShowDialog(isAgree: Boolean): AppRate = apply {
        PreferenceHelper.setIsAgreeShowDialog(context, isAgree)
    }

    fun setView(view: View?): AppRate = apply {
        dialogOptions.setView(view)
    }

    /**
     *
     * Specifies the callback when the button of Rate Dialog is pressed.
     *
     * @param listener implemented [OnClickButtonListener] Interface
     * @return the [AppRate] singleton object
     */
    fun setOnClickButtonListener(listener: OnClickButtonListener?): AppRate = apply {
        dialogOptions.setButtonListener(listener)
    }

    /**
     *
     * Sets the Rate Dialog icon.
     *
     * Takes precedence over values set using [.setDialogIcon].
     *
     * @param resourceId the Rate Dialog icon resource ID
     * @return the [AppRate] singleton object
     * @see .setDialogIcon
     * @see .setShowDialogIcon
     */
    fun setDialogIcon(resourceId: Int): AppRate = apply {
        dialogOptions.iconResId = resourceId
    }

    /**
     *
     * Sets the Rate Dialog icon.
     *
     * @param dialogIcon the Rate Dialog icon Drawable
     * @return the [AppRate] singleton object
     * @see .setDialogIcon
     * @see .setShowDialogIcon
     */
    fun setDialogIcon(dialogIcon: Drawable?): AppRate = apply {
        dialogOptions.setIcon(dialogIcon)
    }

    /**
     *
     * Sets the Rate Dialog title text.
     *
     * @param resourceId the Rate Dialog title text resource ID
     * @return the [AppRate] singleton object
     * @see .setTitle
     * @see .setShowTitle
     */
    fun setTitle(resourceId: Int): AppRate = apply {
        dialogOptions.titleTextResId = resourceId
    }

    /**
     *
     * Sets the Rate Dialog title text.
     *
     * Takes precedence over values set using [.setTitle].
     *
     * @param title the Rate Dialog title text
     * @return the [AppRate] singleton object
     * @see .setTitle
     * @see .setShowTitle
     */
    fun setTitle(title: String?): AppRate = apply {
        dialogOptions.setTitleText(title)
    }

    /**
     *
     * Sets the Rate Dialog message text.
     *
     * @param resourceId the Rate Dialog message text resource ID
     * @return the [AppRate] singleton object
     * @see .setMessage
     * @see .setShowMessage
     */
    fun setMessage(resourceId: Int): AppRate = apply {
        dialogOptions.messageTextResId = resourceId
    }

    /**
     *
     * Sets the Rate Dialog message text.
     *
     * Takes precedence over values set using [.setMessage].
     *
     * @param message the Rate Dialog message text
     * @return the [AppRate] singleton object
     * @see .setMessage
     * @see .setShowMessage
     */
    fun setMessage(message: String?): AppRate = apply {
        dialogOptions.setMessageText(message)
    }

    /**
     *
     * Sets the Rate Dialog positive button text.
     *
     * @param resourceId the Rate Dialog positive button text resource ID
     * @return the [AppRate] singleton object
     * @see .setTextRateNow
     */
    fun setTextRateNow(resourceId: Int): AppRate = apply {
        dialogOptions.positiveTextResId = resourceId
    }

    /**
     *
     * Sets the Rate Dialog positive button text.
     *
     * Takes precedence over values set using [.setTextRateNow].
     *
     * @param positiveText the Rate Dialog positive button text
     * @return the [AppRate] singleton object
     * @see .setTextRateNow
     */
    fun setTextRateNow(positiveText: String?): AppRate = apply {
        dialogOptions.setPositiveText(positiveText)
    }

    /**
     *
     * Sets the Rate Dialog neutral button text.
     *
     * @param resourceId the Rate Dialog neutral button text resource ID
     * @return the [AppRate] singleton object
     * @see .setTextLater
     * @see .setShowLaterButton
     */
    fun setTextLater(resourceId: Int): AppRate = apply {
        dialogOptions.neutralTextResId = resourceId
    }

    /**
     *
     * Sets the Rate Dialog neutral button text.
     *
     * Takes precedence over values set using [.setTextLater].
     *
     * @param neutralText the Rate Dialog neutral button text
     * @return the [AppRate] singleton object
     * @see .setTextLater
     * @see .setShowLaterButton
     */
    fun setTextLater(neutralText: String?): AppRate = apply {
        dialogOptions.setNeutralText(neutralText)
    }

    /**
     *
     * Sets the Rate Dialog negative button text.
     *
     * @param resourceId the Rate Dialog negative button text resource ID
     * @return the [AppRate] singleton object
     * @see .setTextNever
     * @see .setShowNeverButton
     */
    fun setTextNever(resourceId: Int): AppRate = apply {
        dialogOptions.negativeTextResId = resourceId
    }

    /**
     *
     * Sets the Rate Dialog negative button text.
     *
     * Takes precedence over values set using [.setTextNever].
     *
     * @param negativeText the Rate Dialog negative button text
     * @return the [AppRate] singleton object
     * @see .setTextNever
     * @see .setShowNeverButton
     */
    fun setTextNever(negativeText: String?): AppRate = apply {
        dialogOptions.setNegativeText(negativeText)
    }

    /**
     *
     * Sets whether the Rate Dialog is cancelable or not.
     *
     * @param cancelable default is false
     * @return the [AppRate] singleton object
     */
    fun setCancelable(cancelable: Boolean): AppRate {
        dialogOptions.cancelable = cancelable
        return this
    }

    /**
     *
     * Sets the Rate Dialog type.
     *
     * @param dialogType one of the values defined by [DialogType.AnyDialogType]
     * @return the [AppRate] singleton object
     */
    fun setDialogType(@AnyDialogType dialogType: Int): AppRate {
        dialogOptions.type = dialogType
        return this
    }

    /**
     *
     * Sets one of the app stores defined by [StoreType.StoreWithoutApplicationId] to
     * the Positive button.
     *
     * @param storeType one of the values defined by [StoreType.StoreWithoutApplicationId]
     * @return the [AppRate] singleton object
     * @throws IllegalArgumentException if `storeType` isn't defined by
     * [StoreType.StoreWithoutApplicationId]
     * @see .setStoreType
     * @see .setStoreType
     * @see .setStoreType
     */
    @Throws(IllegalArgumentException::class)
    fun setStoreType(@StoreWithoutApplicationId storeType: Int): AppRate {
        require(!(storeType == StoreType.APPLE || storeType == StoreType.BLACKBERRY)) {
            "For StoreType.APPLE/StoreType.BLACKBERRY you must" +
                    " use setStoreType(StoreType.APPLE/StoreType.BLACKBERRY, long applicationId)."
        }
        require(!(storeType < StoreType.AMAZON || storeType > StoreType.YANDEX)) {
            "StoreType must be one of: ${StoreType::AMAZON.name}, ${StoreType::APPLE.name}, " +
                    "${StoreType::BAZAAR.name}, " + "${StoreType::BLACKBERRY.name}, " +
                    "${StoreType::CHINESESTORES.name}, " + "${StoreType::GOOGLEPLAY.name}, " +
                    "${StoreType::MI.name}, ${StoreType::SAMSUNG.name}, " +
                    "${StoreType::SLIDEME.name}, ${StoreType::TENCENT.name}, ${StoreType::YANDEX.name}."
        }
        return setStoreTypeInternal(storeType)
    }

    /**
     *
     * Sets one of the app stores defined by [StoreType.StoreWithApplicationId] to
     * the Positive button.
     *
     * @param storeType one of the values defined by [StoreType.StoreWithApplicationId]
     * @param applicationId application ID in the `storeType` app store
     * @return the [AppRate] singleton object
     * @throws IllegalArgumentException if `storeType` isn't defined by
     * [StoreType.StoreWithApplicationId] or by
     * [StoreType.StoreWithoutApplicationId]
     * @see .setStoreType
     * @see .setStoreType
     * @see .setStoreType
     */
    @Throws(IllegalArgumentException::class)
    fun setStoreType(
        @StoreWithApplicationId storeType: Int,
        applicationId: Long
    ): AppRate {
        require(!(storeType < StoreType.AMAZON || storeType > StoreType.YANDEX)) {
            "StoreType must be one of: ${StoreType::AMAZON.name}, ${StoreType::APPLE.name}, " +
                    "${StoreType::BAZAAR.name}, " + "${StoreType::BLACKBERRY.name}, " +
                    "${StoreType::CHINESESTORES.name}, " + "${StoreType::GOOGLEPLAY.name}, " +
                    "${StoreType::MI.name}, ${StoreType::SAMSUNG.name}, " +
                    "${StoreType::SLIDEME.name}, ${StoreType::TENCENT.name}, ${StoreType::YANDEX.name}."
        }
        return if (storeType != StoreType.APPLE && storeType != StoreType.BLACKBERRY) setStoreType(
            storeType
        ) else setStoreTypeInternal(storeType, stringParam = arrayOf(applicationId.toString()))
    }

    /**
     *
     * Sets any other app store/stores to the Positive button.
     *
     * @param uris an RFC 2396-compliant URI or array of URIs to your app,
     * e. g. `https://otherstore.com/app?id=com.yourapp`
     * or `otherstore://apps/com.yourapp`
     * @return the [AppRate] singleton object
     * @throws IllegalArgumentException if `uris` equals null
     * @see .setStoreType
     * @see .setStoreType
     * @see .setStoreType
     */
    @Throws(IllegalArgumentException::class)
    fun setStoreType(vararg uris: String): AppRate {
        return setStoreTypeInternal(StoreType.OTHER, stringParam = arrayOf(*uris))
    }

    /**
     *
     * Sets custom action to the Positive button.
     *
     * For example, you can open your custom RateActivity when the Positive button clicked.
     *
     * @param intents any custom intent or array of intents,
     * first will be executed (`startActivity(intents[0])`), if first fails,
     * second will be executed (`startActivity(intents[1])`), etc.
     * @return the [AppRate] singleton object
     * @throws IllegalArgumentException if `intents` equals null
     * @see .setStoreType
     * @see .setStoreType
     * @see .setStoreType
     */
    @Throws(IllegalArgumentException::class)
    fun setStoreType(vararg intents: Intent): AppRate {
        return setStoreTypeInternal(StoreType.INTENT, intentParam = arrayOf(*intents))
    }

    private fun setStoreTypeInternal(
        @AnyStoreType storeType: Int,
        stringParam: Array<String>? = null,
        intentParam: Array<Intent>? = null
    ): AppRate = apply {
        storeOptions.setStoreType(storeType, stringParam, intentParam)
    }

    /**
     *
     * Gets the app store type from library options.
     *
     * NOTE: this method doesn't get an app store type from user's device.
     *
     * @return one of the values defined by [StoreType.AnyStoreType]
     */
    @AnyStoreType
    fun getStoreType(): Int = storeOptions.storeType

    fun incrementEventCount(eventName: String?): AppRate {
        return setEventCountValue(
            eventName,
            (PreferenceHelper.getCustomEventCount(context, eventName) + 1).toShort()
        )
    }

    fun setEventCountValue(eventName: String?, countValue: Short): AppRate = apply {
        PreferenceHelper.setCustomEventCount(context, eventName, countValue)
    }

    /**
     *
     * Sets dialog theme. You can use a specific theme to inflate the dialog.
     *
     * @param themeResId theme resource ID, default is 0
     * @return the [AppRate] singleton object
     */
    fun setThemeResId(@StyleRes themeResId: Int): AppRate = apply {
        dialogOptions.themeResId = themeResId
    }

    /**
     *
     * Sets [DialogManager.Factory] implementation.
     *
     * Call `AppRate.with(this).setDialogManagerFactory(null)` to set
     * [DefaultDialogManager.Factory] implementation.
     *
     * @param dialogManagerFactory object of class that implements [DialogManager.Factory],
     * default is [DefaultDialogManager.Factory] class object
     * @return the [AppRate] singleton object
     */
    fun setDialogManagerFactory(dialogManagerFactory: DialogManager.Factory?): AppRate = apply {
        this.dialogManagerFactory.clearDialogManager()
        if (dialogManagerFactory == null) {
            this.dialogManagerFactory = DefaultDialogManager.Factory()
        } else {
            dialogManagerFactory.clearDialogManager()
            this.dialogManagerFactory = dialogManagerFactory
        }
    }

    /**
     *
     * Sets the check whether the version code of the app is changed.
     *
     * @param isVersionCodeCheck true means to re-enable the Rate Dialog if a new version of app
     * with different version code is installed, default is false
     * @return the [AppRate] singleton object
     */
    fun setVersionCodeCheck(isVersionCodeCheck: Boolean): AppRate = apply {
        this.isVersionCodeCheck = isVersionCodeCheck
    }

    /**
     *
     * Sets the check whether the version name of the app is changed.
     *
     * @param isVersionNameCheck true means to re-enable the Rate Dialog if a new version of app
     * with different version name is installed, default is false
     * @return the [AppRate] singleton object
     */
    fun setVersionNameCheck(isVersionNameCheck: Boolean): AppRate = apply {
        this.isVersionNameCheck = isVersionNameCheck
    }

    /**
     *
     * Monitors launches of the application.
     *
     * Call this method when the `onCreate()` of the app's launcher activity or Application class
     * is called.
     */
    fun monitor() {
        if (PreferenceHelper.isFirstLaunch(context)) {
            PreferenceHelper.setFirstLaunchSharedPreferences(context)
        } else {
            PreferenceHelper.setLaunchTimes(
                context,
                (PreferenceHelper.getLaunchTimes(context) + 1).toShort()
            )
            if (getLongVersionCode(context) != PreferenceHelper.getVersionCode(context)) {
                if (isVersionCodeCheck) {
                    setAgreeShowDialog(true)
                }
                PreferenceHelper.setVersionCode(context)
            }
            if (getVersionName(context) != PreferenceHelper.getVersionName(context)) {
                if (isVersionNameCheck) {
                    setAgreeShowDialog(true)
                }
                PreferenceHelper.setVersionName(context)
            }
        }
    }

    /**
     *
     * Call this method directly if you want to force display of the Rate Dialog.
     *
     * Call it when some button presses on. Method also useful for testing purposes.
     *
     * @param activity your activity, use "this" in most cases
     */
    fun showRateDialog(activity: Activity) {
        var callDismissListener = false
        dialog?.let {
            it.get()?.let {
                callDismissListener = true
            }
        }
        try {
            dismissRateDialog()
            callDismissListener = false
        } catch (e: Exception) {
            Log.i(TAG, "Can't dismiss Rate Dialog, because unpredictable exception.", e)
            clearRateDialog()
        }
        setRateDialog(
            WeakReference(
                dialogManagerFactory
                    .createDialogManager(activity, dialogOptions, storeOptions).createDialog()
            )
        )
        dialog?.get()?.let {
            try {
                if (activity.isFinishing) {
                    Log.w(
                        TAG, "$LOG_MESSAGE_PART_1 can't show Rate Dialog, because " +
                                "activity is in the process of finishing."
                    )
                    return@let
                }
                if (activity.isDestroyed) {
                    Log.w(
                        TAG, "$LOG_MESSAGE_PART_1 can't show Rate Dialog, because " +
                                "the final #onDestroy() call has been made on the Activity, so " +
                                "this instance is now dead."
                    )
                    return@let
                }

                it.show()
                dialogOptions.isRedrawn = false

            } catch (e: Exception) {
                Log.w(
                    TAG,
                    "$LOG_MESSAGE_PART_1 can't show Rate Dialog, because unpredictable exception.",
                    e
                )
            }
        } ?: Log.w(TAG, "$LOG_MESSAGE_PART_1 can't create Rate Dialog.")

        if (callDismissListener) {
            (dialogManagerFactory
                .createDialogManager(
                    context,
                    dialogOptions,
                    storeOptions
                ) as DefaultDialogManager).dismissListener.onDismiss(null)
        }
    }

    /**
     *
     * Checks whether the Rate Dialog is currently showing.
     *
     * @return true if the Rate Dialog is currently showing, false otherwise.
     */
    fun isShowingRateDialog(): Boolean = dialog?.get()?.isShowing == true

    /**
     *
     * Dismisses Rate Dialog, removing it from the screen, and
     * clears weak reference dialog object.
     *
     * This method can be invoked safely from any thread.
     */
    fun dismissRateDialog() {
        dialog?.let {
            it.get()?.let { actualDialog ->
                if (actualDialog.isShowing) {
                    actualDialog.dismiss()
                }
            }
        }
        clearRateDialog()
    }

    /**
     *
     * Call this method directly if you want to send a user to rate your app right in the app
     * store.
     *
     * @param activity your activity, use "this" in most cases
     */
    fun rateNow(activity: Activity) {
        (dialogManagerFactory
            .createDialogManager(
                activity,
                dialogOptions,
                storeOptions
            ) as DefaultDialogManager).positiveListener.onClick(
            dialog?.get(),
            DialogInterface.BUTTON_POSITIVE
        )
        dismissRateDialog()
    }

    /**
     *
     * Determines whether conditions to show the Rate Dialog meets or not.
     *
     * @return true if the conditions to show the Rate Dialog meets, false otherwise
     */
    fun shouldShowRateDialog(): Boolean {
        return getAgreeShowDialog() &&
                isOverLaunchTimes() &&
                isSelectedAppLaunch() &&
                isOverInstallDate() &&
                isOverRemindDate() &&
                isOverRemindLaunchesNumber() &&
                isOverCustomEventsRequirements() &&
                isBelow365DayPeriodMaxNumberDialogLaunchTimes()
    }

    private fun isOverDate(targetDate: Long, threshold: Long): Boolean {
        return Date().time - targetDate >= threshold
    }

    private fun isOverLaunchTimes(): Boolean =
        appLaunchTimes.toInt() == 0 || PreferenceHelper.getLaunchTimes(context) >= appLaunchTimes

    private fun isSelectedAppLaunch(): Boolean = selectedAppLaunches.toInt() == 1 ||
            selectedAppLaunches.toInt() != 0 && PreferenceHelper.getLaunchTimes(context) % selectedAppLaunches == 0

    private fun isOverInstallDate(): Boolean = installDate == 0L || isOverDate(
        PreferenceHelper.getInstallDate(context),
        installDate
    )

    private fun isOverRemindDate(): Boolean = remindInterval == 0L ||
            PreferenceHelper.getRemindInterval(context) == 0L ||
            isOverDate(PreferenceHelper.getRemindInterval(context), remindInterval)

    private fun isOverRemindLaunchesNumber(): Boolean = remindLaunchesNumber.toInt() == 0 ||
            PreferenceHelper.getRemindLaunchesNumber(context).toInt() == 0 ||
            PreferenceHelper.getLaunchTimes(context) - PreferenceHelper.getRemindLaunchesNumber(
        context
    ) >= remindLaunchesNumber

    private fun isBelow365DayPeriodMaxNumberDialogLaunchTimes(): Boolean =
        dialogLaunchTimes == Short.MAX_VALUE ||
                PreferenceHelper.get365DayPeriodDialogLaunchTimes(context) < dialogLaunchTimes

    private fun isOverCustomEventsRequirements(): Boolean {
        if (customEventsCounts.isEmpty()) {
            return true
        }
        // checking if at least one custom event count is below the expected value
        val unmetCustomEvents = customEventsCounts.entries.firstOrNull {
            PreferenceHelper.getCustomEventCount(
                context,
                it.key
            ) < it.value
        }
        // if null, all custom events have counts bigger than or are equal to the expected values
        return unmetCustomEvents == null
    }

    /**
     *
     * Debug mode. **For development only!**
     *
     * Setting the library to Debug mode ensures that the Rate Dialog will be shown each time
     * the app is launched.
     *
     * @param isDebug default is false, true ensures that the Rate Dialog will be shown each time
     * the app is launched
     * @return the [AppRate] singleton object
     */
    fun setDebug(isDebug: Boolean): AppRate = apply {
        this.isDebug = isDebug
    }
}