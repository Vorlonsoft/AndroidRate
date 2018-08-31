/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.vorlonsoft.android.rate.Constants.Utils.TAG;
import static com.vorlonsoft.android.rate.StoreType.AMAZON;
import static com.vorlonsoft.android.rate.StoreType.APPLE;
import static com.vorlonsoft.android.rate.StoreType.BAZAAR;
import static com.vorlonsoft.android.rate.StoreType.BLACKBERRY;
import static com.vorlonsoft.android.rate.StoreType.CHINESESTORES;
import static com.vorlonsoft.android.rate.StoreType.MI;
import static com.vorlonsoft.android.rate.StoreType.SAMSUNG;
import static com.vorlonsoft.android.rate.StoreType.SLIDEME;
import static com.vorlonsoft.android.rate.StoreType.TENCENT;
import static com.vorlonsoft.android.rate.StoreType.YANDEX;
import static com.vorlonsoft.android.rate.UriHelper.getStoreUri;
import static com.vorlonsoft.android.rate.UriHelper.getStoreWebUri;
import static com.vorlonsoft.android.rate.Utils.isPackagesExists;

/**
 * <p>IntentHelper Class - intent helper class of the AndroidRate library.</p>
 *
 * @since    0.5.0
 * @version  1.2.0
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */

final class IntentHelper {

    private static final String AMAZON_APPSTORE_PACKAGE_NAME = "com.amazon.venezia";

    private static final String BLACKBERRY_WORLD_PACKAGE_NAME = "net.rim.bb.appworld";

    private static final String CAFE_BAZAAR_PACKAGE_NAME = "com.farsitel.bazaar";

    private static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";

    private static final String MI_PACKAGE_NAME = "com.xiaomi.market";

    private static final String SAMSUNG_GALAXY_APPS_PACKAGE_NAME = "com.sec.android.app.samsungapps";

    private static final String SLIDEME_MARKETPLACE_PACKAGE_NAME = "com.slideme.sam.manager";

    private static final String TENCENT_PACKAGE_NAME = "com.tencent.android.qqdownloader";

    private static final String YANDEX_STORE_PACKAGE_NAME = "com.yandex.store";

    private static final String[] BROWSERS_PACKAGES_NAMES = {
            "com.android.chrome",
            "org.mozilla.firefox",
            "com.opera.browser",
            "com.opera.mini.native",
            "com.sec.android.app.sbrowser",
            "com.UCMobile.intl",
            "com.tencent.mtt",
            "com.android.browser"
    };

    private static final String[] CHINESE_STORES_PACKAGES_NAMES = {
            "com.tencent.android.qqdownloader", //腾讯应用宝
            "com.qihoo.appstore",               //360手机助手
            "com.xiaomi.market",                //小米应用商店
            "com.huawei.appmarket",             //华为应用商店
            "com.baidu.appsearch",              //百度手机助手
            "com.oppo.market",                  //OPPO应用商店
            "zte.com.market",                   //中兴应用商店
            "com.bbk.appstore",                 //VIVO应用商店
            "com.wandoujia.phoenix2",           //豌豆荚
            "com.pp.assistant",                 //PP手机助手
            "com.hiapk.marketpho",              //安智应用商店
            "com.dragon.android.pandaspace",    //91手机助手
            "com.yingyonghui.market",           //应用汇
            "com.tencent.qqpimsecure",          //QQ手机管家
            "com.mappn.gfan",                   //机锋应用市场
            "cn.goapk.market",                  //GO市场
            "com.yulong.android.coolmart",      //宇龙Coolpad应用商店
            "com.lenovo.leos.appstore",         //联想应用商店
            "com.coolapk.market"                //cool市场
    };

    private IntentHelper() {
        throw new AssertionError();
    }

    private static void setIntentForStore(final Intent intent) {
        // Make sure it DOESN'T open in the stack of appPackageName activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Task reparenting if needed
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        // If the Store was already open in a search result
        // this make sure it still go to the app page you requested
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Nullable
    static Intent[] createIntentsForStore(@NonNull final Context context, final int appStore, @NonNull final String paramName) {

        //noinspection ConstantConditions
        if ((context == null) || (paramName == null)) {
            return null;
        }

        final byte deviceStoresPackagesNumber;
        final String[] storesPackagesNames;
        final String[] deviceStoresPackagesNames;
        final Intent[] intents;

        boolean needStorePackage = false;
        boolean hasWebUriIntent = true;

        switch (appStore) {
            case AMAZON:
                storesPackagesNames = new String[]{AMAZON_APPSTORE_PACKAGE_NAME};
                break;
            case APPLE:
                storesPackagesNames = null;
                break;
            case BAZAAR:
                storesPackagesNames = new String[]{CAFE_BAZAAR_PACKAGE_NAME};
                break;
            case BLACKBERRY:
                storesPackagesNames = new String[]{BLACKBERRY_WORLD_PACKAGE_NAME};
                break;
            case CHINESESTORES:
                storesPackagesNames = CHINESE_STORES_PACKAGES_NAMES;
                needStorePackage = true;
                hasWebUriIntent = false;
                break;
            case MI:
                storesPackagesNames = new String[]{MI_PACKAGE_NAME};
                break;
            case SAMSUNG:
                storesPackagesNames = new String[]{SAMSUNG_GALAXY_APPS_PACKAGE_NAME};
                needStorePackage = true;
                break;
            case SLIDEME:
                storesPackagesNames = new String[]{SLIDEME_MARKETPLACE_PACKAGE_NAME};
                break;
            case TENCENT:
                storesPackagesNames = new String[]{TENCENT_PACKAGE_NAME};
                break;
            case YANDEX:
                storesPackagesNames = new String[]{YANDEX_STORE_PACKAGE_NAME};
                needStorePackage = true;
                break;
            default:
                storesPackagesNames = new String[]{GOOGLE_PLAY_PACKAGE_NAME};
        }

        deviceStoresPackagesNames = storesPackagesNames == null ? null : isPackagesExists(context, storesPackagesNames);
        deviceStoresPackagesNumber = deviceStoresPackagesNames == null ? 0 : (byte) deviceStoresPackagesNames.length;

        if (deviceStoresPackagesNumber > 0) {
            intents = hasWebUriIntent ? new Intent[deviceStoresPackagesNumber + 1] : new Intent[deviceStoresPackagesNumber];
            for (byte b = 0; b < deviceStoresPackagesNumber; b++) {
                intents[b] = new Intent(Intent.ACTION_VIEW, getStoreUri(appStore, paramName));
                setIntentForStore(intents[b]);
                intents[b].setPackage(deviceStoresPackagesNames[b]);
            }
            if (hasWebUriIntent) {
                intents[deviceStoresPackagesNumber] = new Intent(Intent.ACTION_VIEW, getStoreWebUri(appStore, paramName));
            }
        } else {
            if (!needStorePackage) {
                intents = new Intent[]{new Intent(Intent.ACTION_VIEW, getStoreWebUri(appStore, paramName))};
                if (appStore == APPLE) {
                    final String[] deviceBrowsersPackagesNames = isPackagesExists(context, BROWSERS_PACKAGES_NAMES);
                    if ((deviceBrowsersPackagesNames != null) && (deviceBrowsersPackagesNames.length > 0)) {
                        intents[0].setPackage(deviceBrowsersPackagesNames[0]);
                    }
                }
            } else {
                if (hasWebUriIntent) {
                    Log.w(TAG, "Failed to rate app, " + Arrays.toString(storesPackagesNames) + " not exist on device and device can't start app store web (http/https) uri activity without it");
                } else {
                    Log.w(TAG, "Failed to rate app, " + Arrays.toString(storesPackagesNames) + " not exist on device and app store (" + appStore + ") hasn't web (http/https) uri");
                }
                return null;
            }
        }
        return intents;
    }
}
