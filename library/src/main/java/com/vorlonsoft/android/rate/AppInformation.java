package com.vorlonsoft.android.rate;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.vorlonsoft.android.rate.Constants.Utils.TAG;

/**
 * <p>AppInformation Class - app information class of the AndroidRate library, thread-safe and a
 * fast singleton implementation.</p>
 *
 * @since    1.2.1
 * @version  1.2.1
 * @author   Alexander Savin
 */

final class AppInformation {

    /** <p>Thread-safe and a fast {@link AppInformation} singleton implementation.</p> */
    private static volatile AppInformation singleton = null;
    /** <p>The version number of app's package.</p> */
    private final int appVersionCode;
    /** <p>The major version number of app's package. <b>0 if API < 28.</b></p> */
    private final int appVersionCodeMajor;
    /** <p>The versionCode and the versionCodeMajor combined together as a single long value. <b>0 if API < 28.</b></p> */
    private final long appLongVersionCode;
    /** <p>The name of app's package.</p> */
    private final String appPackageName;
    /** <p>The version name of app's package.</p> */
    private final String appVersionName;
    /** <p>The icon associated with an app.</p> */
    private final Drawable appIcon;

    /**
     * <p>Constructor of AppInformation class.</p>
     *
     * @param appVersionCode the version number of app's package
     * @param appLongVersionCode the versionCode and the versionCodeMajor combined together as a single long value
     * @param appPackageName the name of app's package
     * @param appVersionName the version name of app's package
     * @param appIcon the icon associated with an app
     */
    private AppInformation(final int appVersionCode,
                           final long appLongVersionCode,
                           @NonNull final String appPackageName,
                           @NonNull final String appVersionName,
                           @Nullable final Drawable appIcon) {
        this.appVersionCode = appVersionCode;
        this.appVersionCodeMajor = (int) (appLongVersionCode >>> 32);
        this.appLongVersionCode = appLongVersionCode;
        this.appPackageName = appPackageName;
        this.appVersionName = appVersionName;
        this.appIcon = appIcon;
    }

    /**
     * <p>Creates thread-safe and a fast {@link AppInformation} singleton implementation.</p>
     *
     * @param context context
     * @return thread-safe and a fast {@link AppInformation} singleton implementation
     */
    @NonNull
    static AppInformation getInstance(@NonNull final Context context) {
        if (singleton == null) {
            final int appVersionCode;
            final long appLongVersionCode;
            final String appPackageName = context.getPackageName();
            final String appVersionName;
            Drawable appIcon;
            PackageInfo packageInfo;
            PackageManager pm = context.getPackageManager();
            try {
                appIcon = pm.getApplicationIcon(appPackageName);
            } catch (PackageManager.NameNotFoundException e) {
                Log.i(TAG, "Failed to get app icon", e);
                appIcon = null;
            }
            try {
                packageInfo = pm.getPackageInfo(appPackageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.i(TAG, "Failed to get app package info", e);
                packageInfo = null;
            }
            if (packageInfo != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    appVersionCode = packageInfo.versionCode;
                    appLongVersionCode = 0L;
                } else {
                    appLongVersionCode = packageInfo.getLongVersionCode();
                    appVersionCode = (int) (appLongVersionCode & 0b11111111111111111111111111111111L);
                }
                appVersionName = packageInfo.versionName;
            } else {
                appVersionCode = 0;
                appLongVersionCode = 0L;
                appVersionName = "";
            }
            synchronized (AppRate.class) {
                if (singleton == null) {
                    singleton = new AppInformation(appVersionCode, appLongVersionCode, appPackageName, appVersionName, appIcon);
                }
            }
        }
        return singleton;
    }

    /**
     * <p>Returns the version number of app's package, as specified by the &lt;manifest&gt; tag's
     * {@link android.R.styleable#AndroidManifest_versionCode versionCode} attribute.</p>
     *
     * @return the version number of app's package
     * @see #getAppVersionCodeMajor()
     * @see #getAppLongVersionCode()
     */
    @SuppressWarnings("WeakerAccess")
    int getAppVersionCode() {
        return appVersionCode;
    }

    /**
     * <p>Returns the major version number of app's package, as specified by the &lt;manifest&gt; tag's
     * {@link android.R.styleable#AndroidManifest_versionCodeMajor versionCodeMajor} attribute.</p>
     *
     * @return the major version number of app's package, <b>0 if API < 28</b>
     * @see #getAppVersionCode()
     * @see #getAppLongVersionCode()
     */
    @SuppressWarnings("WeakerAccess")
    int getAppVersionCodeMajor() {
        return appVersionCodeMajor;
    }

    /**
     * <p>Returns {@link android.R.styleable#AndroidManifest_versionCode versionCode} and
     * {@link android.R.styleable#AndroidManifest_versionCodeMajor versionCodeMajor} combined
     * together as a single long value.</p>
     * <p>The {@link android.R.styleable#AndroidManifest_versionCodeMajor versionCodeMajor} is
     * placed in the upper 32 bits.</p>
     *
     * @return versionCode and versionCodeMajor combined together as a single long value, <b>0 if
     *         API < 28</b>
     * @see #getAppVersionCode()
     * @see #getAppVersionCodeMajor()
     */
    @SuppressWarnings("WeakerAccess")
    long getAppLongVersionCode() {
        return appLongVersionCode;
    }

    /**
     * <p>Returns the name of app's package.</p>
     *
     * @return the name of app's package
     */
    String getAppPackageName() {
        return appPackageName;
    }

    /**
     * <p>Returns the version name of app's package, as specified by the &lt;manifest&gt; tag's
     * {@link android.R.styleable#AndroidManifest_versionName versionName} attribute.</p>
     *
     * @return the version name of app's package
     */
    String getAppVersionName() {
        return appVersionName;
    }

    /**
     * <p>Returns the icon associated with an application.</p>
     *
     * @return the image of the icon, or the default application icon if it could not be found.
     *         Returns null if the resources for the application could not be loaded.
     */
    @Nullable
    Drawable getAppIcon() {
        return appIcon;
    }
}