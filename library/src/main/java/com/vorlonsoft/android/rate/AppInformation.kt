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
 * @version  2.0.0
 * @author   Alexander Savin
 */
@Suppress("KDocUnresolvedReference")
internal object AppInformation {
    /** The [PackageManager.getPackageInfo] exception log message. */
    private const val packageInfoException: String = "Failed to get app package info."
    /** The [PackageManager.getApplicationIcon] exception log message. */
    private const val applicationIconException: String = "Failed to get app icon."
    /** The icon associated with an application. */
    @JvmStatic
    private var icon: Drawable? = null
    /** The versionCode and the versionCodeMajor combined together as a single long value. */
    @JvmStatic
    private var longVersionCode: Long? = null
    /** The name of the app's package. */
    @JvmStatic
    private var packageName: String? = null
    /** The version number of the app's package. */
    @JvmStatic
    private var versionCode: Int? = null
    /** The major version number of the app's package. */
    @JvmStatic
    private var versionCodeMajor: Int? = null
    /** The version name of the app's package. */
    @JvmStatic
    private var versionName: String? = null

    /**
     * Returns the icon associated with an application.
     *
     * @param context context
     * @return the image of the icon or the default application's icon if it couldn't be found, null
     * if the resources for the application couldn't be loaded.
     */
    @JvmStatic
    fun getIcon(context: Context): Drawable? {
        if (icon == null) try {
            icon = context.packageManager.getApplicationIcon(getPackageName(context))
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, applicationIconException, e)
        }
        return icon
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
        if (longVersionCode == null) if (SDK_INT < P) {
            @Suppress("DEPRECATION")
            longVersionCode = getVersionCode(context).toLong() and
                              0b11111111_11111111_11111111_11111111L
        } else try {
            longVersionCode = context.packageManager.getPackageInfo(getPackageName(context), 0)
                                                                                    .longVersionCode
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, packageInfoException, e)
        }
        return longVersionCode ?: 0L
    }

    /**
     * Returns the name of the app's package.
     *
     * @param context context
     * @return the name of the app's package
     */
    @JvmStatic
    fun getPackageName(context: Context): String {
        if (packageName == null) packageName = context.packageName
        return packageName ?: EMPTY_STRING
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
        if (versionCode == null) if (SDK_INT < P) try {
            @Suppress("DEPRECATION")
            versionCode = context.packageManager.getPackageInfo(getPackageName(context), 0)
                                                                                        .versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, packageInfoException, e)
        } else {
            versionCode = (getLongVersionCode(context) and
                           0b11111111_11111111_11111111_11111111L).toInt()
        }
        return versionCode ?: 0
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
        if (versionCodeMajor == null) versionCodeMajor = if (SDK_INT < P) 0 else
                                                 getLongVersionCode(context).ushr(32).toInt()
        return versionCodeMajor ?: 0
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
        if (versionName == null) try {
            versionName = context.packageManager.getPackageInfo(getPackageName(context), 0)
                                                                                        .versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, packageInfoException, e)
        }
        return versionName ?: EMPTY_STRING
    }
}