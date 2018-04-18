/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import static com.vorlonsoft.android.rate.AppRate.TAG;
import static com.vorlonsoft.android.rate.UriHelper.getAmazonAppstore;
import static com.vorlonsoft.android.rate.UriHelper.getAmazonAppstoreWeb;
import static com.vorlonsoft.android.rate.UriHelper.getBlackBerryWorld;
import static com.vorlonsoft.android.rate.UriHelper.getBlackBerryWorldWeb;
import static com.vorlonsoft.android.rate.UriHelper.getCafeBazaar;
import static com.vorlonsoft.android.rate.UriHelper.getCafeBazaarWeb;
import static com.vorlonsoft.android.rate.UriHelper.getChineseStores;
import static com.vorlonsoft.android.rate.UriHelper.getGooglePlay;
import static com.vorlonsoft.android.rate.UriHelper.getGooglePlayWeb;
import static com.vorlonsoft.android.rate.UriHelper.getMiAppstore;
import static com.vorlonsoft.android.rate.UriHelper.getSamsungGalaxyApps;
import static com.vorlonsoft.android.rate.UriHelper.getSamsungGalaxyAppsWeb;
import static com.vorlonsoft.android.rate.UriHelper.getSlideME;
import static com.vorlonsoft.android.rate.UriHelper.getSlideMEWeb;
import static com.vorlonsoft.android.rate.UriHelper.getTencentAppStore;
import static com.vorlonsoft.android.rate.UriHelper.getTencentAppStoreWeb;
import static com.vorlonsoft.android.rate.UriHelper.getYandexStore;
import static com.vorlonsoft.android.rate.UriHelper.getYandexStoreWeb;
import static com.vorlonsoft.android.rate.UriHelper.isPackageExists;
import static com.vorlonsoft.android.rate.UriHelper.isPackagesExists;

final class IntentHelper {

    private static final String AMAZON_APPSTORE_PACKAGE_NAME = "com.amazon.venezia";
    private static final String BLACKBERRY_WORLD_PACKAGE_NAME = "net.rim.bb.appworld";
    private static final String CAFE_BAZAAR_PACKAGE_NAME = "com.farsitel.bazaar";
    private static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";
    private static final String SAMSUNG_GALAXY_APPS_PACKAGE_NAME = "com.sec.android.app.samsungapps";
    private static final String SLIDEME_PACKAGE_NAME = "com.slideme.sam.manager";
    private static final String TENCENT_PACKAGE_NAME = "com.tencent.android.qqdownloader";
    private static final String YANDEX_STORE_PACKAGE_NAME = "com.yandex.store";

    private static final String[] CHINESE_STORES_PACKAGE_NAMES = {
            "com.tencent.android.qqdownloader", //腾讯应用宝
            "com.qihoo.appstore", //360手机助手
            "com.xiaomi.market", //小米应用商店
            "com.huawei.appmarket", //华为应用商店
            "com.baidu.appsearch", //百度手机助手
            "com.oppo.market", //OPPO应用商店
            "zte.com.market", //中兴应用商店
            "com.bbk.appstore", //VIVO应用商店
            "com.wandoujia.phoenix2", //豌豆荚
            "com.pp.assistant", //PP手机助手
            "com.hiapk.marketpho", //安智应用商店
            "com.dragon.android.pandaspace", //91手机助手
            "com.yingyonghui.market", //应用汇
            "com.tencent.qqpimsecure", //QQ手机管家
            "com.mappn.gfan", //机锋应用市场
            "cn.goapk.market", //GO市场
            "com.yulong.android.coolmart", //宇龙Coolpad应用商店
            "com.lenovo.leos.appstore", //联想应用商店
            "com.coolapk.market", //cool市场
    };

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

    static Intent[] createIntentsForAmazonAppstore(Context context, String packageName) {
        final Intent[] intents = new Intent[2];
        if (isPackageExists(context, AMAZON_APPSTORE_PACKAGE_NAME)) {
            intents[0] = new Intent(Intent.ACTION_VIEW, getAmazonAppstore(packageName));
            intents[0] = setIntentForStore(intents[0]);
            intents[0].setPackage(AMAZON_APPSTORE_PACKAGE_NAME);
            intents[1] = new Intent(Intent.ACTION_VIEW, getAmazonAppstoreWeb(packageName));
        } else {
            intents[0] = new Intent(Intent.ACTION_VIEW, getAmazonAppstoreWeb(packageName));
        }
        return intents;
    }

    static Intent[] createIntentsForBlackBerryWorld(Context context, String applicationId) {
        final Intent[] intents = new Intent[2];
        if (isPackageExists(context, BLACKBERRY_WORLD_PACKAGE_NAME)) {
            intents[0] = new Intent(Intent.ACTION_VIEW, getBlackBerryWorld(applicationId));
            intents[0] = setIntentForStore(intents[0]);
            intents[0].setPackage(BLACKBERRY_WORLD_PACKAGE_NAME);
            intents[1] = new Intent(Intent.ACTION_VIEW, getBlackBerryWorldWeb(applicationId));
        } else {
            intents[0] = new Intent(Intent.ACTION_VIEW, getBlackBerryWorldWeb(applicationId));
        }
        return intents;
    }

    static Intent[] createIntentsForCafeBazaar(Context context, String packageName) {
        final Intent[] intents = new Intent[2];
        if (isPackageExists(context, CAFE_BAZAAR_PACKAGE_NAME)) {
            intents[0] = new Intent(Intent.ACTION_VIEW, getCafeBazaar(packageName));
            intents[0] = setIntentForStore(intents[0]);
            intents[0].setPackage(CAFE_BAZAAR_PACKAGE_NAME);
            intents[1] = new Intent(Intent.ACTION_VIEW, getCafeBazaarWeb(packageName));
        } else {
            intents[0] = new Intent(Intent.ACTION_VIEW, getCafeBazaarWeb(packageName));
        }
        return intents;
    }

    static Intent[] createIntentsForChineseStores(Context context, String packageName) {
        final String[] storesPackagesNames = isPackagesExists(context, CHINESE_STORES_PACKAGE_NAMES);
        final Intent[] intents;
        final byte storesPackagesNumber = (byte) storesPackagesNames.length;

        if (storesPackagesNumber > 0) {
            intents = new Intent[storesPackagesNumber];
            for (byte i = 0; i < storesPackagesNumber; i++) {
                intents[i] = new Intent(Intent.ACTION_VIEW, getChineseStores(packageName));
                intents[i] = setIntentForStore(intents[i]);
                intents[i].setPackage(storesPackagesNames[i]);
            }
            return intents;
        } else {
            Log.w(TAG, "Failed to rate app, can't find any chinese app stores on the device");
            return null;
        }
    }

    static Intent[] createIntentsForGooglePlay(Context context, String packageName) {
        final Intent[] intents = new Intent[2];
        if (isPackageExists(context, GOOGLE_PLAY_PACKAGE_NAME)) {
            intents[0] = new Intent(Intent.ACTION_VIEW, getGooglePlay(packageName));
            intents[0] = setIntentForStore(intents[0]);
            intents[0].setPackage(GOOGLE_PLAY_PACKAGE_NAME);
            intents[1] = new Intent(Intent.ACTION_VIEW, getGooglePlayWeb(packageName));
        } else {
            intents[0] = new Intent(Intent.ACTION_VIEW, getGooglePlayWeb(packageName));
        }
        return intents;
    }

    static Intent[] createIntentsForMiAppstore(String packageName) {
        return new Intent[]{new Intent(Intent.ACTION_VIEW, getMiAppstore(packageName))};
    }

    static Intent[] createIntentsForOther(Uri uri) {
        return new Intent[]{new Intent(Intent.ACTION_VIEW, uri)};
    }

    static Intent[] createIntentsForSamsungGalaxyApps(Context context, String packageName) {
        final Intent[] intents = new Intent[2];
        if (isPackageExists(context, SAMSUNG_GALAXY_APPS_PACKAGE_NAME)) {
            intents[0] = new Intent(Intent.ACTION_VIEW, getSamsungGalaxyApps(packageName));
            intents[0] = setIntentForStore(intents[0]);
            intents[0].setPackage(SAMSUNG_GALAXY_APPS_PACKAGE_NAME);
            intents[1] = new Intent(Intent.ACTION_VIEW, getSamsungGalaxyAppsWeb(packageName));
        } else {
            Log.w(TAG, "Failed to rate app, com.sec.android.app.samsungapps doesn't exist");
            return null;
        }
        return intents;
    }

    static Intent[] createIntentsForSlideME(Context context, String packageName) {
        final Intent[] intents = new Intent[2];
        if (isPackageExists(context, SLIDEME_PACKAGE_NAME)) {
            intents[0] = new Intent(Intent.ACTION_VIEW, getSlideME(packageName));
            intents[0] = setIntentForStore(intents[0]);
            intents[0].setPackage(SLIDEME_PACKAGE_NAME);
            intents[1] = new Intent(Intent.ACTION_VIEW, getSlideMEWeb(packageName));
        } else {
            intents[0] = new Intent(Intent.ACTION_VIEW, getSlideMEWeb(packageName));
        }
        return intents;
    }

    static Intent[] createIntentsForTencentAppStore(Context context, String packageName) {
        final Intent[] intents = new Intent[2];
        if (isPackageExists(context, TENCENT_PACKAGE_NAME)) {
            intents[0] = new Intent(Intent.ACTION_VIEW, getTencentAppStore(packageName));
            intents[0] = setIntentForStore(intents[0]);
            intents[0].setPackage(TENCENT_PACKAGE_NAME);
            intents[1] = new Intent(Intent.ACTION_VIEW, getTencentAppStoreWeb(packageName));
        } else {
            intents[0] = new Intent(Intent.ACTION_VIEW, getTencentAppStoreWeb(packageName));
        }
        return intents;
    }

    static Intent[] createIntentsForYandexStore(Context context, String packageName) {
        final Intent[] intents = new Intent[2];
        if (isPackageExists(context, YANDEX_STORE_PACKAGE_NAME)) {
            intents[0] = new Intent(Intent.ACTION_VIEW, getYandexStore(packageName));
            intents[0] = setIntentForStore(intents[0]);
            intents[0].setPackage(YANDEX_STORE_PACKAGE_NAME);
            intents[1] = new Intent(Intent.ACTION_VIEW, getYandexStoreWeb(packageName));
        } else {
            Log.w(TAG, "Failed to rate app, com.yandex.store doesn't exist");
            return null;
        }
        return intents;
    }

}
