/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.vorlonsoft.android.rate.Constants.Utils.UTILITY_CLASS_MESSAGE;
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

    private static final String DEFAULT_STORE_URI = "market://details?id=";

    private static final String AMAZON_APPSTORE = "amzn://apps/android?p=";

    private static final String AMAZON_APPSTORE_WEB = "https://www.amazon.com/gp/mas/dl/android?p=";

    private static final String APPLE_APP_STORE_WEB = "https://itunes.apple.com/app/id";

    private static final String BLACKBERRY_WORLD = "appworld://content/";

    private static final String BLACKBERRY_WORLD_WEB = "https://appworld.blackberry.com/webstore/content/";

    private static final String CAFE_BAZAAR = "bazaar://details?id=";

    private static final String CAFE_BAZAAR_WEB = "https://cafebazaar.ir/app/";

    private static final String CHINESE_STORES = DEFAULT_STORE_URI;

    private static final String GOOGLE_PLAY = DEFAULT_STORE_URI;

    private static final String GOOGLE_PLAY_WEB = "https://play.google.com/store/apps/details?id=";

    private static final String MI_APPSTORE = DEFAULT_STORE_URI;

    private static final String MI_APPSTORE_WEB = "http://app.xiaomi.com/details?id=";

    private static final String SAMSUNG_GALAXY_APPS = "samsungapps://ProductDetail/";

    private static final String SAMSUNG_GALAXY_APPS_WEB = "https://apps.samsung.com/appquery/appDetail.as?appId=";

    private static final String SLIDEME_MARKETPLACE = "sam://details?id=";

    private static final String SLIDEME_MARKETPLACE_WEB = "http://slideme.org/app/";

    private static final String TENCENT_APP_STORE = DEFAULT_STORE_URI;

    private static final String TENCENT_APP_STORE_WEB = "http://a.app.qq.com/o/simple.jsp?pkgname=";

    private static final String YANDEX_STORE = "yastore://details?id=";

    private static final String YANDEX_STORE_WEB = "https://store.yandex.com/apps/details?id=";

    private UriHelper() {
        throw new UnsupportedOperationException("UriHelper" + UTILITY_CLASS_MESSAGE);
    }

    /**
     * <p>Creates a Uri which parses the given strings.</p>
     *
     * @param part1 part1 of an RFC 2396-compliant, encoded URI
     * @param part2 part2 of an RFC 2396-compliant, encoded URI
     * @return Uri for this given uri strings
     */
    @NonNull
    private static Uri formUri(@NonNull final String part1, @NonNull final String part2) {
        return Uri.parse(part1 + part2);
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    static Uri getStoreUri(final int appStore, @NonNull final String paramName) {
        if (paramName == null) {
            return null;
        }
        switch (appStore) {
            case AMAZON:
                return formUri(AMAZON_APPSTORE, paramName);
            case APPLE:
                return null;
            case BAZAAR:
                return formUri(CAFE_BAZAAR, paramName);
            case BLACKBERRY:
                return formUri(BLACKBERRY_WORLD, paramName);
            case CHINESESTORES:
                return formUri(CHINESE_STORES, paramName);
            case MI:
                return formUri(MI_APPSTORE, paramName);
            case SAMSUNG:
                return formUri(SAMSUNG_GALAXY_APPS, paramName);
            case SLIDEME:
                return formUri(SLIDEME_MARKETPLACE, paramName);
            case TENCENT:
                return formUri(TENCENT_APP_STORE, paramName);
            case YANDEX:
                return formUri(YANDEX_STORE, paramName);
            default:
                return formUri(GOOGLE_PLAY, paramName);
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
                return formUri(AMAZON_APPSTORE_WEB, paramName);
            case APPLE:
                return formUri(APPLE_APP_STORE_WEB, paramName);
            case BAZAAR:
                return formUri(CAFE_BAZAAR_WEB, paramName);
            case BLACKBERRY:
                return formUri(BLACKBERRY_WORLD_WEB, paramName);
            case CHINESESTORES:
                return null;
            case MI:
                return formUri(MI_APPSTORE_WEB, paramName);
            case SAMSUNG:
                return formUri(SAMSUNG_GALAXY_APPS_WEB, paramName);
            case SLIDEME:
                return formUri(SLIDEME_MARKETPLACE_WEB, paramName);
            case TENCENT:
                return formUri(TENCENT_APP_STORE_WEB, paramName);
            case YANDEX:
                return formUri(YANDEX_STORE_WEB, paramName);
            default:
                return formUri(GOOGLE_PLAY_WEB, paramName);
        }
    }
}