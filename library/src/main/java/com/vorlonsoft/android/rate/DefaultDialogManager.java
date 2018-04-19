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
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForAmazonAppstore;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForBlackBerryWorld;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForCafeBazaar;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForChineseStores;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForGooglePlay;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForMiAppstore;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForOther;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForSamsungGalaxyApps;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForSlideME;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForTencentAppStore;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForYandexStore;
import static com.vorlonsoft.android.rate.PreferenceHelper.setAgreeShowDialog;
import static com.vorlonsoft.android.rate.PreferenceHelper.setRemindInterval;
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
                        intentsToAppStores = createIntentsForAmazonAppstore(context, packageName);
                        break;
                    case BAZAAR:
                        intentsToAppStores = createIntentsForCafeBazaar(context, packageName);
                        break;
                    case BLACKBERRY:
                        intentsToAppStores = createIntentsForBlackBerryWorld(context, options.getBlackBerryWorldApplicationId());
                        break;
                    case CHINESESTORES:
                        intentsToAppStores = createIntentsForChineseStores(context, packageName);
                        break;
                    case MI:
                        intentsToAppStores = createIntentsForMiAppstore(packageName);
                        break;
                    case OTHER:
                        intentsToAppStores = createIntentsForOther(context, options.getOtherStoreUri());
                        break;
                    case SAMSUNG:
                        intentsToAppStores = createIntentsForSamsungGalaxyApps(context, packageName);
                        break;
                    case SLIDEME:
                        intentsToAppStores = createIntentsForSlideME(context, packageName);
                        break;
                    case TENCENT:
                        intentsToAppStores = createIntentsForTencentAppStore(context, packageName);
                        break;
                    case YANDEX:
                        intentsToAppStores = createIntentsForYandexStore(context, packageName);
                        break;
                    default:
                        intentsToAppStores = createIntentsForGooglePlay(context, packageName);
                }
            } else {
                Log.w(TAG, "Failed to rate app, can't get context.getPackageName()");
                intentsToAppStores = null;
            }
            try {
                if (intentsToAppStores != null) {
                    if (intentsToAppStores.length == 0) {
                        Log.w(TAG, "Failed to rate app, no intent found for startActivity (intentsToAppStores.length == 0)");
                    } else if (intentsToAppStores[0] == null) {
                        throw new ActivityNotFoundException("Failed to rate app, no intent found for startActivity (intentsToAppStores[0] == null)");
                    } else {
                        context.startActivity(intentsToAppStores[0]);
                    }
                }
            } catch (ActivityNotFoundException e) {
                Log.w(TAG, "Failed to rate app, no activity found for " + intentsToAppStores[0], e);
                if (intentsToAppStores.length > 1) {
                    for (byte i = 1; i < intentsToAppStores.length; i++) { // intentsToAppStores[1] - second intent in the array
                        boolean isCatch = false;
                        try {
                            if (intentsToAppStores[i] == null) {
                                throw new ActivityNotFoundException("Failed to rate app, no intent found for startActivity (intentsToAppStores[" + i + "] == null)");
                            } else {
                                context.startActivity(intentsToAppStores[i]);
                            }
                        } catch (ActivityNotFoundException ex) {
                            Log.w(TAG, "Failed to rate app, no activity found for " + intentsToAppStores[i], ex);
                            isCatch = true;
                        } finally {
                            if (!isCatch) {
                                i = (byte) intentsToAppStores.length;
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