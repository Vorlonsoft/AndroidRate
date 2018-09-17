/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

/**
 * <p>UriHelper Class - uri helper class of the AndroidRate library.</p>
 *
 * @since    0.1.3
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */

final class UriHelper {

    private static final String AMAZON_APPSTORE = "amzn://apps/android?p=";

    private static final String AMAZON_APPSTORE_WEB = "https://www.amazon.com/gp/mas/dl/android?p=";

    private static final String APPLE_APP_STORE_WEB = "https://itunes.apple.com/app/id";

    private static final String BLACKBERRY_WORLD = "appworld://content/";

    private static final String BLACKBERRY_WORLD_WEB = "https://appworld.blackberry.com/webstore/content/";

    private static final String CAFE_BAZAAR = "bazaar://details?id=";

    private static final String CAFE_BAZAAR_WEB = "https://cafebazaar.ir/app/";

    private static final String CHINESE_STORES = "market://details?id=";

    private static final String GOOGLE_PLAY = "market://details?id=";

    private static final String GOOGLE_PLAY_WEB = "https://play.google.com/store/apps/details?id=";

    private static final String MI_APPSTORE = "market://details?id=";

    private static final String MI_APPSTORE_WEB = "http://app.xiaomi.com/details?id=";

    private static final String SAMSUNG_GALAXY_APPS = "samsungapps://ProductDetail/";

    private static final String SAMSUNG_GALAXY_APPS_WEB = "https://apps.samsung.com/appquery/appDetail.as?appId=";

    private static final String SLIDEME_MARKETPLACE = "sam://details?id=";

    private static final String SLIDEME_MARKETPLACE_WEB = "http://slideme.org/app/";

    private static final String TENCENT_APP_STORE = "market://details?id=";

    private static final String TENCENT_APP_STORE_WEB = "http://a.app.qq.com/o/simple.jsp?pkgname=";

    private static final String YANDEX_STORE = "yastore://details?id=";

    private static final String YANDEX_STORE_WEB = "https://store.yandex.com/apps/details?id=";

    private UriHelper() {
        throw new UnsupportedOperationException("UriHelper is a utility class and can't be instantiated!");
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    static Uri getStoreUri(final int appStore, @NonNull final String paramName) {
        if (paramName == null) {
            return null;
        }
        switch (appStore) {
            case AMAZON:
                return Uri.parse(AMAZON_APPSTORE + paramName);
            case APPLE:
                return null;
            case BAZAAR:
                return Uri.parse(CAFE_BAZAAR + paramName);
            case BLACKBERRY:
                return Uri.parse(BLACKBERRY_WORLD + paramName);
            case CHINESESTORES:
                return Uri.parse(CHINESE_STORES + paramName);
            case MI:
                return Uri.parse(MI_APPSTORE + paramName);
            case SAMSUNG:
                return Uri.parse(SAMSUNG_GALAXY_APPS + paramName);
            case SLIDEME:
                return Uri.parse(SLIDEME_MARKETPLACE + paramName);
            case TENCENT:
                return Uri.parse(TENCENT_APP_STORE + paramName);
            case YANDEX:
                return Uri.parse(YANDEX_STORE + paramName);
            default:
                return Uri.parse(GOOGLE_PLAY + paramName);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    static Uri getStoreWebUri(final int appStore, @NonNull final String paramName) {
        if (paramName == null) {
            return null;
        }
        switch (appStore) {
            case AMAZON:
                return Uri.parse(AMAZON_APPSTORE_WEB + paramName);
            case APPLE:
                return Uri.parse(APPLE_APP_STORE_WEB + paramName);
            case BAZAAR:
                return Uri.parse(CAFE_BAZAAR_WEB + paramName);
            case BLACKBERRY:
                return Uri.parse(BLACKBERRY_WORLD_WEB + paramName);
            case CHINESESTORES:
                return null;
            case MI:
                return Uri.parse(MI_APPSTORE_WEB + paramName);
            case SAMSUNG:
                return Uri.parse(SAMSUNG_GALAXY_APPS_WEB + paramName);
            case SLIDEME:
                return Uri.parse(SLIDEME_MARKETPLACE_WEB + paramName);
            case TENCENT:
                return Uri.parse(TENCENT_APP_STORE_WEB + paramName);
            case YANDEX:
                return Uri.parse(YANDEX_STORE_WEB + paramName);
            default:
                return Uri.parse(GOOGLE_PLAY_WEB + paramName);
        }
    }
}