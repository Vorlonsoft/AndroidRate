/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.vorlonsoft.android.rate.Constants.Utils.EMPTY_STRING;
import static com.vorlonsoft.android.rate.Constants.Utils.LOG_MESSAGE_PART_1;
import static com.vorlonsoft.android.rate.Constants.Utils.TAG;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForStore;
import static com.vorlonsoft.android.rate.PreferenceHelper.setIsAgreeShowDialog;
import static com.vorlonsoft.android.rate.PreferenceHelper.setRemindInterval;
import static com.vorlonsoft.android.rate.PreferenceHelper.setRemindLaunchesNumber;
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

/**
 * <p>DefaultDialogOnClickListener Class - the default Rate Dialog buttons on-click listener class
 * of the AndroidRate library.</p>
 *
 * <p>Listener implements the {@link View.OnClickListener} and
 * {@link DialogInterface.OnClickListener} interfaces and allows to run some code when a Rate Dialog
 * button is clicked.</p>
 *
 * @since    1.2.5
 * @version  1.2.5
 * @author   Alexander Savin
 * @see OnClickButtonListener
 */
final class DefaultDialogOnClickListener implements View.OnClickListener,
                                                    DialogInterface.OnClickListener {
    /** <p>The WeakReference to the {@link DefaultDialogOnClickListener} singleton object.</p> */
    private static volatile WeakReference<DefaultDialogOnClickListener> singleton = null;
    /** <p>A Rate Dialog button callback specified by a library user.</p> */
    private OnClickButtonListener buttonListener;
    /** <p>A activity context.</p> */
    private Context context;
    /** <p>Store options.</p> */
    private final StoreOptions storeOptions;

    /**
     * <p>Creates {@link DefaultDialogOnClickListener} object.</p>
     *
     * @param buttonListener a Rate Dialog button callback specified by a library user
     * @param context a activity context
     * @param storeOptions store options
     */
    private DefaultDialogOnClickListener(@NonNull final Context context,
                                         @NonNull final StoreOptions storeOptions,
                                         @Nullable final OnClickButtonListener buttonListener) {
        this.context = context;
        this.storeOptions = storeOptions;
        this.buttonListener = buttonListener;
    }

    /**
     * <p>Creates the {@link DefaultDialogOnClickListener} singleton object.</p>
     *
     * @param context a activity context
     * @param storeOptions store options
     * @return the {@link DefaultDialogOnClickListener} singleton object
     */
    @NonNull
    static DefaultDialogOnClickListener getInstance(@NonNull final Context context,
                                             @NonNull final StoreOptions storeOptions,
                                             @Nullable final OnClickButtonListener buttonListener) {
        if ((singleton == null) || (singleton.get() == null)) {
            synchronized (DefaultDialogOnClickListener.class) {
                if ((singleton == null) || (singleton.get() == null)) {
                    if (singleton != null) {
                        singleton.clear();
                    }
                    singleton = new WeakReference<>(new DefaultDialogOnClickListener(context,
                                                                     storeOptions, buttonListener));
                } else {
                    singleton.get().context = context;
                    singleton.get().buttonListener = buttonListener;
                }
            }
        } else {
            singleton.get().context = context;
            singleton.get().buttonListener = buttonListener;
        }
        return singleton.get();
    }

    /**
     * <p>Called when a button has been clicked in a non-{@link DialogType#CLASSIC CLASSIC} Rate
     * Dialog.</p>
     *
     * @param button the button that was clicked.
     */
    @Override
    public void onClick(@NonNull final View button) {
        final int buttonResId = button.getId();
        if (buttonResId == R.id.rate_dialog_button_positive) {
            onClick(null, BUTTON_POSITIVE);
        } else if (buttonResId == R.id.rate_dialog_button_negative) {
            onClick(null, BUTTON_NEGATIVE);
        } else if (buttonResId == R.id.rate_dialog_button_neutral) {
            onClick(null, BUTTON_NEUTRAL);
        } else {
            Log.w(TAG, LOG_MESSAGE_PART_1 + "dialog button with the given ResId doesn't " +
                                                                                          "exist.");
        }

        AppRate.with(context).dismissRateDialog();
    }

    /**
     * <p>This method will be invoked when a button in the Rate Dialog is clicked.</p>
     *
     * @param dialog the Rate Dialog that received the click
     * @param which  the button that was clicked (ex.
     *               {@link DialogInterface#BUTTON_POSITIVE BUTTON_POSITIVE}) or the position
     */
    @Override
    public void onClick(@Nullable final DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                onPositiveButtonClick();
                break;
            case BUTTON_NEGATIVE:
                onNegativeButtonClick();
                break;
            case BUTTON_NEUTRAL:
                onNeutralButtonClick();
                break;
            default:
                Log.w(TAG, LOG_MESSAGE_PART_1 + "dialog button with the given identifier " +
                                                                                  "doesn't exist.");
                return;
        }

        if (buttonListener != null) {
            buttonListener.onClickButton((byte) which);
        }
    }

    /** <p>Calls when a positive button on the Rate Dialog is clicked.</p> */
    private void onPositiveButtonClick() {
        final String packageName = AppInformation.getPackageName(context);
        if (packageName.hashCode() != EMPTY_STRING.hashCode()) {
            final Intent[] intentsToAppStores = getIntentsForStores(packageName);
            try {
                if (intentsToAppStores.length == 0) {
                    Log.w(TAG, LOG_MESSAGE_PART_1 + "no intent found for startActivity " +
                                                               "(intentsToAppStores.length == 0).");
                } else if (intentsToAppStores[0] == null) {
                    throw new ActivityNotFoundException(LOG_MESSAGE_PART_1 + "no intent found for" +
                                                 " startActivity (intentsToAppStores[0] == null).");
                } else {
                    context.startActivity(intentsToAppStores[0]);
                }
            } catch (ActivityNotFoundException e) {
                Log.w(TAG, LOG_MESSAGE_PART_1 + "no activity found for " + intentsToAppStores[0]
                         , e);
                final byte intentsToAppStoresNumber = (byte) intentsToAppStores.length;
                if (intentsToAppStoresNumber > 1) {
                    boolean isCatch;
                    for (byte b = 1; b < intentsToAppStoresNumber; b++) {//intentsToAppStores[1] -
                        try {                                           //second intent in the array
                            if (intentsToAppStores[b] == null) {
                                throw new ActivityNotFoundException(LOG_MESSAGE_PART_1 +
                                        "no intent found for startActivity (intentsToAppStores[" +
                                                                                 b + "] == null).");
                            } else {
                                context.startActivity(intentsToAppStores[b]);
                            }
                            isCatch = false;
                        } catch (ActivityNotFoundException ex) {
                            Log.w(TAG, LOG_MESSAGE_PART_1 + "no activity found for " +
                                                                         intentsToAppStores[b], ex);
                            isCatch = true;
                        }
                        if (!isCatch) {
                            break;
                        }
                    }
                }
            }
        } else {
            Log.w(TAG, LOG_MESSAGE_PART_1 + "can't get app package name.");
        }
        setIsAgreeShowDialog(context, false);
    }

    /** <p>Calls when a negative button on the Rate Dialog is clicked.</p> */
    private void onNegativeButtonClick() {
        setIsAgreeShowDialog(context, false);
    }

    /** <p>Calls when a neutral button on the Rate Dialog is clicked.</p> */
    private void onNeutralButtonClick() {
        setRemindInterval(context);
        setRemindLaunchesNumber(context);
    }

    /**
     * <p>Creates intents for stores.</p>
     *
     * @param packageName package name
     * @return intents for stores
     */
    @NonNull
    private Intent[] getIntentsForStores(@NonNull final String packageName) {
        switch (storeOptions.getStoreType()) {
            case AMAZON:
                return createIntentsForStore(context, AMAZON, packageName);
            case APPLE:
                return createIntentsForStore(context, APPLE, storeOptions.getApplicationId());
            case BAZAAR:
                return createIntentsForStore(context, BAZAAR, packageName);
            case BLACKBERRY:
                return createIntentsForStore(context, BLACKBERRY, storeOptions.getApplicationId());
            case CHINESESTORES:
                return createIntentsForStore(context, CHINESESTORES, packageName);
            case MI:
                return createIntentsForStore(context, MI, packageName);
            case SAMSUNG:
                return createIntentsForStore(context, SAMSUNG, packageName);
            case SLIDEME:
                return createIntentsForStore(context, SLIDEME, packageName);
            case TENCENT:
                return createIntentsForStore(context, TENCENT, packageName);
            case YANDEX:
                return createIntentsForStore(context, YANDEX, packageName);
            case INTENT:
            case OTHER:
                return storeOptions.getIntents();
            default:
                return createIntentsForStore(context, GOOGLEPLAY, packageName);
        }
    }
}