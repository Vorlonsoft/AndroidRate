/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static com.vorlonsoft.android.rate.UriHelper.getAmazonAppstore;
import static com.vorlonsoft.android.rate.UriHelper.getAmazonAppstoreWeb;
import static com.vorlonsoft.android.rate.UriHelper.getBlackBerryWorld;
import static com.vorlonsoft.android.rate.UriHelper.getBlackBerryWorldWeb;
import static com.vorlonsoft.android.rate.UriHelper.getCafeBazaar;
import static com.vorlonsoft.android.rate.UriHelper.getCafeBazaarWeb;
import static com.vorlonsoft.android.rate.UriHelper.getGooglePlay;
import static com.vorlonsoft.android.rate.UriHelper.getGooglePlayWeb;
import static com.vorlonsoft.android.rate.UriHelper.getMiAppstore;
import static com.vorlonsoft.android.rate.UriHelper.getSamsungGalaxyApps;
import static com.vorlonsoft.android.rate.UriHelper.getSlideME;
import static com.vorlonsoft.android.rate.UriHelper.getSlideMEWeb;
import static com.vorlonsoft.android.rate.UriHelper.getTencentAppStore;
import static com.vorlonsoft.android.rate.UriHelper.getTencentAppStoreWeb;
import static com.vorlonsoft.android.rate.UriHelper.getYandexStore;
import static com.vorlonsoft.android.rate.UriHelper.isPackageExists;

final class IntentHelper {

    static final String AMAZON_APPSTORE_PACKAGE_NAME = "com.amazon.venezia";
    static final String BLACKBERRY_WORLD_PACKAGE_NAME = "net.rim.bb.appworld";
    static final String CAFE_BAZAAR_PACKAGE_NAME = "com.farsitel.bazaar";
    static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";
    static final String SAMSUNG_GALAXY_APPS_PACKAGE_NAME = "com.sec.android.app.samsungapps";
    static final String SLIDEME_PACKAGE_NAME = "com.slideme.sam.manager";
    static final String TENCENT_PACKAGE_NAME = "com.tencent.android.qqdownloader";
    static final String YANDEX_STORE_PACKAGE_NAME = "com.yandex.store";

    private IntentHelper() {
    }

    private static Intent setIntentForStore(Intent intent) {
        // Make sure it DOESN'T open in the stack of appPackageName activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Task reparenting if needed
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        // If the Store was already open in a search result
        // this make sure it still go to the app page you requested
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    static Intent createIntentForAmazonAppstore(Context context, String packageName) {
        Intent intent;
        if (isPackageExists(context, AMAZON_APPSTORE_PACKAGE_NAME)) {
            intent = new Intent(Intent.ACTION_VIEW, getAmazonAppstore(packageName));
            intent = setIntentForStore(intent);
            intent.setPackage(AMAZON_APPSTORE_PACKAGE_NAME);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, getAmazonAppstoreWeb(packageName));
        }
        return intent;
    }

    static Intent createIntentForBlackBerryWorld(Context context, String applicationId) {
        Intent intent;
        if (isPackageExists(context, BLACKBERRY_WORLD_PACKAGE_NAME)) {
            intent = new Intent(Intent.ACTION_VIEW, getBlackBerryWorld(applicationId));
            intent = setIntentForStore(intent);
            intent.setPackage(BLACKBERRY_WORLD_PACKAGE_NAME);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, getBlackBerryWorldWeb(applicationId));
        }
        return intent;
    }

    static Intent createIntentForCafeBazaar(Context context, String packageName) {
        Intent intent;
        if (isPackageExists(context, CAFE_BAZAAR_PACKAGE_NAME)) {
            intent = new Intent(Intent.ACTION_VIEW, getCafeBazaar(packageName));
            intent = setIntentForStore(intent);
            intent.setPackage(CAFE_BAZAAR_PACKAGE_NAME);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, getCafeBazaarWeb(packageName));
        }
        return intent;
    }

    static Intent createIntentForGooglePlay(Context context, String packageName) {
        Intent intent;
        if (isPackageExists(context, GOOGLE_PLAY_PACKAGE_NAME)) {
            intent = new Intent(Intent.ACTION_VIEW, getGooglePlay(packageName));
            intent = setIntentForStore(intent);
            intent.setPackage(GOOGLE_PLAY_PACKAGE_NAME);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, getGooglePlayWeb(packageName));
        }
        return intent;
    }

    static Intent createIntentForMiAppstore(String packageName) {
        return new Intent(Intent.ACTION_VIEW, getMiAppstore(packageName));
    }

    static Intent createIntentForOther(Uri uri) {
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    static Intent createIntentForSamsungGalaxyApps(Context context, String packageName) {
        Intent intent;
        if (isPackageExists(context, SAMSUNG_GALAXY_APPS_PACKAGE_NAME)) {
            intent = new Intent(Intent.ACTION_VIEW, getSamsungGalaxyApps(packageName));
            intent = setIntentForStore(intent);
            intent.setPackage(SAMSUNG_GALAXY_APPS_PACKAGE_NAME);
        } else {
            intent = null;
        }
        return intent;
    }

    static Intent createIntentForSlideME(Context context, String packageName) {
        Intent intent;
        if (isPackageExists(context, SLIDEME_PACKAGE_NAME)) {
            intent = new Intent(Intent.ACTION_VIEW, getSlideME(packageName));
            intent = setIntentForStore(intent);
            intent.setPackage(SLIDEME_PACKAGE_NAME);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, getSlideMEWeb(packageName));
        }
        return intent;
    }

    static Intent createIntentForTencentAppStore(Context context, String packageName) {
        Intent intent;
        if (isPackageExists(context, TENCENT_PACKAGE_NAME)) {
            intent = new Intent(Intent.ACTION_VIEW, getTencentAppStore(packageName));
            intent = setIntentForStore(intent);
            intent.setPackage(TENCENT_PACKAGE_NAME);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, getTencentAppStoreWeb(packageName));
        }
        return intent;
    }

    static Intent createIntentForYandexStore(Context context, String packageName) {
        Intent intent;
        if (isPackageExists(context, YANDEX_STORE_PACKAGE_NAME)) {
            intent = new Intent(Intent.ACTION_VIEW, getYandexStore(packageName));
            intent = setIntentForStore(intent);
            intent.setPackage(YANDEX_STORE_PACKAGE_NAME);
        } else {
            intent = null;
        }
        return intent;
    }

}
