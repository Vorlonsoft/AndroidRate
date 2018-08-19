/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.app.Dialog;
import android.content.Context;

/**
 * <p>AndroidRate is a library to help you promote your Android app
 * by prompting users to rate the app after using it for a few days.</p>
 * <p>DialogManager Interface - dialog manager interface of
 * the AndroidRate library. You can implements it and use
 * {@code AppRate.with(this).setDialogManagerFactory(DialogManager.Factory)]}
 * if you want to use fully custom dialog (from support library etc.)</p>
 *
 * @author   Alexander Savin
 * @author   Antoine Vianey
 * @version  1.1.9
 * @since    1.0.2
 */

public interface DialogManager {

    Dialog createDialog();

    /**
     * <p>AndroidRate is a library to help you promote your Android app
     * by prompting users to rate the app after using it for a few days.</p>
     * <p>DialogManager.Factory Interface - dialog manager factory interface
     * of the AndroidRate library. You can implements it and use
     * {@code AppRate.with(this).setDialogManagerFactory(DialogManager.Factory)]}
     * if you want to use fully custom dialog (from support library etc.)</p>
     *
     * @author   Alexander Savin
     * @author   Antoine Vianey
     * @version  1.1.9
     * @since    1.0.2
     */

    interface Factory {
        DialogManager createDialogManager(final Context context, final DialogOptions dialogOptions, final StoreOptions storeOptions);
    }
}