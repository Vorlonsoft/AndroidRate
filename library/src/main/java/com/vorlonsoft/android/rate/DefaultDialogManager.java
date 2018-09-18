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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.vorlonsoft.android.rate.Constants.Utils.EMPTY_STRING;
import static com.vorlonsoft.android.rate.Constants.Utils.TAG;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForStore;
import static com.vorlonsoft.android.rate.PreferenceHelper.getDialogFirstLaunchTime;
import static com.vorlonsoft.android.rate.PreferenceHelper.increment365DayPeriodDialogLaunchTimes;
import static com.vorlonsoft.android.rate.PreferenceHelper.setDialogFirstLaunchTime;
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
import static com.vorlonsoft.android.rate.Utils.isLollipop;

/**
 * <p>DefaultDialogManager Class - default dialog manager class implements {@link DialogManager}
 * interface of the AndroidRate library.</p>
 * <p>You can extend DefaultDialogManager Class and use
 * {@link AppRate#setDialogManagerFactory(DialogManager.Factory)} if you want to use fully custom
 * dialog (from v7 AppCompat library etc.). DefaultDialogManager Class is thread-safe and a fast
 * singleton implementation inside library, not outside (protected, not private constructor).</p>
 *
 * @since    1.0.2
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Antoine Vianey
 * @see DefaultDialogManager.Factory
 * @see DialogManager
 */

public class DefaultDialogManager implements DialogManager {
    /** <p>The WeakReference to the {@link DefaultDialogManager} singleton object.</p> */
    private static volatile WeakReference<DefaultDialogManager> singleton = null;
    private final StoreOptions storeOptions;
    private final OnClickButtonListener listener;
    @SuppressWarnings("WeakerAccess")
    protected final DialogOptions dialogOptions;
    @SuppressWarnings({"WeakerAccess", "UnusedAssignment"})
    protected Context context = null;
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnShowListener showListener = dialog -> {
        if (getDialogFirstLaunchTime(context) == 0L) {
            setDialogFirstLaunchTime(context);
        }
        increment365DayPeriodDialogLaunchTimes(context);
        if (isLollipop()) {
            try {
                final Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    final LinearLayout linearLayout = (LinearLayout) positiveButton.getParent();
                    if ((linearLayout != null) && (positiveButton.getLeft() + positiveButton.getWidth() > linearLayout.getWidth())) {
                        final Button neutralButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                        final Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        linearLayout.setGravity(Gravity.END);
                        if ((neutralButton != null) && (negativeButton != null)) {
                            linearLayout.removeView(neutralButton);
                            linearLayout.removeView(negativeButton);
                            linearLayout.addView(negativeButton);
                            linearLayout.addView(neutralButton);
                        } else if (neutralButton != null) {
                            linearLayout.removeView(neutralButton);
                            linearLayout.addView(neutralButton);
                        } else if (negativeButton != null) {
                            linearLayout.removeView(negativeButton);
                            linearLayout.addView(negativeButton);
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(TAG, "Positive button may not fits in the window, can't change layout orientation to vertical");
            }
        }
    };
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnDismissListener dismissListener = dialog -> AppRate.with(context).clearRateDialog();
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            final String packageName = AppInformation.getInstance(context).getAppPackageName();
            if ((packageName != null) && (packageName.hashCode() != EMPTY_STRING.hashCode())) {
                final Intent[] intentsToAppStores = getIntentsForStores(packageName);
                if (intentsToAppStores == null) {
                    Log.w(TAG, "Failed to rate app, can't create intents for store");
                } else {
                    try {
                        if (intentsToAppStores.length == 0) {
                            Log.w(TAG, "Failed to rate app, no intent found for startActivity (intentsToAppStores.length == 0)");
                        } else if (intentsToAppStores[0] == null) {
                            throw new ActivityNotFoundException("Failed to rate app, no intent found for startActivity (intentsToAppStores[0] == null)");
                        } else {
                            context.startActivity(intentsToAppStores[0]);
                        }
                    } catch (ActivityNotFoundException e) {
                        Log.w(TAG, "Failed to rate app, no activity found for " + intentsToAppStores[0], e);
                        final byte intentsToAppStoresNumber = (byte) intentsToAppStores.length;
                        if (intentsToAppStoresNumber > 1) {
                            boolean isCatch;
                            for (byte b = 1; b < intentsToAppStoresNumber; b++) { // intentsToAppStores[1] - second intent in the array
                                try {
                                    if (intentsToAppStores[b] == null) {
                                        throw new ActivityNotFoundException("Failed to rate app, no intent found for startActivity (intentsToAppStores[" + b + "] == null)");
                                    } else {
                                        context.startActivity(intentsToAppStores[b]);
                                    }
                                    isCatch = false;
                                } catch (ActivityNotFoundException ex) {
                                    Log.w(TAG, "Failed to rate app, no activity found for " + intentsToAppStores[b], ex);
                                    isCatch = true;
                                }
                                if (!isCatch) {
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                Log.w(TAG, "Failed to rate app, can't get app package name");
            }
            setIsAgreeShowDialog(context, false);
            if (listener != null) listener.onClickButton((byte) which);
        }
    };
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            setIsAgreeShowDialog(context, false);
            if (listener != null) listener.onClickButton((byte) which);
        }
    };
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener neutralListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            setRemindInterval(context);
            setRemindLaunchesNumber(context);
            if (listener != null) listener.onClickButton((byte) which);
        }
    };

    @SuppressWarnings("WeakerAccess")
    protected DefaultDialogManager(final Context context, final DialogOptions dialogOptions, final StoreOptions storeOptions) {
        this.context = context;
        this.dialogOptions = dialogOptions;
        this.storeOptions = storeOptions;
        this.listener = dialogOptions.getListener();
    }

    @Nullable
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

    @SuppressWarnings("WeakerAccess")
    protected void setContext(Context context){
        this.context = context;
    }

    /**
     * <p>Creates {@link android.app.AlertDialog.Builder}.</p>
     *
     * @param context activity context
     * @param themeResId theme resource ID
     * @return created {@link android.app.AlertDialog.Builder} object
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    protected AlertDialog.Builder getDialogBuilder(@NonNull final Context context, final int themeResId) {
        return Utils.getDialogBuilder(context, themeResId);
    }

    /**
     * <p>Creates Rate Dialog.</p>
     *
     * @return created dialog
     */
    @Nullable
    @Override
    public Dialog createDialog() {
        AlertDialog.Builder builder = getDialogBuilder(context, dialogOptions.getThemeResId());

        if (builder == null) {
            return null;
        }

        builder.setMessage(dialogOptions.getMessageText(context));

        if (dialogOptions.shouldShowTitle()) {
            builder.setTitle(dialogOptions.getTitleText(context));
        }

        builder.setCancelable(dialogOptions.getCancelable());

        View view = dialogOptions.getView();
        if (view != null) {
            builder.setView(view);
        }

        builder.setPositiveButton(dialogOptions.getPositiveText(context), positiveListener);

        if (dialogOptions.shouldShowNeutralButton()) {
            builder.setNeutralButton(dialogOptions.getNeutralText(context), neutralListener);
        }

        if (dialogOptions.shouldShowNegativeButton()) {
            builder.setNegativeButton(dialogOptions.getNegativeText(context), negativeListener);
        }

        final AlertDialog alertDialog = builder.create();
        if (alertDialog != null) {
            alertDialog.setOnShowListener(showListener);
            alertDialog.setOnDismissListener(dismissListener);
        }

        return alertDialog;
    }

    /**
     * <p>DefaultDialogManager.Factory Class - default dialog manager factory class implements
     * {@link DialogManager.Factory} interface of the AndroidRate library.</p>
     * <p>You can extend DefaultDialogManager.Factory Class and use
     * {@link AppRate#setDialogManagerFactory(DialogManager.Factory)} if you want to use fully
     * custom dialog (from v7 AppCompat library etc.).</p>
     *
     * @since    1.0.2
     * @version  1.2.1
     * @author   Alexander Savin
     * @author   Antoine Vianey
     * @see DialogManager.Factory
     */

    static class Factory implements DialogManager.Factory {

        Factory() {
            if (singleton != null) {
                singleton.clear();
            }
        }

        /** Clear {@link DefaultDialogManager} singleton */
        @Override
        public void clearDialogManager() {
            if (singleton != null) {
                singleton.clear();
            }
        }

        /**
         * <p>Creates {@link DefaultDialogManager} singleton object.</p>
         *
         * @param context context
         * @param dialogOptions Rate Dialog options
         * @param storeOptions App store options
         * @return {@link DefaultDialogManager} singleton object
         */
        @Override
        public DialogManager createDialogManager(final Context context, final DialogOptions dialogOptions, final StoreOptions storeOptions) {
            if ((singleton == null) || (singleton.get() == null)) {
                synchronized (DefaultDialogManager.class) {
                    if ((singleton == null) || (singleton.get() == null)) {
                        if (singleton != null) {
                            singleton.clear();
                        }
                        singleton = new WeakReference<>(new DefaultDialogManager(context, dialogOptions, storeOptions));
                    } else {
                        singleton.get().setContext(context);
                    }
                }
            } else {
                singleton.get().setContext(context);
            }
            return singleton.get();
        }
    }
}