/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.annotation.SuppressLint;
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

    @SuppressLint("ObsoleteSdkInt")
    static AlertDialog.Builder getDialogBuilder(Context context, Integer pThemeResId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return new AlertDialog.Builder(context);
        } else {
            return new AlertDialog.Builder(context, pThemeResId != null ? pThemeResId : getDialogTheme());
        }
    }

}