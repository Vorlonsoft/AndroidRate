/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build

internal object Utils {

    val isLollipop: Boolean
        get() = Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1

    val dialogTheme: Int
        get() = if (isLollipop) R.style.CustomLollipopDialogStyle else 0

    fun underHoneyComb(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
    }

    @SuppressLint("NewApi")
    fun getDialogBuilder(context: Context, pThemeResId: Int?): AlertDialog.Builder {
        return if (underHoneyComb()) {
            AlertDialog.Builder(context)
        } else {
            AlertDialog.Builder(context, dialogTheme)
        }
    }

}
