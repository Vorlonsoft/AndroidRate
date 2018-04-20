/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;

final class Utils {

    private Utils() {
    }

    private static boolean isLollipop() {
        return ((Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) || (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1));
    }

    private static int getDialogTheme() {
        return isLollipop() ? R.style.CustomLollipopDialogStyle : 0;
    }

    static AlertDialog.Builder getDialogBuilder(final Context context, final int themeResId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return new AlertDialog.Builder(context);
        } else {
            return new AlertDialog.Builder(context, themeResId != 0 ? themeResId : getDialogTheme());
        }
    }

}