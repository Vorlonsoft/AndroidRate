/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import com.vorlonsoft.android.rate.Constants.Utils.EMPTY_STRING
import com.vorlonsoft.android.rate.Constants.Utils.TAG

/**
 * AppInformation Class - the app information class of the AndroidRate library, an thread-safe and
 * fast singleton implementation.
 *
 * @property appLongVersionCode the [versionCode][android.R.styleable.AndroidManifest_versionCode]
 * and the [versionCodeMajor][android.R.styleable.AndroidManifest_versionCodeMajor] combined
 * together as a single long value, the
 * [versionCodeMajor][android.R.styleable.AndroidManifest_versionCodeMajor] is placed in the upper
 * 32 bits.
 * @property appPackageName the name of the app's package
 * @property appVersionName the version name of the app's package, as specified by the
 * &lt;manifest&gt; tag's [versionName][android.R.styleable.AndroidManifest_versionName] attribute.
 * @property appIcon the image of the icon, the default application's icon if it couldn't be found,
 * null if the resources for the application couldn't be loaded.
 * @constructor Creates the AppInformation Class singleton.
 * @since    1.2.1
 * @version  1.2.1
 * @author   Alexander Savin
 */
@Suppress("KDocUnresolvedReference")
internal class AppInformation private constructor(
        val appLongVersionCode: Long,
        val appPackageName: String,
        val appVersionName: String,
        val appIcon: Drawable?) {
    /**
     * The version number of the app's package, as specified by the &lt;manifest&gt; tag's
     * [versionCode][android.R.styleable.AndroidManifest_versionCode] attribute.
     *
     * @see appVersionCodeMajor
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val appVersionCode: Int
        get() = (appLongVersionCode and 0b11111111_11111111_11111111_11111111L).toInt()
    /**
     * The major version number of the app's package, as specified by the &lt;manifest&gt; tag's
     * [versionCodeMajor][android.R.styleable.AndroidManifest_versionCodeMajor] attribute, **0 if
     * API < 28**.
     *
     * @see appVersionCode
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val appVersionCodeMajor: Int
        get() = appLongVersionCode.ushr(32).toInt()

    /** Contains the [AppInformation] singleton object and the function to get its instance. */
    internal companion object {
        /** The [AppInformation] singleton object. */
        @Volatile
        private var singleton: AppInformation? = null

        /**
         * Creates the [AppInformation] singleton object.
         *
         * @param context context
         * @return the [AppInformation] singleton object
         */
        @JvmStatic
        fun getInstance(context: Context): AppInformation? {
            if (singleton === null) {
                val appLongVersionCode: Long
                val appPackageName = context.packageName
                val appVersionName: String
                val appIcon: Drawable?
                val packageInfo: PackageInfo?
                val pm = context.packageManager
                appIcon = try {
                    pm.getApplicationIcon(appPackageName)
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.i(TAG, "Failed to get app icon.", e)
                    null
                }
                packageInfo = try {
                    pm.getPackageInfo(appPackageName, 0)
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.i(TAG, "Failed to get app package info.", e)
                    null
                }
                if (packageInfo !== null) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                        @Suppress("DEPRECATION")
                        appLongVersionCode = packageInfo.versionCode.toLong() and
                                             0b11111111_11111111_11111111_11111111L
                    } else {
                        appLongVersionCode = packageInfo.longVersionCode
                    }
                    appVersionName = packageInfo.versionName
                } else {
                    appLongVersionCode = 0L
                    appVersionName = EMPTY_STRING
                }

                synchronized(AppInformation::class) {
                    if (singleton === null) {
                        singleton = AppInformation(appLongVersionCode, appPackageName,
                                                   appVersionName, appIcon)
                    }
                }
            }
            return singleton
        }
    }
}