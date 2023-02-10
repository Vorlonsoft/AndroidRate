/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import static com.vorlonsoft.android.rate.DialogType.CLASSIC;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.jetbrains.annotations.NotNull;

/**
 * <p>AppCompatDialogManager Class - v7 AppCompat library dialog manager class implements
 * {@link DialogManager} interface of the AndroidRate library. <b>DefaultDialogManager Class will be
 * used for API levels below 14 instead, see
 * {@link AppCompatDialogManager.Factory#createDialogManager(Context, DialogOptions, StoreOptions)}
 * .</b></p>
 * <p>You can extend AppCompatDialogManager Class and use
 * {@link AppRate#setDialogManagerFactory(DialogManager.Factory)} if you want to use fully custom
 * dialog (from v7 AppCompat library). AppCompatDialogManager Class is an thread-safe and fast
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

    @SuppressWarnings("WeakerAccess")
    protected AppCompatDialogManager(final Context context, final DialogOptions dialogOptions,
                                     final StoreOptions storeOptions) {
        super(context, dialogOptions, storeOptions);
    }

    /**
     * <p>Creates {@link androidx.appcompat.app.AlertDialog.Builder}.</p>
     *
     * @param context activity context
     * @param themeResId theme resource ID
     * @return created {@link androidx.appcompat.app.AlertDialog.Builder} object
     * @see DefaultDialogManager#getDialogBuilder(Context, int)
     */
    @SuppressWarnings("WeakerAccess")
    @NonNull
    protected AlertDialog.Builder getAppCompatDialogBuilder(@NonNull final Context context,
                                                            final int themeResId) {
        return Utils.getAppCompatDialogBuilder(context, themeResId);
    }

    /**
     * <p>Supplies the arguments to the {@link DialogType#CLASSIC CLASSIC} Rate Dialog
     * {@link androidx.appcompat.app.AlertDialog.Builder}.</p>
     *
     * @param builder the {@link DialogType#CLASSIC CLASSIC} Rate Dialog
     *                {@link androidx.appcompat.app.AlertDialog.Builder}
     * @param dialogContext a Context for Rate Dialogs created by this Builder
     * @see DefaultDialogManager#supplyClassicDialogArguments(android.app.AlertDialog.Builder, Context)
     */
    @SuppressWarnings("WeakerAccess")
    protected void supplyAppCompatClassicDialogArguments(@NonNull AlertDialog.Builder builder, @NonNull Context dialogContext) {
        if (Boolean.TRUE.equals(dialogOptions.isShowIcon())) {
            builder.setIcon(dialogOptions.getIcon(dialogContext));
        }
        if (dialogOptions.isShowTitle()) {
            builder.setTitle(dialogOptions.getTitleText(context));
        }
        if (dialogOptions.isShowMessage()) {
            builder.setMessage(dialogOptions.getMessageText(context));
        }
        if (dialogOptions.isShowNeutralButton()) {
            builder.setNeutralButton(dialogOptions.getNeutralText(context), neutralListener);
        }
        if (dialogOptions.isShowNegativeButton()) {
            builder.setNegativeButton(dialogOptions.getNegativeText(context), negativeListener);
        }
        builder.setPositiveButton(dialogOptions.getPositiveText(context), positiveListener);
    }

    /**
     * <p>Creates Rate Dialog.</p>
     *
     * @return created dialog
     */
    @Nullable
    @Override
    public Dialog createDialog() {

        AlertDialog.Builder builder = getAppCompatDialogBuilder(context, dialogOptions.getThemeResId());
        Context dialogContext = builder.getContext();

        final View view = dialogOptions.getView(dialogContext);

        if ((dialogOptions.getType() == CLASSIC) || (view == null)) {
            if (dialogOptions.getType() != CLASSIC) {
                builder = getAppCompatDialogBuilder(context, 0);
                dialogContext = builder.getContext();
            }
            supplyAppCompatClassicDialogArguments(builder, dialogContext);
        } else {
            supplyNonClassicDialogArguments(view, dialogContext);
        }

        final AlertDialog alertDialog = builder
                .setCancelable(dialogOptions.getCancelable())
                .setView(view)
                .create();

        alertDialog.setOnShowListener(showListener);
        alertDialog.setOnDismissListener(dismissListener);

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

        /** <p>Clear {@link AppCompatDialogManager} singleton.</p> */
        @SuppressWarnings("unused")
        @Override
        public void clearDialogManager() { /* needed */ }

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
        @SuppressWarnings("unused")
        @NotNull
        @Override
        public DialogManager createDialogManager(@NotNull final Context context,
                                                 @NotNull final DialogOptions dialogOptions,
                                                 @NotNull final StoreOptions storeOptions) {
            DialogManager.Factory dialogManagerFactory = new DefaultDialogManager.Factory();
            AppRate.with(context).setDialogManagerFactory(dialogManagerFactory);
            return dialogManagerFactory.createDialogManager(context, dialogOptions, storeOptions);
        }
    }
}