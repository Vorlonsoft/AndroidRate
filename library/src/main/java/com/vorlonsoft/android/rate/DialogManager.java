/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.app.Dialog;
import android.content.Context;

/**
 * <p>DialogManager Interface - dialog manager interface of
 * the AndroidRate library. You can implements it and use
 * {@code AppRate.with(this).setDialogManagerFactory(DialogManager.Factory)]}
 * if you want to use fully custom dialog (from support library etc.)</p>
 *
 * @since    1.0.2
 * @version  1.2.0
 * @author   Alexander Savin
 * @author   Antoine Vianey
 */

public interface DialogManager {

    Dialog createDialog();

    /**
     * <p>DialogManager.Factory Interface - dialog manager factory interface
     * of the AndroidRate library. You can implements it and use
     * {@code AppRate.with(this).setDialogManagerFactory(DialogManager.Factory)]}
     * if you want to use fully custom dialog (from support library etc.)</p>
     *
     * @since    1.0.2
     * @version  1.2.0
     * @author   Alexander Savin
     * @author   Antoine Vianey
     */

    interface Factory {

        /** Clear DialogManager singleton */
        void clearDialogManager();

        DialogManager createDialogManager(final Context context, final DialogOptions dialogOptions, final StoreOptions storeOptions);
    }
}