/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.app.Dialog;
import android.content.Context;

/**
 * <p>DialogManager Interface - dialog manager interface of the AndroidRate library.</p>
 * <p>You can implement DialogManager Interface and use
 * {@link AppRate#setDialogManagerFactory(DialogManager.Factory)} if you want to use fully custom
 * dialog (from v7 AppCompat library etc.).</p>
 *
 * @since    1.0.2
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Antoine Vianey
 * @see DialogManager.Factory
 */

public interface DialogManager {

    /**
     * <p>Creates Rate Dialog.</p>
     *
     * @return created dialog
     */
    Dialog createDialog();

    /**
     * <p>DialogManager.Factory Interface - dialog manager factory interface of the AndroidRate
     * library.</p>
     * <p>You can implement DialogManager.Factory Interface and use
     * {@link AppRate#setDialogManagerFactory(DialogManager.Factory)} if you want to use fully
     * custom dialog (from v7 AppCompat library etc.).</p>
     *
     * @since    1.0.2
     * @version  1.2.1
     * @author   Alexander Savin
     * @author   Antoine Vianey
     * @see DialogManager
     */

    interface Factory {

        /** <p>Clears {@link DialogManager} implementation singleton.</p> */
        void clearDialogManager();

        /**
         * <p>Creates {@link DialogManager} implementation singleton object.</p>
         *
         * @param context context
         * @param dialogOptions Rate Dialog options
         * @param storeOptions App store options
         * @return {@link DialogManager} implementation singleton object
         */
        DialogManager createDialogManager(final Context context, final DialogOptions dialogOptions,
                                          final StoreOptions storeOptions);
    }
}