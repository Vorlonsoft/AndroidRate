/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.app.Dialog
import android.content.Context

/**
 * DialogManager Interface - the dialog manager interface of the AndroidRate library.
 *
 * You can implement the [DialogManager Interface][DialogManager] and use
 * [AppRate.setDialogManagerFactory] if you want to use a fully custom dialog (from the v7 AppCompat
 * library, etc.).
 *
 * @since    1.0.2
 * @version  2.0.0
 * @author   Alexander Savin
 * @author   Antoine Vianey
 * @see DialogManager.Factory
 */
interface DialogManager {
    /**
     * Creates the Rate Dialog.
     *
     * @return created dialog
     */
    fun createDialog(): Dialog?

    /**
     * DialogManager.Factory Interface - the dialog manager factory interface of the AndroidRate
     * library.
     *
     * You can implement the [DialogManager.Factory Interface][DialogManager.Factory] and use
     * [AppRate.setDialogManagerFactory] if you want to use a fully custom dialog (from v7 AppCompat
     * library, etc.).
     *
     * @since    1.0.2
     * @version  2.0.0
     * @author   Alexander Savin
     * @author   Antoine Vianey
     * @see DialogManager
     */
    interface Factory {
        /** Clears the [DialogManager] implementation singleton. */
        fun clearDialogManager()

        /**
         * Creates the [DialogManager] implementation singleton object.
         *
         * @param context context
         * @param dialogOptions Rate Dialog options
         * @param storeOptions App store options
         * @return [DialogManager] implementation singleton object
         */
        fun createDialogManager(context: Context,
                                dialogOptions: DialogOptions,
                                storeOptions: StoreOptions): DialogManager
    }
}