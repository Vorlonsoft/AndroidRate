/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

/**
 * <p>AppCompatDialogManager Class - v7 AppCompat library dialog manager class implements
 * {@link DialogManager} interface of the AndroidRate library. <b>DefaultDialogManager Class will be
 * used for API levels below 14 instead, see
 * {@link AppCompatDialogManager.Factory#createDialogManager(Context, DialogOptions, StoreOptions)}.</b></p>
 * <p>You can extend AppCompatDialogManager Class and use
 * {@link AppRate#setDialogManagerFactory(DialogManager.Factory)} if you want to use fully custom
 * dialog (from v7 AppCompat library). AppCompatDialogManager Class is thread-safe and a fast
 * singleton implementation inside library, not outside (protected, not private constructor).</p>
 *
 * @since    1.2.1
 * @version  1.2.1
 * @author   Alexander Savin
 * @see AppCompatDialogManager.Factory
 * @see DefaultDialogManager
 * @see DialogManager
 */

public class AppCompatDialogManager extends DefaultDialogManager implements DialogManager {
    /** <p>The WeakReference to the {@link AppCompatDialogManager} singleton object.</p> */
    private static volatile WeakReference<DialogManager> singleton = null;

    @SuppressWarnings("WeakerAccess")
    @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected AppCompatDialogManager(final Context context, final DialogOptions dialogOptions, final StoreOptions storeOptions) {
        super(context, dialogOptions, storeOptions);
    }

    /**
     * <p>Creates {@link androidx.appcompat.app.AlertDialog.Builder}.</p>
     *
     * @param context activity context
     * @param themeResId theme resource ID
     * @return created {@link androidx.appcompat.app.AlertDialog.Builder} object
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected AlertDialog.Builder getAppCompatDialogBuilder(@NonNull final Context context, final int themeResId) {
        return Utils.getAppCompatDialogBuilder(context, themeResId);
    }

    /**
     * <p>Creates Rate Dialog.</p>
     *
     * @return created dialog
     */
    @Nullable
    @Override
    @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public Dialog createDialog() {
        AlertDialog.Builder builder = getAppCompatDialogBuilder(context, dialogOptions.getThemeResId());

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
     * <p>AppCompatDialogManager.Factory Class - v7 AppCompat library dialog manager factory class
     * implements {@link DialogManager.Factory} interface of the AndroidRate library.</p>
     * <p>You can extend AppCompatDialogManager.Factory Class and use
     * {@link AppRate#setDialogManagerFactory(DialogManager.Factory)} if you want to use fully
     * custom dialog (from v7 AppCompat library).</p>
     *
     * @since    1.2.1
     * @version  1.2.1
     * @author   Alexander Savin
     * @see DialogManager.Factory
     */

    public static class Factory implements DialogManager.Factory {

        public Factory() {
            if (singleton != null) {
                singleton.clear();
            }
        }

        /** <p>Clear {@link AppCompatDialogManager} singleton.</p> */
        @Override
        public void clearDialogManager() {
            if (singleton != null) {
                singleton.clear();
            }
        }

        /**
         * <p>Creates {@link AppCompatDialogManager} singleton object.
         * <b>{@link DefaultDialogManager} singleton object will be created for API levels 13 and
         * below instead</b></p>
         *
         * @param context context
         * @param dialogOptions Rate Dialog options
         * @param storeOptions App store options
         * @return {@link AppCompatDialogManager} singleton object for API levels 14 and higher,
         * <b>{@link DefaultDialogManager} singleton object for API levels 13 and below</b>
         */
        @Override
        public DialogManager createDialogManager(final Context context, final DialogOptions dialogOptions, final StoreOptions storeOptions) {
            if ((singleton == null) || (singleton.get() == null)) {
                synchronized (AppCompatDialogManager.class) {
                    if ((singleton == null) || (singleton.get() == null)) {
                        if (singleton != null) {
                            singleton.clear();
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            singleton = new WeakReference<>(new AppCompatDialogManager(context, dialogOptions, storeOptions));
                        } else {
                            singleton = new WeakReference<>(new DefaultDialogManager(context, dialogOptions, storeOptions));
                        }
                    }else {
                        ((DefaultDialogManager) singleton.get()).setContext(context);
                    }
                }
            } else {
                ((DefaultDialogManager) singleton.get()).setContext(context);
            }
            return singleton.get();
        }
    }
}