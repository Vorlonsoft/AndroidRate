/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import static com.vorlonsoft.android.rate.AppRate.TAG;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentForAmazonAppstore;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentForBlackBerryWorld;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentForCafeBazaar;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentForGooglePlay;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentForMiAppstore;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentForOther;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentForSamsungGalaxyApps;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentForSlideME;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentForTencentAppStore;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentForYandexStore;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForChineseStores;
import static com.vorlonsoft.android.rate.PreferenceHelper.setAgreeShowDialog;
import static com.vorlonsoft.android.rate.PreferenceHelper.setRemindInterval;
import static com.vorlonsoft.android.rate.StoreType.CHINESESTORES;
import static com.vorlonsoft.android.rate.StoreType.MI;
import static com.vorlonsoft.android.rate.StoreType.OTHER;
import static com.vorlonsoft.android.rate.Utils.getDialogBuilder;

public class DefaultDialogManager implements DialogManager {

    static class Factory implements DialogManager.Factory {
        @Override
        public DialogManager createDialogManager(Context context, DialogOptions options) {
            return new DefaultDialogManager(context, options);
        }
    }

    private final Context context;
    private final DialogOptions options;
    private final OnClickButtonListener listener;

    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final Intent[] intentsToAppStores;
            final String packageName = context.getPackageName();
            if ((packageName != null) && (packageName.hashCode() != "".hashCode())) {
                switch(options.getStoreType()) {
                    case AMAZON:
                        intentsToAppStores = createIntentForAmazonAppstore(context, packageName);
                        break;
                    case BAZAAR:
                        intentsToAppStores = createIntentForCafeBazaar(context, packageName);
                        break;
                    case BLACKBERRY:
                        intentsToAppStores = createIntentForBlackBerryWorld(context, options.getBlackBerryWorldApplicationId());
                        break;
                    case CHINESESTORES:
                        intentsToAppStores = createIntentsForChineseStores(context, packageName);
                        break;
                    case MI:
                        intentsToAppStores = createIntentForMiAppstore(packageName);
                        break;
                    case OTHER:
                        intentsToAppStores = createIntentForOther(options.getOtherStoreUri());
                        break;
                    case SAMSUNG:
                        intentsToAppStores = createIntentForSamsungGalaxyApps(context, packageName);
                        break;
                    case SLIDEME:
                        intentsToAppStores = createIntentForSlideME(context, packageName);
                        break;
                    case TENCENT:
                        intentsToAppStores = createIntentForTencentAppStore(context, packageName);
                        break;
                    case YANDEX:
                        intentsToAppStores = createIntentForYandexStore(context, packageName);
                        break;
                    default:
                        intentsToAppStores = createIntentForGooglePlay(context, packageName);
                }
            } else {
                Log.w(TAG, "Failed to rate app, can't get context.getPackageName()");
                intentsToAppStores = null;
            }
            try {
                if (intentsToAppStores != null) {
                    context.startActivity(intentsToAppStores[0]);
                }
            } catch (ActivityNotFoundException e) {
                Log.w(TAG, "Failed to rate app, no activity found for " + intentsToAppStores[0], e);
                if ((options.getStoreType() != OTHER) &&
                    (options.getStoreType() != MI) &&
                    (options.getStoreType() != CHINESESTORES) &&
                    (intentsToAppStores.length > 1)) {
                    try {
                        context.startActivity(intentsToAppStores[1]);
                    } catch (ActivityNotFoundException ex) {
                        Log.w(TAG, "Failed to rate app, no activity found for " + intentsToAppStores[1], ex);
                    }
                } else if (options.getStoreType() == CHINESESTORES) {
                    if (intentsToAppStores.length > 1) {
                        for (byte i = 1; i < intentsToAppStores.length; i++) {
                            try {
                                context.startActivity(intentsToAppStores[i]);
                            } catch (ActivityNotFoundException ex) {
                                Log.w(TAG, "Failed to rate app, no activity found for " + intentsToAppStores[i], ex);
                            }
                        }
                    }
                }
            }
            setAgreeShowDialog(context, false);
            if (listener != null) listener.onClickButton((byte) which);
        }
    };
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            setAgreeShowDialog(context, false);
            if (DefaultDialogManager.this.listener != null) DefaultDialogManager.this.listener.onClickButton((byte) which);
        }
    };
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener neutralListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            setRemindInterval(context);
            if (listener != null) listener.onClickButton((byte) which);
        }
    };

    @SuppressWarnings("WeakerAccess")
    public DefaultDialogManager(final Context context, final DialogOptions options) {
        this.context = context;
        this.options = options;
        this.listener = options.getListener();
    }

    public Dialog createDialog() {
        AlertDialog.Builder builder = getDialogBuilder(context, options.getThemeResId());
        builder.setMessage(options.getMessageText(context));

        if (options.shouldShowTitle()) builder.setTitle(options.getTitleText(context));

        builder.setCancelable(options.getCancelable());

        View view = options.getView();
        if (view != null) builder.setView(view);

        builder.setPositiveButton(options.getPositiveText(context), positiveListener);

        if (options.shouldShowNeutralButton()) {
            builder.setNeutralButton(options.getNeutralText(context), neutralListener);
        }

        if (options.shouldShowNegativeButton()) {
            builder.setNegativeButton(options.getNegativeText(context), negativeListener);
        }

        return builder.create();
    }

}