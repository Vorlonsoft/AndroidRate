/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.P
import android.util.Log
import com.vorlonsoft.android.rate.Constants.Utils.EMPTY_STRING
import com.vorlonsoft.android.rate.Constants.Utils.TAG

/**
 * AppInformation Object - the app information object of the AndroidRate library.
 *
 * Contains [PackageManager] exceptions constants and [getIcon], [getLongVersionCode],
 * [getPackageName], [getVersionCode], [getVersionCodeMajor], [getVersionName] functions.
 *
 * @since    1.2.1
 * @version  1.2.1
 * @author   Alexander Savin
 */
@Suppress("KDocUnresolvedReference")
internal object AppInformation {
    /** The [PackageManager.getPackageInfo] exception log message. */
    private const val packageInfoException: String = "Failed to get app package info."
    /** The [PackageManager.getApplicationIcon] exception log message. */
    private const val applicationIconException: String = "Failed to get app icon."

    /**
     * Returns the icon associated with an application.
     *
     * @param context context
     * @return the image of the icon or the default application's icon if it couldn't be found, null
     * if the resources for the application couldn't be loaded.
     */
    @JvmStatic
    fun getIcon(context: Context): Drawable? {
        return try {
            context.packageManager.getApplicationIcon(getPackageName(context))
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, applicationIconException, e)
            null
        }
    }

    /**
     * Returns the [versionCode][android.R.styleable.AndroidManifest_versionCode] and the
     * [versionCodeMajor][android.R.styleable.AndroidManifest_versionCodeMajor] combined together as
     * a single long value.
     *
     * The [versionCodeMajor][android.R.styleable.AndroidManifest_versionCodeMajor] is placed in the
     * upper 32 bits.
     *
     * @param context context
     * @return the [versionCode][android.R.styleable.AndroidManifest_versionCode] and the
     * [versionCodeMajor][android.R.styleable.AndroidManifest_versionCodeMajor] combined together
     * @see getVersionCode
     * @see getVersionCodeMajor
     */
    @JvmStatic
    fun getLongVersionCode(context: Context): Long {
        return try {
            if (SDK_INT < P) {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(getPackageName(context), 0)
                                    .versionCode.toLong() and 0b11111111_11111111_11111111_11111111L
            } else {
                context.packageManager.getPackageInfo(getPackageName(context), 0)
                                                                                    .longVersionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, packageInfoException, e)
            0L
        }
    }

    /**
     * Returns the name of the app's package.
     *
     * @param context context
     * @return the name of the app's package
     */
    @JvmStatic
    fun getPackageName(context: Context): String {
        return context.packageName
    }

    /**
     * Returns the version number of the app's package, as specified by the &lt;manifest&gt; tag's
     * [versionCode][android.R.styleable.AndroidManifest_versionCode] attribute.
     *
     * @param context context
     * @return the version number of the app's package
     * @see getVersionCodeMajor
     * @see getLongVersionCode
     */
    @JvmStatic
    fun getVersionCode(context: Context): Int {
        return try {
            if (SDK_INT < P) {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(getPackageName(context), 0).versionCode
            } else {
                (context.packageManager.getPackageInfo(getPackageName(context), 0)
                                .longVersionCode and 0b11111111_11111111_11111111_11111111L).toInt()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, packageInfoException, e)
            0
        }
    }

    /**
     * Returns the major version number of the app's package, as specified by the &lt;manifest&gt;
     * tag's [versionCodeMajor][android.R.styleable.AndroidManifest_versionCodeMajor] attribute,
     * **0 if API < 28**.
     *
     * @param context context
     * @return the major version number of the app's package, <b>0 if API < 28</b>
     * @see getVersionCode
     * @see getLongVersionCode
     */
    @JvmStatic
    fun getVersionCodeMajor(context: Context): Int {
        return if (SDK_INT < P) 0 else try {
            context.packageManager.getPackageInfo(getPackageName(context), 0)
                                                            .longVersionCode.ushr(32).toInt()
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, packageInfoException, e)
            0
        }
    }

    /**
     * Returns the version name of the app's package, as specified by the &lt;manifest&gt; tag's
     * [versionName][android.R.styleable.AndroidManifest_versionName] attribute.
     *
     * @param context context
     * @return the version name of the app's package
     */
    @JvmStatic
    fun getVersionName(context: Context): String {
        return try {
            context.packageManager.getPackageInfo(getPackageName(context), 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, packageInfoException, e)
            EMPTY_STRING
        }
    }
}