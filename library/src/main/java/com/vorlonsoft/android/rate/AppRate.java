/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.vorlonsoft.android.rate.Constants.Utils.LOG_MESSAGE_PART_1;
import static com.vorlonsoft.android.rate.Constants.Utils.TAG;
import static com.vorlonsoft.android.rate.PreferenceHelper.get365DayPeriodDialogLaunchTimes;
import static com.vorlonsoft.android.rate.PreferenceHelper.getCustomEventCount;
import static com.vorlonsoft.android.rate.PreferenceHelper.getInstallDate;
import static com.vorlonsoft.android.rate.PreferenceHelper.getIsAgreeShowDialog;
import static com.vorlonsoft.android.rate.PreferenceHelper.getLaunchTimes;
import static com.vorlonsoft.android.rate.PreferenceHelper.getRemindInterval;
import static com.vorlonsoft.android.rate.PreferenceHelper.getRemindLaunchesNumber;
import static com.vorlonsoft.android.rate.PreferenceHelper.getVersionCode;
import static com.vorlonsoft.android.rate.PreferenceHelper.getVersionName;
import static com.vorlonsoft.android.rate.PreferenceHelper.isFirstLaunch;
import static com.vorlonsoft.android.rate.PreferenceHelper.setCustomEventCount;
import static com.vorlonsoft.android.rate.PreferenceHelper.setFirstLaunchSharedPreferences;
import static com.vorlonsoft.android.rate.PreferenceHelper.setIsAgreeShowDialog;
import static com.vorlonsoft.android.rate.PreferenceHelper.setVersionCode;
import static com.vorlonsoft.android.rate.PreferenceHelper.setVersionName;
import static com.vorlonsoft.android.rate.StoreType.AMAZON;
import static com.vorlonsoft.android.rate.StoreType.APPLE;
import static com.vorlonsoft.android.rate.StoreType.BLACKBERRY;
import static com.vorlonsoft.android.rate.StoreType.INTENT;
import static com.vorlonsoft.android.rate.StoreType.OTHER;
import static com.vorlonsoft.android.rate.StoreType.YANDEX;

/**
 * <p>AppRate Class - main class of the AndroidRate library, an thread-safe and fast singleton
 * implementation.</p>
 *
 * @since    0.0.4
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */
public final class AppRate {
    /** <p>The {@link AppRate} singleton object.</p> */
    @SuppressLint("StaticFieldLeak")
    private static volatile AppRate singleton = null;
    private final Map<String, Short> customEventsCounts;
    /** <p>The context of the single, global Application object of the current process.</p> */
    private final Context context;
    private final DialogOptions dialogOptions = new DialogOptions();
    private final StoreOptions storeOptions = new StoreOptions();
    private boolean isDebug = false;
    private boolean isVersionCodeCheck = false;
    private boolean isVersionNameCheck = false;
    private long installDate = Time.DAY * 10L;
    private byte appLaunchTimes = (byte) 10;
    private long remindInterval = Time.DAY;
    private byte remindLaunchesNumber = (byte) 0;
    private byte selectedAppLaunches = (byte) 1;
    /** Short.MAX_VALUE means unlimited occurrences of the display of the dialog within a 365-day period */
    private short dialogLaunchTimes = Short.MAX_VALUE;
    /** <p>Weak reference to the dialog object.</p> */
    private WeakReference<Dialog> dialog = null;
    private DialogManager.Factory dialogManagerFactory = new DefaultDialogManager.Factory();

    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            customEventsCounts = new ArrayMap<>();
        } else {
            customEventsCounts = new HashMap<>();
        }
    }

    private AppRate(final Context context) {
        this.context = context.getApplicationContext();
    }

    public static AppRate with(final Context context) {
        if (singleton == null) {
            synchronized (AppRate.class) {
                if (singleton == null) {
                    singleton = new AppRate(context);
                }
            }
        }
        return singleton;
    }

    /**
     * <p>Shows the Rate Dialog when conditions are met.</p>
     * <p>Call this method at the end of your onCreate() method to determine whether
     * to show the Rate Dialog or not. It will check if the conditions are met and
     * show Rate Dialog if yes.</p>
     *
     * @param activity your activity, use "this" in most cases
     * @return true if the Rate Dialog is shown, false otherwise
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean showRateDialogIfMeetsConditions(Activity activity) {
        final boolean isMeetsConditions = ((singleton != null) &&
                                           (singleton.isDebug() || singleton.shouldShowRateDialog()));
        if (isMeetsConditions) {
            singleton.showRateDialog(activity);
        }
        return isMeetsConditions;
    }

    /**
     * <p>QuickStart with the AndroidRate library's defaults.</p>
     * <p>Monitors the app launch times and shows the Rate Dialog when <b>library's default</b>
     * conditions are met.</p>
     *
     * @param activity your activity, use "this" in most cases
     * @return true if the Rate Dialog is shown, false otherwise
     * @see #monitor()
     * @see #showRateDialogIfMeetsConditions(Activity)
     */
    @SuppressWarnings("unused")
    public static boolean quickStart(Activity activity) {
        with(activity).monitor();
        return showRateDialogIfMeetsConditions(activity);
    }

    private boolean isOverDate(long targetDate, long threshold) {
        return new Date().getTime() - targetDate >= threshold;
    }

    private boolean isBelow365DayPeriodMaxNumberDialogLaunchTimes() {
        return ((dialogLaunchTimes == Short.MAX_VALUE) ||
                (get365DayPeriodDialogLaunchTimes(context) < dialogLaunchTimes));
    }

    /**
     * <p>Sets weak reference to the dialog object.</p>
     *
     * @param dialog weak reference to the dialog object
     */
    private void setRateDialog(@NonNull WeakReference<Dialog> dialog) {
        this.dialog = dialog;
    }

    /**
     * <p>Clears weak reference dialog object. Invoking this method will not cause this
     * object to be enqueued.</p>
     * <p>This method is invoked only by Java code; when the garbage collector
     * clears references it does so directly, without invoking this method.</p>
     */
    private void clearRateDialog() {
        if (dialog != null) {
            dialog.clear();
        }
    }

    /**
     * <p>Sets the max number of occurrences of the display of the Rate Dialog within a 365-day
     * period.</p>
     *
     * @param dialogLaunchTimes the max number of the display of the Rate Dialog within a 365-day
     *                          period, default is {@code Short.MAX_VALUE}, {@code Short.MAX_VALUE}
     *                          means unlimited occurrences
     * @return the {@link AppRate} singleton object
     */
    @SuppressWarnings({"unused"})
    public AppRate set365DayPeriodMaxNumberDialogLaunchTimes(short dialogLaunchTimes) {
        this.dialogLaunchTimes = dialogLaunchTimes;
        return this;
    }

    /**
     * <p>Sets the minimum number of launches until the Rate Dialog pops up for
     * the first time.</p>
     *
     * @param appLaunchTimes number of launches, default is 10, 3 means app is launched 3 or
     *                       more times
     * @return the {@link AppRate} singleton object
     */
    public AppRate setLaunchTimes(@SuppressWarnings("SameParameterValue") byte appLaunchTimes) {
        this.appLaunchTimes = appLaunchTimes;
        return this;
    }

    /**
     * <p>Sets the minimal number of days until the Rate Dialog pops up for the first time.</p>
     *
     * @param installDate number of days, default is 10, 0 means install day, 10 means app is
     *                    launched 10 or more days later than installation
     * @return the {@link AppRate} singleton object
     * @see #setTimeToWait(long, short)
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public AppRate setInstallDays(byte installDate) {
        return setTimeToWait(Time.DAY, installDate);
    }

    /**
     * <p>Sets the minimal number of time units until the Rate Dialog pops up for the first time.</p>
     * <p>Default is 10 {@link Time#DAY days}, 0 means install millisecond, 10 means app is launched
     * 10 or more time units later than installation.</p>
     *
     * @param timeUnit one of the values defined by {@link Time.TimeUnits}
     * @param timeUnitsNumber time units number
     * @return the {@link AppRate} singleton object
     * @see #setInstallDays(byte)
     * @see Time.TimeUnits
     */
    @SuppressWarnings("WeakerAccess")
    public AppRate setTimeToWait(@Time.TimeUnits long timeUnit, short timeUnitsNumber) {
        this.installDate = timeUnit * timeUnitsNumber;
        return this;
    }

    /**
     * <p>Sets the minimal number of days until the Rate Dialog pops up for the next time after
     * neutral button clicked.</p>
     *
     * @param remindInterval number of days, default is 1, 1 means app is launched 1 or more days
     *                       after neutral button clicked
     * @return the {@link AppRate} singleton object
     * @see #setRemindTimeToWait(long, short)
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public AppRate setRemindInterval(byte remindInterval) {
        return setRemindTimeToWait(Time.DAY, remindInterval);
    }

    /**
     * <p>Sets the minimal number of time units until the Rate Dialog pops up for the next time
     * after neutral button clicked.</p>
     * <p>Default is 1 {@link Time#DAY day}, 1 means app is launched 1 or more time units after
     * neutral button clicked.</p>
     *
     * @param timeUnit one of the values defined by {@link Time.TimeUnits}
     * @param timeUnitsNumber time units number
     * @return the {@link AppRate} singleton object
     * @see #setRemindInterval(byte)
     * @see Time.TimeUnits
     */
    @SuppressWarnings("WeakerAccess")
    public AppRate setRemindTimeToWait(@Time.TimeUnits long timeUnit, short timeUnitsNumber) {
        this.remindInterval = timeUnit * timeUnitsNumber;
        return this;
    }

    /**
     * <p>Sets the minimal number of app's launches after neutral button clicked until the Rating
     * Dialog pops up next time.</p>
     *
     * @param remindLaunchesNumber number of app launches, default is 0, 1 means app is launched 1
     *                             or more times after neutral button clicked
     * @return the {@link AppRate} singleton object
     */
    @SuppressWarnings("unused")
    public AppRate setRemindLaunchesNumber(@SuppressWarnings("SameParameterValue") byte remindLaunchesNumber) {
        this.remindLaunchesNumber = remindLaunchesNumber;
        return this;
    }

    /**
     * <p>Clears shared preferences that were set up by clicking the Remind Button.</p>
     *
     * @return the {@link AppRate} singleton object
     */
    @SuppressWarnings("unused")
    public AppRate clearRemindButtonClick() {
        PreferenceHelper.clearRemindButtonClick(context);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setMinimumEventCount(String eventName, short minimumCount) {
        this.customEventsCounts.put(eventName, minimumCount);
        return this;
    }

    /**
     * <p>Selects App launches.</p>
     * <p>Method sets divisor for division of app launches with a remainder. This condition is
     * satisfied if {@code appLaunches % divisorAppLaunches == 0}</p>
     *
     * @param selectedAppLaunches default is 1, 1 means each launch, 2 means every 2nd launch,
     *                            3 means every 3rd launch, etc
     * @return the {@link AppRate} singleton object
     * @since 1.2.0
     */
    @SuppressWarnings("WeakerAccess")
    public AppRate setSelectedAppLaunches(@SuppressWarnings("SameParameterValue") byte selectedAppLaunches) {
        this.selectedAppLaunches = selectedAppLaunches;
        return this;
    }

    /**
     * <p>Selects App launches.</p>
     * <p>Method sets divisor for division of app launches with a remainder. This condition is
     * satisfied if {@code appLaunches % divisorAppLaunches == 0}</p>
     *
     * @param selectedAppLaunches default is 1, 1 means each launch, 2 means every 2nd launch,
     *                            3 means every 3rd launch, etc
     * @return the {@link AppRate} singleton object
     * @deprecated since 1.2.0, use {@link #setSelectedAppLaunches(byte)} with the same
     *             {@code param} instead
     * @see #setSelectedAppLaunches(byte)
     */
    @Deprecated
    public AppRate setRemindLaunchTimes(@SuppressWarnings("SameParameterValue") byte selectedAppLaunches) {
        return setSelectedAppLaunches(selectedAppLaunches);
    }

    /**
     * <p>Decides whether the Neutral button ("Remind Me Later") appears in the Rate Dialog or
     * not.</p>
     *
     * @param isShowNeutralButton default is true, true means to show the Neutral button
     * @return the {@link AppRate} singleton object
     * @see #setTextLater(int)
     * @see #setTextLater(String)
     */
    public AppRate setShowLaterButton(@SuppressWarnings("SameParameterValue") boolean isShowNeutralButton) {
        dialogOptions.setShowNeutralButton(isShowNeutralButton);
        return this;
    }

    /**
     * <p>Decides if the Negative button appears in the Rate Dialog or not.</p>
     *
     * @param isShowNeverButton default is true, true means to show the Negative button
     * @return the {@link AppRate} singleton object
     * @see #setTextNever(int)
     * @see #setTextNever(String)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setShowNeverButton(boolean isShowNeverButton) {
        dialogOptions.setShowNegativeButton(isShowNeverButton);
        return this;
    }

    /**
     * <p>Sets whether the Rate Dialog should show the title.</p>
     *
     * @param isShowTitle true to show the title, false otherwise
     * @return the {@link AppRate} singleton object
     * @see #setTitle(int)
     * @see #setTitle(String)
     */
    @SuppressWarnings("unused")
    public AppRate setShowTitle(boolean isShowTitle) {
        dialogOptions.setShowTitle(isShowTitle);
        return this;
    }

    /**
     * <p>Sets whether the Rate Dialog should show the message.</p>
     *
     * @param isShowMessage true to show the message, false otherwise
     * @return the {@link AppRate} singleton object
     * @see #setMessage(int)
     * @see #setMessage(String)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setShowMessage(boolean isShowMessage) {
        dialogOptions.setShowMessage(isShowMessage);
        return this;
    }

    /**
     * <p>Sets whether the Rate Dialog should show the icon.</p>
     *
     * @param isShowDialogIcon true to show the icon, false otherwise, default values are false for
     *                         {@link DialogType#CLASSIC DialogType.CLASSIC} and true for
     *                         {@link DialogType#APPLE DialogType.APPLE} and
     *                         {@link DialogType#MODERN DialogType.MODERN}
     * @return the {@link AppRate} singleton object
     * @see #setDialogIcon(int)
     * @see #setDialogIcon(Drawable)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setShowDialogIcon(boolean isShowDialogIcon) {
        dialogOptions.setShowDialogIcon(isShowDialogIcon);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate clearSettingsParam() {
        PreferenceHelper.clearSharedPreferences(context);
        return this;
    }

    /**
     * <p>Clears agree to show Rate Dialog flag.</p>
     *
     * @return the {@link AppRate} singleton object
     */
    @SuppressWarnings("unused")
    public AppRate clearAgreeShowDialog() {
        return setAgreeShowDialog(true);
    }

    /**
     * <p>Returns agree to show Rate Dialog flag.</p>
     *
     * @return agree to show Rate Dialog flag value
     */
    @SuppressWarnings("WeakerAccess")
    public boolean getAgreeShowDialog() {
        return getIsAgreeShowDialog(context);
    }

    /**
     * <p>Sets agree to show Rate Dialog flag.</p>
     *
     * @param isAgree agree to show Rate Dialog flag
     * @return the {@link AppRate} singleton object
     */
    @SuppressWarnings("WeakerAccess")
    public AppRate setAgreeShowDialog(boolean isAgree) {
        setIsAgreeShowDialog(context, isAgree);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setView(@Nullable View view) {
        dialogOptions.setView(view);
        return this;
    }

    /**
     * <p>Specifies the callback when the button of Rate Dialog is pressed.</p>
     *
     * @param listener implemented {@link OnClickButtonListener} Interface
     * @return the {@link AppRate} singleton object
     */
    public AppRate setOnClickButtonListener(OnClickButtonListener listener) {
        dialogOptions.setListener(listener);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog icon.</p>
     * <p>Takes precedence over values set using {@link #setDialogIcon(Drawable)}.</p>
     *
     * @param resourceId the Rate Dialog icon resource ID
     * @return the {@link AppRate} singleton object
     * @see #setDialogIcon(Drawable)
     * @see #setShowDialogIcon(boolean)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setDialogIcon(int resourceId) {
        dialogOptions.setDialogIconResId(resourceId);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog icon.</p>
     *
     * @param dialogIcon the Rate Dialog icon Drawable
     * @return the {@link AppRate} singleton object
     * @see #setDialogIcon(int)
     * @see #setShowDialogIcon(boolean)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setDialogIcon(Drawable dialogIcon) {
        dialogOptions.setDialogIcon(dialogIcon);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog title text.</p>
     *
     * @param resourceId the Rate Dialog title text resource ID
     * @return the {@link AppRate} singleton object
     * @see #setTitle(String)
     * @see #setShowTitle(boolean)
     */
    @SuppressWarnings("unused")
    public AppRate setTitle(@SuppressWarnings("SameParameterValue") int resourceId) {
        dialogOptions.setTitleResId(resourceId);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog title text.</p>
     * <p>Takes precedence over values set using {@link #setTitle(int)}.</p>
     *
     * @param title the Rate Dialog title text
     * @return the {@link AppRate} singleton object
     * @see #setTitle(int)
     * @see #setShowTitle(boolean)
     */
    @SuppressWarnings("unused")
    public AppRate setTitle(String title) {
        dialogOptions.setTitleText(title);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog message text.</p>
     *
     * @param resourceId the Rate Dialog message text resource ID
     * @return the {@link AppRate} singleton object
     * @see #setMessage(String)
     * @see #setShowMessage(boolean)
     */
    @SuppressWarnings("unused")
    public AppRate setMessage(int resourceId) {
        dialogOptions.setMessageResId(resourceId);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog message text.</p>
     * <p>Takes precedence over values set using {@link #setMessage(int)}.</p>
     *
     * @param message the Rate Dialog message text
     * @return the {@link AppRate} singleton object
     * @see #setMessage(int)
     * @see #setShowMessage(boolean)
     */
    @SuppressWarnings("unused")
    public AppRate setMessage(String message) {
        dialogOptions.setMessageText(message);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog positive button text.</p>
     *
     * @param resourceId the Rate Dialog positive button text resource ID
     * @return the {@link AppRate} singleton object
     * @see #setTextRateNow(String)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setTextRateNow(@SuppressWarnings("SameParameterValue") int resourceId) {
        dialogOptions.setTextPositiveResId(resourceId);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog positive button text.</p>
     * <p>Takes precedence over values set using {@link #setTextRateNow(int)}.</p>
     *
     * @param positiveText the Rate Dialog positive button text
     * @return the {@link AppRate} singleton object
     * @see #setTextRateNow(int)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setTextRateNow(String positiveText) {
        dialogOptions.setPositiveText(positiveText);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog neutral button text.</p>
     *
     * @param resourceId the Rate Dialog neutral button text resource ID
     * @return the {@link AppRate} singleton object
     * @see #setTextLater(String)
     * @see #setShowLaterButton(boolean)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setTextLater(@SuppressWarnings("SameParameterValue") int resourceId) {
        dialogOptions.setTextNeutralResId(resourceId);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog neutral button text.</p>
     * <p>Takes precedence over values set using {@link #setTextLater(int)}.</p>
     *
     * @param neutralText the Rate Dialog neutral button text
     * @return the {@link AppRate} singleton object
     * @see #setTextLater(int)
     * @see #setShowLaterButton(boolean)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setTextLater(String neutralText) {
        dialogOptions.setNeutralText(neutralText);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog negative button text.</p>
     *
     * @param resourceId the Rate Dialog negative button text resource ID
     * @return the {@link AppRate} singleton object
     * @see #setTextNever(String)
     * @see #setShowNeverButton(boolean)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setTextNever(@SuppressWarnings("SameParameterValue") int resourceId) {
        dialogOptions.setTextNegativeResId(resourceId);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog negative button text.</p>
     * <p>Takes precedence over values set using {@link #setTextNever(int)}.</p>
     *
     * @param negativeText the Rate Dialog negative button text
     * @return the {@link AppRate} singleton object
     * @see #setTextNever(int)
     * @see #setShowNeverButton(boolean)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setTextNever(String negativeText) {
        dialogOptions.setNegativeText(negativeText);
        return this;
    }

    /**
     * <p>Sets whether the Rate Dialog is cancelable or not.</p>
     *
     * @param cancelable default is false
     * @return the {@link AppRate} singleton object
     */
    public AppRate setCancelable(@SuppressWarnings("SameParameterValue") boolean cancelable) {
        dialogOptions.setCancelable(cancelable);
        return this;
    }

    /**
     * <p>Sets the Rate Dialog type.</p>
     *
     * @param dialogType one of the values defined by {@link DialogType.AnyDialogType}
     * @return the {@link AppRate} singleton object
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public AppRate setDialogType(@DialogType.AnyDialogType final int dialogType) {
        dialogOptions.setDialogType(dialogType);
        return this;
    }

    /**
     * <p>Sets one of the app stores defined by {@link StoreType.StoreWithoutApplicationId} to
     * the Positive button.</p>
     *
     * @param storeType one of the values defined by {@link StoreType.StoreWithoutApplicationId}
     * @return the {@link AppRate} singleton object
     * @throws IllegalArgumentException if {@code storeType} isn't defined by
     *         {@link StoreType.StoreWithoutApplicationId}
     * @see #setStoreType(int, long)
     * @see #setStoreType(String...)
     * @see #setStoreType(Intent...)
     */
    @SuppressWarnings("WeakerAccess")
    public AppRate setStoreType(@StoreType.StoreWithoutApplicationId final int storeType) throws IllegalArgumentException {
        if ((storeType == APPLE) || (storeType == BLACKBERRY)) {
            throw new IllegalArgumentException("For StoreType.APPLE/StoreType.BLACKBERRY you must" +
                     " use setStoreType(StoreType.APPLE/StoreType.BLACKBERRY, long applicationId).");
        } else if ((storeType < AMAZON) || (storeType > YANDEX)) {
            throw new IllegalArgumentException("StoreType must be one of: AMAZON, APPLE, BAZAAR, " +
                    "BLACKBERRY, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT, YANDEX.");
        }
        return setStoreType(storeType, null, null);
    }

    /**
     * <p>Sets one of the app stores defined by {@link StoreType.StoreWithApplicationId} to
     * the Positive button.</p>
     *
     * @param storeType one of the values defined by {@link StoreType.StoreWithApplicationId}
     * @param applicationId application ID in the {@code storeType} app store
     * @return the {@link AppRate} singleton object
     * @throws IllegalArgumentException if {@code storeType} isn't defined by
     *         {@link StoreType.StoreWithApplicationId} or by
     *         {@link StoreType.StoreWithoutApplicationId}
     * @see #setStoreType(int)
     * @see #setStoreType(String...)
     * @see #setStoreType(Intent...)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setStoreType(@StoreType.StoreWithApplicationId final int storeType,
                                final long applicationId) throws IllegalArgumentException {
        if ((storeType < AMAZON) || (storeType > YANDEX)) {
            throw new IllegalArgumentException("StoreType must be one of: AMAZON, APPLE, BAZAAR, " +
                    "BLACKBERRY, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT, YANDEX.");
        }
        return ((storeType != APPLE) && (storeType != BLACKBERRY)) ?
                setStoreType(storeType, null, null) :
                setStoreType(storeType, new String[]{String.valueOf(applicationId)}, null);
    }

    /**
     * <p>Sets any other app store/stores to the Positive button.</p>
     *
     * @param uris an RFC 2396-compliant URI or array of URIs to your app,
     *             e. g. {@code https://otherstore.com/app?id=com.yourapp}
     *             or {@code otherstore://apps/com.yourapp}
     * @return the {@link AppRate} singleton object
     * @throws IllegalArgumentException if {@code uris} equals null
     * @see #setStoreType(int)
     * @see #setStoreType(int, long)
     * @see #setStoreType(Intent...)
     */
    @SuppressWarnings({"ConstantConditions", "WeakerAccess", "unused"})
    public AppRate setStoreType(@NonNull final String... uris) throws IllegalArgumentException {
        if (uris == null) {
            throw new IllegalArgumentException("setStoreType(String... uris): 'uris' must be != null.");
        }
        return setStoreType(OTHER, uris, null);
    }

    /**
     * <p>Sets custom action to the Positive button.</p>
     * <p>For example, you can open your custom RateActivity when the Positive button clicked.</p>
     *
     * @param intents any custom intent or array of intents,
     *                first will be executed ({@code startActivity(intents[0])}), if first fails,
     *                second will be executed ({@code startActivity(intents[1])}), etc.
     * @return the {@link AppRate} singleton object
     * @throws IllegalArgumentException if {@code intents} equals null
     * @see #setStoreType(int)
     * @see #setStoreType(int, long)
     * @see #setStoreType(String...)
     */
    @SuppressWarnings({"ConstantConditions", "WeakerAccess", "unused"})
    public AppRate setStoreType(@NonNull final Intent... intents) throws IllegalArgumentException {
        if (intents == null) {
            throw new IllegalArgumentException("setStoreType(Intent... intents): 'intents' must be != null.");
        }
        return setStoreType(INTENT, null, intents);
    }

    private AppRate setStoreType(@StoreType.AnyStoreType final int storeType,
                                 final String[] stringParam,
                                 final Intent[] intentParaam) {
        storeOptions.setStoreType(storeType, stringParam, intentParaam);
        return this;
    }

    /**
     * <p>Gets the app store type from library options.</p>
     * <p>NOTE: this method doesn't get an app store type from user's device.</p>
     *
     * @return one of the values defined by {@link StoreType.AnyStoreType}
     */
    @SuppressWarnings("unused")
    @StoreType.AnyStoreType
    public int getStoreType() {
        return storeOptions.getStoreType();
    }

    @SuppressWarnings("unused")
    public AppRate incrementEventCount(String eventName) {
        return setEventCountValue(eventName, (short) (getCustomEventCount(context, eventName) + 1));
    }

    @SuppressWarnings("WeakerAccess")
    public AppRate setEventCountValue(String eventName, short countValue) {
        setCustomEventCount(context, eventName, countValue);
        return this;
    }

    /**
     * <p>Sets dialog theme. You can use a specific theme to inflate the dialog.</p>
     *
     * @param themeResId theme resource ID, default is 0
     * @return the {@link AppRate} singleton object
     */
    @SuppressWarnings({"unused", "UnusedReturnValue", "WeakerAccess"})
    public AppRate setThemeResId(@StyleRes int themeResId) {
        dialogOptions.setThemeResId(themeResId);
        return this;
    }

    /**
     * <p>Sets {@link DialogManager.Factory} implementation.</p>
     * <p>Call {@code AppRate.with(this).setDialogManagerFactory(null)} to set
     * {@link DefaultDialogManager.Factory} implementation.</p>
     *
     * @param dialogManagerFactory object of class that implements {@link DialogManager.Factory},
     *                             default is {@link DefaultDialogManager.Factory} class object
     * @return the {@link AppRate} singleton object
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setDialogManagerFactory(@Nullable DialogManager.Factory dialogManagerFactory) {
        this.dialogManagerFactory.clearDialogManager();
        if (dialogManagerFactory == null) {
            this.dialogManagerFactory = new DefaultDialogManager.Factory();
        } else {
            dialogManagerFactory.clearDialogManager();
            this.dialogManagerFactory = dialogManagerFactory;
        }
        return this;
    }

    /**
     * <p>Sets the check whether the version code of the app is changed.</p>
     *
     * @param isVersionCodeCheck true means to re-enable the Rate Dialog if a new version of app
     *                           with different version code is installed, default is false
     * @return the {@link AppRate} singleton object
     */
    @SuppressWarnings("unused")
    public AppRate setVersionCodeCheck(boolean isVersionCodeCheck) {
        this.isVersionCodeCheck = isVersionCodeCheck;
        return this;
    }

    /**
     * <p>Sets the check whether the version name of the app is changed.</p>
     *
     * @param isVersionNameCheck true means to re-enable the Rate Dialog if a new version of app
     *                           with different version name is installed, default is false
     * @return the {@link AppRate} singleton object
     */
    @SuppressWarnings("unused")
    public AppRate setVersionNameCheck(boolean isVersionNameCheck) {
        this.isVersionNameCheck = isVersionNameCheck;
        return this;
    }

    /**
     * <p>Monitors launches of the application.</p>
     * <p>Call this method when the {@code onCreate()} of the app's launcher activity is
     * launched.</p>
     */
    public void monitor() {
        if (isFirstLaunch(context)) {
            setFirstLaunchSharedPreferences(context);
        } else {
            final AppInformation appInformation = AppInformation.getInstance(context);
            PreferenceHelper.setLaunchTimes(context, (short) (getLaunchTimes(context) + 1));
            if ((appInformation != null) && (appInformation.getAppLongVersionCode() != getVersionCode(context))) {
                if (isVersionCodeCheck) {
                    setAgreeShowDialog(true);
                }
                setVersionCode(context);
            }
            if ((appInformation != null) && !appInformation.getAppVersionName().equals(getVersionName(context))) {
                if (isVersionNameCheck) {
                    setAgreeShowDialog(true);
                }
                setVersionName(context);
            }
        }
    }

    /**
     * <p>Call this method directly if you want to force display of the Rate Dialog.</p>
     * <p>Call it when some button presses on. Method also useful for testing purposes.</p>
     *
     * @param activity your activity, use "this" in most cases
     */
    @SuppressWarnings("WeakerAccess")
    public void showRateDialog(final Activity activity) {
        boolean callDismissListener = false;
        if ((dialog != null) && (dialog.get() != null)) {
            callDismissListener = true;
        }
        try {
            dismissRateDialog();
            callDismissListener = false;
        } catch (Exception e) {
            Log.i(TAG, "Can't dismiss Rate Dialog, because unpredictable exception.", e);
            clearRateDialog();
        }

        setRateDialog(new WeakReference<>(dialogManagerFactory
                .createDialogManager(activity, dialogOptions, storeOptions).createDialog()));
        if (dialog.get() != null) {
            try {
                if (!activity.isFinishing()) {
                    dialog.get().show();
                    dialogOptions.setDialogRedrawn(false);
                } else {
                    Log.w(TAG, LOG_MESSAGE_PART_1 + "can't show Rate Dialog, because " +
                            "activity is in the process of finishing.");
                }
            } catch(Exception e){
                Log.w(TAG, LOG_MESSAGE_PART_1 + "can't show Rate Dialog, because " +
                        "unpredictable exception.", e);
            }
        } else {
            Log.w(TAG, LOG_MESSAGE_PART_1 + "can't create Rate Dialog.");
        }

        if (callDismissListener) {
            ((DefaultDialogManager) dialogManagerFactory
                    .createDialogManager(context, dialogOptions, storeOptions))
                    .dismissListener.onDismiss(null);
        }
    }

    /**
     * <p>Checks whether the Rate Dialog is currently showing.</p>
     *
     * @return true if the Rate Dialog is currently showing, false otherwise.
     */
    @SuppressWarnings("unused")
    public boolean isShowingRateDialog() {
        return ((dialog != null) && (dialog.get() != null)) && dialog.get().isShowing();
    }

    /**
     * <p>Dismisses Rate Dialog, removing it from the screen, and
     * clears weak reference dialog object.</p>
     * <p>This method can be invoked safely from any thread.</p>
     */
    @SuppressWarnings("WeakerAccess")
    public void dismissRateDialog() {
        if ((dialog != null) && (dialog.get() != null)) {
            dialog.get().dismiss();
        }
        clearRateDialog();
    }

    /**
     * <p>Call this method directly if you want to send a user to rate your app right in the app
     * store.</p>
     *
     * @param activity your activity, use "this" in most cases
     */
    @SuppressWarnings("unused")
    public void rateNow(Activity activity) {
        ((DefaultDialogManager) dialogManagerFactory
                .createDialogManager(activity, dialogOptions, storeOptions))
                .positiveListener.onClick((dialog != null) ? dialog.get() : null, BUTTON_POSITIVE);
        dismissRateDialog();
    }

    /**
     * <p>Determines whether conditions to show the Rate Dialog meets or not.</p>
     *
     * @return true if the conditions to show the Rate Dialog meets, false othewise
     */
    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowRateDialog() {
        return getAgreeShowDialog() &&
                isOverLaunchTimes() &&
                isSelectedAppLaunch() &&
                isOverInstallDate() &&
                isOverRemindDate() &&
                isOverRemindLaunchesNumber() &&
                isOverCustomEventsRequirements() &&
                isBelow365DayPeriodMaxNumberDialogLaunchTimes();
    }

    private boolean isOverLaunchTimes() {
        return ((appLaunchTimes == 0) || (getLaunchTimes(context) >= appLaunchTimes));
    }

    private boolean isSelectedAppLaunch() {
        return ((selectedAppLaunches == 1) ||
                ((selectedAppLaunches != 0) && ((getLaunchTimes(context) % selectedAppLaunches) == 0)));
    }

    private boolean isOverInstallDate() {
        return ((installDate == 0L) || isOverDate(getInstallDate(context), installDate));
    }

    private boolean isOverRemindDate() {
        return ((remindInterval == 0L) ||
                (getRemindInterval(context) == 0L) ||
                isOverDate(getRemindInterval(context), remindInterval));
    }

    private boolean isOverRemindLaunchesNumber() {
        return ((remindLaunchesNumber == 0) ||
                (getRemindLaunchesNumber(context) == 0) ||
                (getLaunchTimes(context) - getRemindLaunchesNumber(context) >= remindLaunchesNumber));
    }

    private boolean isOverCustomEventsRequirements() {
        if (customEventsCounts.isEmpty()) {
            return true;
        } else {
            Short currentCount;
            for (Map.Entry<String, Short> eventRequirement : customEventsCounts.entrySet()) {
                currentCount = getCustomEventCount(context, eventRequirement.getKey());
                if (currentCount < eventRequirement.getValue()) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * <p>Checks if the library is in Debug mode. <b>For development only!</b></p>
     *
     * @return true if the library is in Debug mode, false otherwise
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * <p>Debug mode. <b>For development only!</b></p>
     * <p>Setting the library to Debug mode ensures that the Rate Dialog will be shown each time
     * the app is launched.</p>
     *
     * @param isDebug default is false, true ensures that the Rate Dialog will be shown each time
     *                the app is launched
     * @return the {@link AppRate} singleton object
     */
    public AppRate setDebug(@SuppressWarnings("SameParameterValue") boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }
}