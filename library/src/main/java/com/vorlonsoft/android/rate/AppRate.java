/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.vorlonsoft.android.rate.PreferenceHelper.*;

public final class AppRate {

    @SuppressLint("StaticFieldLeak")
    private static AppRate singleton;

    private final Context context;

    private final DialogOptions options = new DialogOptions();

    private byte installDate = 10;

    private byte launchTimes = 10;

    private byte remindInterval = 1;

    private final HashMap<String, Short> customEventCounts = new HashMap<>();

    private byte remindLaunchTimes = 1;

    private boolean isDebug = false;

    private DialogManager.Factory dialogManagerFactory = new DefaultDialogManager.Factory();

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

    @SuppressWarnings("UnusedReturnValue")
    public static boolean showRateDialogIfMeetsConditions(Activity activity) {
        boolean isMeetsConditions = singleton.isDebug || singleton.shouldShowRateDialog();
        if (isMeetsConditions) {
            singleton.showRateDialog(activity);
        }
        return isMeetsConditions;
    }

    private static boolean isOverDate(long targetDate, byte threshold) {
        return new Date().getTime() - targetDate >= (long) (threshold) * 24 * 60 * 60 * 1000;
    }

    public AppRate setLaunchTimes(@SuppressWarnings("SameParameterValue") byte launchTimes) {
        this.launchTimes = launchTimes;
        return this;
    }

    public AppRate setInstallDays(@SuppressWarnings("SameParameterValue") byte installDate) {
        this.installDate = installDate;
        return this;
    }

    public AppRate setRemindInterval(@SuppressWarnings("SameParameterValue") byte remindInterval) {
        this.remindInterval = remindInterval;
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setMinimumEventCount(String eventName, short minimumCount) {
        this.customEventCounts.put(eventName, minimumCount);
        return this;
    }

    public AppRate setRemindLaunchTimes(@SuppressWarnings("SameParameterValue") byte remindLaunchTimes) {
        this.remindLaunchTimes = remindLaunchTimes;
        return this;
    }

    public AppRate setShowLaterButton(@SuppressWarnings("SameParameterValue") boolean isShowNeutralButton) {
        options.setShowNeutralButton(isShowNeutralButton);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setShowNeverButton(boolean isShowNeverButton) {
        options.setShowNegativeButton(isShowNeverButton);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setShowTitle(boolean isShowTitle) {
        options.setShowTitle(isShowTitle);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate clearAgreeShowDialog() {
        PreferenceHelper.setAgreeShowDialog(context, true);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate clearSettingsParam() {
        PreferenceHelper.setAgreeShowDialog(context, true);
        PreferenceHelper.clearSharedPreferences(context);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setAgreeShowDialog(boolean clear) {
        PreferenceHelper.setAgreeShowDialog(context, clear);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setView(View view) {
        options.setView(view);
        return this;
    }

    public AppRate setOnClickButtonListener(OnClickButtonListener listener) {
        options.setListener(listener);
        return this;
    }

    public AppRate setTitle(@SuppressWarnings("SameParameterValue") int resourceId) {
        options.setTitleResId(resourceId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTitle(String title) {
        options.setTitleText(title);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setMessage(int resourceId) {
        options.setMessageResId(resourceId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setMessage(String message) {
        options.setMessageText(message);
        return this;
    }

    public AppRate setTextRateNow(@SuppressWarnings("SameParameterValue") int resourceId) {
        options.setTextPositiveResId(resourceId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTextRateNow(String positiveText) {
        options.setPositiveText(positiveText);
        return this;
    }

    public AppRate setTextLater(@SuppressWarnings("SameParameterValue") int resourceId) {
        options.setTextNeutralResId(resourceId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTextLater(String neutralText) {
        options.setNeutralText(neutralText);
        return this;
    }

    public AppRate setTextNever(@SuppressWarnings("SameParameterValue") int resourceId) {
        options.setTextNegativeResId(resourceId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setTextNever(String negativeText) {
        options.setNegativeText(negativeText);
        return this;
    }

    public AppRate setCancelable(@SuppressWarnings("SameParameterValue") boolean cancelable) {
        options.setCancelable(cancelable);
        return this;
    }

    public AppRate setStoreType(@SuppressWarnings("SameParameterValue") StoreType appStore) {
        return setStoreType(appStore, null);
    }

    @SuppressWarnings("unused")
    public AppRate setStoreType(int applicationID) {
        return setStoreType(StoreType.BLACKBERRY, String.valueOf(applicationID));
    }

    @SuppressWarnings("unused")
    public AppRate setStoreType(String uri) {
        return setStoreType(StoreType.OTHER, uri);
    }

    @SuppressWarnings("WeakerAccess")
    public AppRate setStoreType(StoreType appStore, String param) {
        if ((appStore == StoreType.BLACKBERRY)&&(param == null)) {
            throw new IllegalArgumentException("For StoreType.BLACKBERRY you must use setStoreType(int applicationID)");
        } else if ((appStore == StoreType.OTHER)&&(param == null)) {
            throw new IllegalArgumentException("For StoreType.OTHER you must use setStoreType(String uri) and 'uri' must be != null");
        }
        options.setStoreType(appStore, param);
        return this;
    }

    public StoreType getStoreType() {
        return options.getStoreType();
    }

    @SuppressWarnings("unused")
    public AppRate incrementEventCount(String eventName) {
        return setEventCountValue(eventName, (short) (getCustomEventCount(context,eventName) + 1));
    }

    @SuppressWarnings("WeakerAccess")
    public AppRate setEventCountValue(String eventName, short countValue) {
        setCustomEventCount(context, eventName, countValue);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setThemeResId(int themeResId){
        options.setThemeResId(themeResId);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRate setDialogManagerFactory(DialogManager.Factory dialogManagerFactory) {
        this.dialogManagerFactory = dialogManagerFactory;
        return this;
    }

    public void monitor() {
        if (isFirstLaunch(context)) {
            setInstallDate(context);
        }
        PreferenceHelper.setLaunchTimes(context, (short) (getLaunchTimes(context) + 1));
    }

    @SuppressWarnings("WeakerAccess")
    public void showRateDialog(Activity activity) {
        if (!activity.isFinishing()) {
            dialogManagerFactory.createDialogManager(activity, options).createDialog().show();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowRateDialog() {
        return getIsAgreeShowDialog(context) &&
                isOverLaunchTimes() &&
                isOverRemindLaunchTimes() &&
                isOverInstallDate() &&
                isOverRemindDate() &&
                isOverCustomEventRequirements();
    }

    private boolean isOverLaunchTimes() {
        return getLaunchTimes(context) >= launchTimes;
    }

    private boolean isOverRemindLaunchTimes() { return ((remindLaunchTimes != 0) && ((getLaunchTimes(context) % remindLaunchTimes) == 0)); }

    private boolean isOverInstallDate() {
        return isOverDate(getInstallDate(context), installDate);
    }

    private boolean isOverRemindDate() {
        return isOverDate(getRemindInterval(context), remindInterval);
    }

    private boolean isOverCustomEventRequirements() {
        for(Map.Entry<String, Short> eventRequirement : customEventCounts.entrySet()) {
            Short currentCount = getCustomEventCount(context, eventRequirement.getKey());
            if(currentCount < eventRequirement.getValue()) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unused")
    public boolean isDebug() {
        return isDebug;
    }

    public AppRate setDebug(@SuppressWarnings("SameParameterValue") boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

}
