/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.app.Dialog;
import android.content.Context;

public interface DialogManager {


    interface Factory {
        DialogManager createDialogManager(final Context context, final DialogOptions options);
    }

    Dialog createDialog();
}