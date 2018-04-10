/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

final class UriHelper {

    private static final String GOOGLE_PLAY = "market://details?id=";

    private static final String GOOGLE_PLAY_WEB = "https://play.google.com/store/apps/details?id=";

    private static final String AMAZON_APPSTORE = "amzn://apps/android?p=";

    private static final String AMAZON_APPSTORE_WEB = "https://www.amazon.com/gp/mas/dl/android?p=";

    private static final String SAMSUNG_GALAXY_APPS = "samsungapps://ProductDetail/";

    private static final String SAMSUNG_GALAXY_APPS_WEB = "https://www.samsungapps.com/appquery/appDetail.as?appId=";

    private static final String TENCENT_APP_STORE = "http://android.myapp.com/myapp/detail.htm?apkName=";

    private static final String MI_APPSTORE = "http://app.mi.com/details?id=";

    private UriHelper() {
    }

    static Uri getGooglePlay(String packageName) {
        return packageName == null ? null : Uri.parse(GOOGLE_PLAY + packageName);
    }

    static Uri getGooglePlayWeb(String packageName) {
        return packageName == null ? null : Uri.parse(GOOGLE_PLAY_WEB + packageName);
    }

    static Uri getAmazonAppstore(String packageName) {
        return packageName == null ? null : Uri.parse(AMAZON_APPSTORE + packageName);
    }

    static Uri getAmazonAppstoreWeb(String packageName) {
        return packageName == null ? null : Uri.parse(AMAZON_APPSTORE_WEB + packageName);
    }

    static Uri getSamsungGalaxyApps(String packageName) {
        return packageName == null ? null : Uri.parse(SAMSUNG_GALAXY_APPS + packageName);
    }

    static Uri getSamsungGalaxyAppsWeb(String packageName) {
        return packageName == null ? null : Uri.parse(SAMSUNG_GALAXY_APPS_WEB + packageName);
    }

    static Uri getTencentAppStore(String packageName) {
        return packageName == null ? null : Uri.parse(TENCENT_APP_STORE + packageName);
    }

    static Uri getMiAppstore(String packageName) {
        return packageName == null ? null : Uri.parse(MI_APPSTORE + packageName);
    }

    static boolean isPackageExists(Context context, @SuppressWarnings("SameParameterValue") String targetPackage) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage)) {
                return true;
            }
        }
        return false;
    }
}