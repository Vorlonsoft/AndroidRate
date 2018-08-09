/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.vorlonsoft.android.rate.Constants.Date.DAY_IN_MILLIS;
import static com.vorlonsoft.android.rate.Constants.Utils.TAG;
import static com.vorlonsoft.android.rate.PreferenceHelper.get365DayPeriodDialogLaunchTimes;
import static com.vorlonsoft.android.rate.PreferenceHelper.getCustomEventCount;
import static com.vorlonsoft.android.rate.PreferenceHelper.getInstallDate;
import static com.vorlonsoft.android.rate.PreferenceHelper.getIsAgreeShowDialog;
import static com.vorlonsoft.android.rate.PreferenceHelper.getLaunchTimes;
import static com.vorlonsoft.android.rate.PreferenceHelper.getRemindInterval;
import static com.vorlonsoft.android.rate.PreferenceHelper.isFirstLaunch;
import static com.vorlonsoft.android.rate.PreferenceHelper.setCustomEventCount;
import static com.vorlonsoft.android.rate.PreferenceHelper.setFirstLaunchSharedPreferences;
import static com.vorlonsoft.android.rate.PreferenceHelper.setIsAgreeShowDialog;
import static com.vorlonsoft.android.rate.StoreType.AMAZON;
import static com.vorlonsoft.android.rate.StoreType.APPLE;
import static com.vorlonsoft.android.rate.StoreType.BLACKBERRY;
import static com.vorlonsoft.android.rate.StoreType.INTENT;
import static com.vorlonsoft.android.rate.StoreType.OTHER;
import static com.vorlonsoft.android.rate.StoreType.YANDEX;

public final class AppRate {

    @SuppressLint("StaticFieldLeak")
    private static volatile AppRate singleton = null;
    private final Map<String, Short> customEventsCounts;
    private final Context context;
    private final DialogOptions dialogOptions = new DialogOptions();
    private final StoreOptions storeOptions = new StoreOptions();
    private boolean isDebug = false;
    private byte installDate = (byte) 10;
    private byte appLaunchTimes = (byte) 10;
    private byte remindInterval = (byte) 1;
    private byte remindLaunchTimes = (byte) 1;
    /**
     * Short.MAX_VALUE - unlimited occurrences of the display of the dialog within a 365-day period
     */
    private short dialogLaunchTimes = Short.MAX_VALUE;
    private Dialog dialog = null;
    private DialogManager.Factory dialogManagerFactory = new DefaultDialogManager.Factory();

    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            customEventsCounts = new ArrayMap<>();
        } else {
            customEventsCounts = new HashMap<>();
        }
    }

    private AppRate(Context context) {
        this.context = context.getApplicationContext();
    }

    public static AppRate with(Context context) {
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
     * Call this method at the end of your onCreate() method to determine whether
     * to show the rate dialog or not. It will show the rate dialog if conditions meets.
     *
     * @param activity your activity, use "this" in most cases
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean showRateDialogIfMeetsConditions(Activity activity) {
        boolean isMeetsConditions = ((singleton != null) && (singleton.isDebug() || singleton.shouldShowRateDialog()));
        if (isMeetsConditions) {
            singleton.showRateDialog(activity);
        }
        return isMeetsConditions;
    }

    private static boolean isOverDate(long targetDate, byte threshold) {
        return new Date().getTime() - targetDate >= threshold * DAY_IN_MILLIS;
    }

    private boolean isBelow365DayPeriodMaxNumberDialogLaunchTimes() {
        return ((dialogLaunchTimes == Short.MAX_VALUE) || (get365DayPeriodDialogLaunchTimes(context) < dialogLaunchTimes));
    }

    /**
     * Sets max number occurrences of the display of the dialog within a 365-day period.
     * Set Short.MAX_VALUE for unlimited occurrences.
     *
     * @param dialogLaunchTimes max number of launches within a 365-day period, default is unlimited
     */
    @SuppressWarnings({"unused"})
    public AppRate set365DayPeriodMaxNumberDialogLaunchTimes(short dialogLaunchTimes) {
        this.dialogLaunchTimes = dialogLaunchTimes;
        return this;
    }

    /**
     * Sets the number of launches until the rating dialog pops up for the first time
     *
     * @param appLaunchTimes number of launches, default is 10
     */
    public AppRate setLaunchTimes(@SuppressWarnings("SameParameterValue") byte appLaunchTimes) {
        this.appLaunchTimes = appLaunchTimes;
        return this;
    }

    /**
     * Sets the number of days until the rating dialog pops up for the first time
     *
     * @param installDate number of days, default is 10
     */
    public AppRate setInstallDays(@SuppressWarnings("SameParameterValue") byte installDate) {
        this.installDate = installDate;
        return this;
    }

    /**
     * Sets number of days until rating dialog pops up for next time after
     * neutral button clicked
     *
     * @param remindInterval number of days, default is 1
     */
    public AppRate setRemindInterval(@SuppressWarnings("SameParameterValue") byte remindInterval) {
        this.remindInterval = remindInterval;
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setMinimumEventCount(String eventName, short minimumCount) {
        this.customEventsCounts.put(eventName, minimumCount);
        return this;
    }

    public AppRate setRemindLaunchTimes(@SuppressWarnings("SameParameterValue") byte remindLaunchTimes) {
        this.remindLaunchTimes = remindLaunchTimes;
        return this;
    }

    /**
     * Decides if Neutral button appear in the rating dialog or not
     *
     * @param isShowNeutralButton default is true
     */
    public AppRate setShowLaterButton(@SuppressWarnings("SameParameterValue") boolean isShowNeutralButton) {
        dialogOptions.setShowNeutralButton(isShowNeutralButton);
        return this;
    }

    /**
     * Decides if Never button appear in the rating dialog or not
     *
     * @param isShowNeverButton default is true
     */
    @SuppressWarnings("unused")
    public AppRate setShowNeverButton(boolean isShowNeverButton) {
        dialogOptions.setShowNegativeButton(isShowNeverButton);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setShowTitle(boolean isShowTitle) {
        dialogOptions.setShowTitle(isShowTitle);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate clearAgreeShowDialog() {
        setIsAgreeShowDialog(context, true);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate clearSettingsParam() {
        PreferenceHelper.clearSharedPreferences(context);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setAgreeShowDialog(boolean isAgree) {
        setIsAgreeShowDialog(context, isAgree);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setView(View view) {
        dialogOptions.setView(view);
        return this;
    }

    public AppRate setOnClickButtonListener(OnClickButtonListener listener) {
        dialogOptions.setListener(listener);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTitle(@SuppressWarnings("SameParameterValue") int resourceId) {
        dialogOptions.setTitleResId(resourceId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTitle(String title) {
        dialogOptions.setTitleText(title);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setMessage(int resourceId) {
        dialogOptions.setMessageResId(resourceId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setMessage(String message) {
        dialogOptions.setMessageText(message);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTextRateNow(@SuppressWarnings("SameParameterValue") int resourceId) {
        dialogOptions.setTextPositiveResId(resourceId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTextRateNow(String positiveText) {
        dialogOptions.setPositiveText(positiveText);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTextLater(@SuppressWarnings("SameParameterValue") int resourceId) {
        dialogOptions.setTextNeutralResId(resourceId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTextLater(String neutralText) {
        dialogOptions.setNeutralText(neutralText);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTextNever(@SuppressWarnings("SameParameterValue") int resourceId) {
        dialogOptions.setTextNegativeResId(resourceId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTextNever(String negativeText) {
        dialogOptions.setNegativeText(negativeText);
        return this;
    }

    /**
     * Sets whether the rating dialog is cancelable or not.
     *
     * @param cancelable default is true
     */
    public AppRate setCancelable(@SuppressWarnings("SameParameterValue") boolean cancelable) {
        dialogOptions.setCancelable(cancelable);
        return this;
    }

    public AppRate setStoreType(@StoreType.StoreWithoutApplicationId final int storeType) {
        if ((storeType == APPLE) || (storeType == BLACKBERRY)) {
            throw new IllegalArgumentException("For StoreType.APPLE/StoreType.BLACKBERRY you must use setStoreType(StoreType.APPLE/StoreType.BLACKBERRY, long applicationId)");
        } else if ((storeType < AMAZON) || (storeType > YANDEX)) {
            throw new IllegalArgumentException("StoreType must be one of: AMAZON, APPLE, BAZAAR, BLACKBERRY, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT, YANDEX");
        }
        return setStoreType(storeType, null, null);
    }

    @SuppressWarnings("unused")
    public AppRate setStoreType(@StoreType.StoreWithApplicationId final int storeType, final long applicationId) {
        if ((storeType < AMAZON) || (storeType > YANDEX)) {
            throw new IllegalArgumentException("StoreType must be one of: AMAZON, APPLE, BAZAAR, BLACKBERRY, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT, YANDEX");
        }
        return ((storeType != APPLE) && (storeType != BLACKBERRY)) ? setStoreType(storeType, null, null) : setStoreType(storeType, new String[]{String.valueOf(applicationId)}, null);
    }

    @SuppressWarnings({"ConstantConditions", "WeakerAccess", "unused"})
    public AppRate setStoreType(@NonNull final String... uris) {
        if (uris == null) {
            throw new IllegalArgumentException("setStoreType(String... uris): 'uris' must be != null");
        }
        return setStoreType(OTHER, uris, null);
    }

    private AppRate setStoreType(final int storeType, final String[] stringParam, final Intent[] intentParaam) {
        storeOptions.setStoreType(storeType, stringParam, intentParaam);
        return this;
    }

    /**
     * Gets the currently set Store
     */
    @StoreType.AnyStoreType
    public int getStoreType() {
        return storeOptions.getStoreType();
    }

    @SuppressWarnings({"ConstantConditions", "WeakerAccess", "unused"})
    public AppRate setStoreType(@NonNull final Intent... intents) {
        if (intents == null) {
            throw new IllegalArgumentException("setStoreType(Intent... intents): 'intents' must be != null");
        }
        return setStoreType(INTENT, null, intents);
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
     * Sets dialog theme. You can use a specific theme to inflate the dialog.
     *
     * @param themeResId theme resource ID, default is 0
     */
    @SuppressWarnings("unused")
    public AppRate setThemeResId(int themeResId) {
        dialogOptions.setThemeResId(themeResId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setDialogManagerFactory(DialogManager.Factory dialogManagerFactory) {
        this.dialogManagerFactory = dialogManagerFactory;
        return this;
    }

    public void monitor() {
        if (isFirstLaunch(context)) {
            setFirstLaunchSharedPreferences(context);
        } else {
            PreferenceHelper.setLaunchTimes(context, (short) (getLaunchTimes(context) + 1));
        }
    }

    /**
     * Call this method directly if you want to force the rate dialog, useful for testing purposes
     *
     * @param activity your activity, use "this" in most cases
     */
    @SuppressWarnings("WeakerAccess")
    public void showRateDialog(Activity activity) {
        dismissRateDialog();
        if (!activity.isFinishing()) {
            dialog = dialogManagerFactory.createDialogManager(activity, dialogOptions, storeOptions).createDialog();
            if (dialog != null) {
                dialog.show();
            } else {
                Log.w(TAG, "Failed to rate app, can't create rate dialog");
            }
        } else {
            Log.w(TAG, "Failed to rate app, can't create rate dialog, because activity is in the process of finishing");
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void dismissRateDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * Call this method directly to go straight to store listing for rating
     *
     * @param activity your activity, use "this" in most cases
     */
    public void rateNow(Activity activity) {
        dialog = dialogManagerFactory.createDialogManager(activity, dialogOptions, storeOptions).createDialog();
        if (dialog != null) {
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        } else {
            Log.w(TAG, "Failed to rate app, can't create rate dialog");
        }
    }

    /**
     * Call this method to determine whether conditions to show the rate dialog meets or not.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowRateDialog() {
        return getIsAgreeShowDialog(context) &&
                isOverLaunchTimes() &&
                isOverRemindLaunchTimes() &&
                isOverInstallDate() &&
                isOverRemindDate() &&
                isOverCustomEventsRequirements() &&
                isBelow365DayPeriodMaxNumberDialogLaunchTimes();
    }

    private boolean isOverLaunchTimes() {
        return ((appLaunchTimes == 0) || (getLaunchTimes(context) >= appLaunchTimes));
    }

    private boolean isOverRemindLaunchTimes() {
        return ((remindLaunchTimes == 1) || ((remindLaunchTimes != 0) && ((getLaunchTimes(context) % remindLaunchTimes) == 0)));
    }

    private boolean isOverInstallDate() {
        return ((installDate == 0) || isOverDate(getInstallDate(context), installDate));
    }

    private boolean isOverRemindDate() {
        return ((remindInterval == 0) || isOverDate(getRemindInterval(context), remindInterval));
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

    @SuppressWarnings({"unused", "WeakerAccess"})
    public boolean isDebug() {
        return isDebug;
    }

    public AppRate setDebug(@SuppressWarnings("SameParameterValue") boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

}
