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
import android.widget.Button;

import java.lang.ref.WeakReference;
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

/**
 * <p>AppRate Class - main class of the AndroidRate library, thread-safe
 * and a fast singleton implementation.</p>
 *
 * @since    0.0.4
 * @version  1.1.9
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */

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
    private byte selectedAppLaunches = (byte) 1;
    /** Short.MAX_VALUE - unlimited occurrences of the display of the dialog within a 365-day period */
    private short dialogLaunchTimes = Short.MAX_VALUE;
    // Weak reference to avoid leaking the context
    private WeakReference<Dialog> dialog = null;
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
     * <p>Shows the Rate Dialog when conditions are met.</p>
     * <p>Call this method at the end of your onCreate() method to determine whether
     * to show the rate dialog or not. It will check if the conditions are met and
     * show rate dialog if yes.</p>
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
     * Clear dialog weak reference
     */
    void clearDialog() {
        if (dialog != null) {
            dialog.clear();
        }
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
     * <p>Sets the minimum number of launches until the rating dialog pops up for the first time</p>
     *
     * @param appLaunchTimes number of launches, default is 10, 3 means app is launched more than 3 times
     * @return AppRate singleton object
     */
    public AppRate setLaunchTimes(@SuppressWarnings("SameParameterValue") byte appLaunchTimes) {
        this.appLaunchTimes = appLaunchTimes;
        return this;
    }

    /**
     * <p>Sets the minimum number of days until the Rating Dialog pops up for the first time.</p>
     *
     * @param installDate number of days, default is 10, 0 means install day, 10 means app is launched more than 10 days later than installation
     * @return AppRate singleton object
     */
    public AppRate setInstallDays(@SuppressWarnings("SameParameterValue") byte installDate) {
        this.installDate = installDate;
        return this;
    }

    /**
     * <p>Sets number of days until the Rating Dialog pops up for the next time after
     * neutral button clicked</p>
     *
     * @param remindInterval number of days, default is 1, 1 means app is launched more than 1 day after neutral button clicked
     * @return AppRate singleton object
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

    //TODO update README.md when 1.2.0 released
    /**
     * <p>Selects App launches.</p>
     * <p>Method sets divisor for division of app launches with a remainder. This condition is satisfied if {@code appLaunches % divisorAppLaunches == 0}</p>
     *
     * @param selectedAppLaunches default is 1, 1 means each launch, 2 means every 2nd launch, 3 means every 3rd launch, etc
     * @return AppRate singleton object
     * @since 1.2.0
     */
    public AppRate setSelectedAppLaunches(@SuppressWarnings("SameParameterValue") byte selectedAppLaunches) {
        this.selectedAppLaunches = selectedAppLaunches;
        return this;
    }

    /**
     * <p>Selects App launches.</p>
     * <p>Method sets divisor for division of app launches with a remainder. This condition is satisfied if {@code appLaunches % divisorAppLaunches == 0}</p>
     *
     * @param selectedAppLaunches default is 1, 1 means each launch, 2 means every 2nd launch, 3 means every 3rd launch, etc
     * @return AppRate singleton object
     * @deprecated since 1.2.0, use {@link #setSelectedAppLaunches(byte)} instead
     * @see #setSelectedAppLaunches(byte)
     */
    public AppRate setRemindLaunchTimes(@SuppressWarnings("SameParameterValue") byte selectedAppLaunches) {
        return setSelectedAppLaunches(selectedAppLaunches);
    }

    /**
     * Decides if Neutral button appear in the rating dialog or not
     *
     * @param isShowNeutralButton default is true
     * @return AppRate singleton object
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
     * @param cancelable default is false
     */
    public AppRate setCancelable(@SuppressWarnings("SameParameterValue") boolean cancelable) {
        dialogOptions.setCancelable(cancelable);
        return this;
    }

    /**
     * <p>Sets one of the app stores defined by {@link StoreType.StoreWithoutApplicationId} to the Positive button.</p>
     *
     * @param storeType one of the values defined by {@link StoreType.StoreWithoutApplicationId}
     * @return AppRate singleton object
     * @throws IllegalArgumentException if {@code storeType} isn't defined by {@link StoreType.StoreWithoutApplicationId}
     * @see #setStoreType(int, long)
     * @see #setStoreType(String...)
     * @see #setStoreType(Intent...)
     */
    @SuppressWarnings("WeakerAccess")
    public AppRate setStoreType(@StoreType.StoreWithoutApplicationId final int storeType) throws IllegalArgumentException {
        if ((storeType == APPLE) || (storeType == BLACKBERRY)) {
            throw new IllegalArgumentException("For StoreType.APPLE/StoreType.BLACKBERRY you must use setStoreType(StoreType.APPLE/StoreType.BLACKBERRY, long applicationId)");
        } else if ((storeType < AMAZON) || (storeType > YANDEX)) {
            throw new IllegalArgumentException("StoreType must be one of: AMAZON, APPLE, BAZAAR, BLACKBERRY, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT, YANDEX");
        }
        return setStoreType(storeType, null, null);
    }

    /**
     * <p>Sets one of the app stores defined by {@link StoreType.StoreWithApplicationId} to the Positive button.</p>
     *
     * @param storeType one of the values defined by {@link StoreType.StoreWithApplicationId}
     * @param applicationId application ID in the {@code storeType} app store
     * @return AppRate singleton object
     * @throws IllegalArgumentException if {@code storeType} isn't defined by {@link StoreType.StoreWithApplicationId} or by {@link StoreType.StoreWithoutApplicationId}
     * @see #setStoreType(int)
     * @see #setStoreType(String...)
     * @see #setStoreType(Intent...)
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public AppRate setStoreType(@StoreType.StoreWithApplicationId final int storeType, final long applicationId) throws IllegalArgumentException {
        if ((storeType < AMAZON) || (storeType > YANDEX)) {
            throw new IllegalArgumentException("StoreType must be one of: AMAZON, APPLE, BAZAAR, BLACKBERRY, CHINESESTORES, GOOGLEPLAY, MI, SAMSUNG, SLIDEME, TENCENT, YANDEX");
        }
        return ((storeType != APPLE) && (storeType != BLACKBERRY)) ? setStoreType(storeType, null, null) : setStoreType(storeType, new String[]{String.valueOf(applicationId)}, null);
    }

    /**
     * <p>Sets any other app store/stores to the Positive button.</p>
     *
     * @param uris an RFC 2396-compliant URI or array of URIs to your app,
     * e. g. {@code https://otherstore.com/app?id=com.yourapp}
     * or {@code otherstore://apps/com.yourapp}
     * @return AppRate singleton object
     * @throws IllegalArgumentException if {@code uris} equals null
     * @see #setStoreType(int)
     * @see #setStoreType(int, long)
     * @see #setStoreType(Intent...)
     */
    @SuppressWarnings({"ConstantConditions", "WeakerAccess", "unused"})
    public AppRate setStoreType(@NonNull final String... uris) throws IllegalArgumentException {
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
     * <p>Gets the app store type from library options.</p>
     * <p>NOTE: this method doesn't get an app store type from user's device.</p>
     *
     * @return one of the values defined by {@link StoreType.AnyStoreType}
     */
    @StoreType.AnyStoreType
    public int getStoreType() {
        return storeOptions.getStoreType();
    }

    /**
     * <p>Sets custom action to the Positive button.</p>
     * <p>For example, you can open your custom RateActivity when the Positive button clicked.</p>
     *
     * @param intents any custom intent or array of intents,
     * first will be executed ({@code startActivity(intents[0])}), if first fails,
     * second will be executed ({@code startActivity(intents[1])}), etc.
     * @return AppRate singleton object
     * @throws IllegalArgumentException if {@code intents} equals null
     * @see #setStoreType(int)
     * @see #setStoreType(int, long)
     * @see #setStoreType(String...)
     */
    @SuppressWarnings({"ConstantConditions", "WeakerAccess", "unused"})
    public AppRate setStoreType(@NonNull final Intent... intents) throws IllegalArgumentException {
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

    /**
     * Sets DialogManager.Factory.
     *
     * @param dialogManagerFactory DialogManager.Factory object, default is DefaultDialogManager.Factory object
     */
    @SuppressWarnings("unused")
    public AppRate setDialogManagerFactory(DialogManager.Factory dialogManagerFactory) {
        dialogManagerFactory.clearDialogManager();
        this.dialogManagerFactory.clearDialogManager();
        this.dialogManagerFactory = dialogManagerFactory;
        return this;
    }

    /**
     * <p>Monitors app launch times.</p>
     * <p>Call this method when the launcher activity's onCreate() is launched.</p>
     */
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
            dialog = new WeakReference<>(dialogManagerFactory.createDialogManager(activity, dialogOptions, storeOptions).createDialog());
            if (dialog.get() != null) {
                dialog.get().show();
            } else {
                Log.w(TAG, "Failed to rate app, can't create rate dialog");
            }
        } else {
            Log.w(TAG, "Failed to rate app, can't create rate dialog, because activity is in the process of finishing");
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void dismissRateDialog() {
        if ((dialog != null) && (dialog.get() != null)) {
            dialog.get().dismiss();
            clearDialog();
        }
    }

    /**
     * Call this method directly to go straight to store listing for rating
     *
     * @param activity your activity, use "this" in most cases
     */
    @SuppressWarnings("unused")
    public void rateNow(Activity activity) {
        if ((dialog != null) && (dialog.get() != null)) {
            Button positiveButton = ((AlertDialog) dialog.get()).getButton(AlertDialog.BUTTON_POSITIVE);
            if (positiveButton != null) {
                positiveButton.performClick();
            }
        } else {
            dialog = new WeakReference<>(dialogManagerFactory.createDialogManager(activity, dialogOptions, storeOptions).createDialog());
            if (dialog.get() != null) {
                Button positiveButton = ((AlertDialog) dialog.get()).getButton(AlertDialog.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.performClick();
                }
                clearDialog();
            } else {
                Log.w(TAG, "Failed to rate app, can't create rate dialog");
            }
        }
    }

    /**
     * Call this method to determine whether conditions to show the rate dialog meets or not.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowRateDialog() {
        return getIsAgreeShowDialog(context) &&
                isOverLaunchTimes() &&
                isSelectedAppLaunch() &&
                isOverInstallDate() &&
                isOverRemindDate() &&
                isOverCustomEventsRequirements() &&
                isBelow365DayPeriodMaxNumberDialogLaunchTimes();
    }

    private boolean isOverLaunchTimes() {
        return ((appLaunchTimes == 0) || (getLaunchTimes(context) >= appLaunchTimes));
    }

    private boolean isSelectedAppLaunch() {
        return ((selectedAppLaunches == 1) || ((selectedAppLaunches != 0) && ((getLaunchTimes(context) % selectedAppLaunches) == 0)));
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
