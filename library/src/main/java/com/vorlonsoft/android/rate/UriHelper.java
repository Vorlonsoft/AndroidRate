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
        throw new AssertionError();
    }

    @Nullable
    private static Uri getStoreUri(final int appStore, final String paramName, final boolean isWebUri) {
        final String baseStoreUri;
        if (isWebUri) {
            switch (appStore) {
                case AMAZON:
                    baseStoreUri = AMAZON_APPSTORE_WEB;
                    break;
                case APPLE:
                    baseStoreUri = APPLE_APP_STORE_WEB;
                    break;
                case BAZAAR:
                    baseStoreUri = CAFE_BAZAAR_WEB;
                    break;
                case BLACKBERRY:
                    baseStoreUri = BLACKBERRY_WORLD_WEB;
                    break;
                case CHINESESTORES:
                    return null;
                case MI:
                    baseStoreUri = MI_APPSTORE_WEB;
                    break;
                case SAMSUNG:
                    baseStoreUri = SAMSUNG_GALAXY_APPS_WEB;
                    break;
                case SLIDEME:
                    baseStoreUri = SLIDEME_MARKETPLACE_WEB;
                    break;
                case TENCENT:
                    baseStoreUri = TENCENT_APP_STORE_WEB;
                    break;
                case YANDEX:
                    baseStoreUri = YANDEX_STORE_WEB;
                    break;
                default:
                    baseStoreUri = GOOGLE_PLAY_WEB;
            }
        } else {
            switch (appStore) {
                case AMAZON:
                    baseStoreUri = AMAZON_APPSTORE;
                    break;
                case APPLE:
                    return null;
                case BAZAAR:
                    baseStoreUri = CAFE_BAZAAR;
                    break;
                case BLACKBERRY:
                    baseStoreUri = BLACKBERRY_WORLD;
                    break;
                case CHINESESTORES:
                    baseStoreUri = CHINESE_STORES;
                    break;
                case MI:
                    baseStoreUri = MI_APPSTORE;
                    break;
                case SAMSUNG:
                    baseStoreUri = SAMSUNG_GALAXY_APPS;
                    break;
                case SLIDEME:
                    baseStoreUri = SLIDEME_MARKETPLACE;
                    break;
                case TENCENT:
                    baseStoreUri = TENCENT_APP_STORE;
                    break;
                case YANDEX:
                    baseStoreUri = YANDEX_STORE;
                    break;
                default:
                    baseStoreUri = GOOGLE_PLAY;
            }
        }
        return Uri.parse(baseStoreUri + paramName);
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    static Uri getStoreUri(final int appStore, @NonNull final String paramName) {
        return paramName == null ? null : getStoreUri(appStore, paramName, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    static Uri getStoreWebUri(final int appStore, @NonNull final String paramName) {
        return paramName == null ? null : getStoreUri(appStore, paramName, true);
    }
}