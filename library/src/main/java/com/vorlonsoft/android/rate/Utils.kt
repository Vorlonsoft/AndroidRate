/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.app.AlertDialog
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.HONEYCOMB
import android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION_CODES.LOLLIPOP_MR1
import androidx.annotation.RequiresApi
import com.vorlonsoft.android.rate.Constants.Utils.EMPTY_STRING
import com.vorlonsoft.android.rate.Constants.Utils.EMPTY_STRING_ARRAY

/**
 * Utils Object - the utils object of the AndroidRate library.
 *
 * Contains [getDialogBuilder], [getAppCompatDialogBuilder], [isPackagesExists] functions.
 *
 * @since    0.5.0
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */
internal object Utils {
    /**
     * Creates [android.app.AlertDialog.Builder].
     *
     * @param context activity context
     * @param themeResId theme resource ID
     * @return created [android.app.AlertDialog.Builder] object
     */
    @JvmStatic
    fun getDialogBuilder(context: Context, themeResId: Int): AlertDialog.Builder {
        return when {
            themeResId == 0 -> AlertDialog.Builder(context)
            SDK_INT >= HONEYCOMB -> AlertDialog.Builder(context, themeResId)
            else -> AlertDialog.Builder(context)
        }
    }

    /**
     * Creates [androidx.appcompat.app.AlertDialog.Builder].
     *
     * @param context activity context
     * @param themeResId theme resource ID
     * @return created [androidx.appcompat.app.AlertDialog.Builder] object
     */
    @RequiresApi(ICE_CREAM_SANDWICH)
    @JvmStatic
    fun getAppCompatDialogBuilder(context: Context,
                                  themeResId: Int): androidx.appcompat.app.AlertDialog.Builder {
        return when (themeResId) {
            0 -> androidx.appcompat.app.AlertDialog.Builder(context)
            else -> androidx.appcompat.app.AlertDialog.Builder(context, themeResId)
        }
    }

    /**
     * Checks whether target packages exist on the user device or not.
     *
     * @param context activity context
     * @param targetPackages target packages
     * @return an string array of existing packages or an empty string array if nothing was found
     */
    @JvmStatic
    fun isPackagesExists(context: Context, targetPackages: Array<String?>): Array<String?> {
        if (targetPackages.isEmpty()) {
            return EMPTY_STRING_ARRAY
        }

        val applicationInfo: List<ApplicationInfo> = context.packageManager
                                                                    .getInstalledApplications(0)

        if (targetPackages.size == 1) {
            if ((targetPackages[0] != null) && (targetPackages[0] != EMPTY_STRING)) {
                for (anApplicationInfo in applicationInfo) {
                    if (targetPackages[0] == anApplicationInfo.packageName) {
                        return arrayOf(targetPackages[0])
                    }
                }
            }
            return EMPTY_STRING_ARRAY
        } else {
            val packageNames: ArrayList<String> = ArrayList()
            for (aTargetPackage in targetPackages) {
                if ((aTargetPackage != null) && (aTargetPackage != EMPTY_STRING)) {
                    for (anApplicationInfo in applicationInfo) {
                        if (aTargetPackage == anApplicationInfo.packageName) {
                            packageNames.add(aTargetPackage)
                            break
                        }
                    }
                }
            }
            return packageNames.toTypedArray()
        }
    }
}