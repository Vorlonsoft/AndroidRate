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
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForOtherStores;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForStore;
import static com.vorlonsoft.android.rate.PreferenceHelper.setAgreeShowDialog;
import static com.vorlonsoft.android.rate.PreferenceHelper.setRemindInterval;
import static com.vorlonsoft.android.rate.StoreType.AMAZON;
import static com.vorlonsoft.android.rate.StoreType.APPLE;
import static com.vorlonsoft.android.rate.StoreType.BAZAAR;
import static com.vorlonsoft.android.rate.StoreType.BLACKBERRY;
import static com.vorlonsoft.android.rate.StoreType.CHINESESTORES;
import static com.vorlonsoft.android.rate.StoreType.GOOGLEPLAY;
import static com.vorlonsoft.android.rate.StoreType.INTENT;
import static com.vorlonsoft.android.rate.StoreType.MI;
import static com.vorlonsoft.android.rate.StoreType.OTHER;
import static com.vorlonsoft.android.rate.StoreType.SAMSUNG;
import static com.vorlonsoft.android.rate.StoreType.SLIDEME;
import static com.vorlonsoft.android.rate.StoreType.TENCENT;
import static com.vorlonsoft.android.rate.StoreType.YANDEX;
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
        public void onClick(final DialogInterface dialog, final int which) {
            final Intent[] intentsToAppStores;
            final String packageName = context.getPackageName();
            if ((packageName != null) && (packageName.hashCode() != "".hashCode())) {
                switch(options.getStoreType()) {
                    case AMAZON:
                        intentsToAppStores = createIntentsForStore(context, AMAZON, packageName);
                        break;
                    case APPLE:
                        intentsToAppStores = createIntentsForStore(context, APPLE, options.getApplicationId());
                        break;
                    case BAZAAR:
                        intentsToAppStores = createIntentsForStore(context, BAZAAR, packageName);
                        break;
                    case BLACKBERRY:
                        intentsToAppStores = createIntentsForStore(context, BLACKBERRY, options.getApplicationId());
                        break;
                    case CHINESESTORES:
                        intentsToAppStores = createIntentsForStore(context, CHINESESTORES, packageName);
                        break;
                    case INTENT:
                        intentsToAppStores = options.getIntents();
                        break;
                    case MI:
                        intentsToAppStores = createIntentsForStore(context, MI, packageName);
                        break;
                    case OTHER:
                        intentsToAppStores = createIntentsForOtherStores(options.getOtherStoreUri());
                        break;
                    case SAMSUNG:
                        intentsToAppStores = createIntentsForStore(context, SAMSUNG, packageName);
                        break;
                    case SLIDEME:
                        intentsToAppStores = createIntentsForStore(context, SLIDEME, packageName);
                        break;
                    case TENCENT:
                        intentsToAppStores = createIntentsForStore(context, TENCENT, packageName);
                        break;
                    case YANDEX:
                        intentsToAppStores = createIntentsForStore(context, YANDEX, packageName);
                        break;
                    default:
                        intentsToAppStores = createIntentsForStore(context, GOOGLEPLAY, packageName);
                }
                if (intentsToAppStores == null) {
                    Log.w(TAG, "Failed to rate app, can't create intents for store");
                }
            } else {
                Log.w(TAG, "Failed to rate app, can't get app package name");
                intentsToAppStores = null;
            }
            try {
                if (intentsToAppStores != null) {
                    if (intentsToAppStores.length == 0) {
                        Log.w(TAG, "Failed to rate app, no intent found for startActivity (intentsToAppStores.length == null)");
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
        public void onClick(final DialogInterface dialog, final int which) {
            setAgreeShowDialog(context, false);
            if (DefaultDialogManager.this.listener != null) DefaultDialogManager.this.listener.onClickButton((byte) which);
        }
    };
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener neutralListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
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

    @Override
    public Dialog createDialog() {
        AlertDialog.Builder builder = getDialogBuilder(context, options.getThemeResId());

        if (builder == null) return null;

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