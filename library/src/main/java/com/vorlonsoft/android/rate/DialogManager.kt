/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.app.Dialog
import android.content.Context

interface DialogManager {

    interface Factory {
        fun createDialogManager(context: Context, options: DialogOptions): DialogManager
    }

    fun createDialog(): Dialog
}