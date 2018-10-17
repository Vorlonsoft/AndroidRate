/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.net.Uri

import com.vorlonsoft.android.rate.StoreType.Companion.AMAZON
import com.vorlonsoft.android.rate.StoreType.Companion.APPLE
import com.vorlonsoft.android.rate.StoreType.Companion.BAZAAR
import com.vorlonsoft.android.rate.StoreType.Companion.BLACKBERRY
import com.vorlonsoft.android.rate.StoreType.Companion.CHINESESTORES
import com.vorlonsoft.android.rate.StoreType.Companion.MI
import com.vorlonsoft.android.rate.StoreType.Companion.SAMSUNG
import com.vorlonsoft.android.rate.StoreType.Companion.SLIDEME
import com.vorlonsoft.android.rate.StoreType.Companion.TENCENT
import com.vorlonsoft.android.rate.StoreType.Companion.YANDEX

/**
 * UriHelper Object - the uri helper object of the AndroidRate library.
 *
 * Contains stores URIs constants and [formUri], [getStoreUri], [getStoreWebUri] functions.
 *
 * @since    0.1.3
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */
internal object UriHelper {
    /** Default store URI. */
    private const val DEFAULT_STORE_URI = "market://details?id="
    /** Amazon Appstore URI. */
    private const val AMAZON_APPSTORE = "amzn://apps/android?p="
    /** Amazon Appstore web URI. */
    private const val AMAZON_APPSTORE_WEB = "https://www.amazon.com/gp/mas/dl/android?p="
    /** Apple App Store web URI. */
    private const val APPLE_APP_STORE_WEB = "https://itunes.apple.com/app/id"
    /** BlackBerry World URI. */
    private const val BLACKBERRY_WORLD = "appworld://content/"
    /** BlackBerry World web URI. */
    private const val BLACKBERRY_WORLD_WEB = "https://appworld.blackberry.com/webstore/content/"
    /** Cafe Bazaar URI. */
    private const val CAFE_BAZAAR = "bazaar://details?id="
    /** Cafe Bazaar web URI. */
    private const val CAFE_BAZAAR_WEB = "https://cafebazaar.ir/app/"
    /** 19 chinese app stores URI. */
    private const val CHINESE_STORES = DEFAULT_STORE_URI
    /** Google Play URI. */
    private const val GOOGLE_PLAY = DEFAULT_STORE_URI
    /** Google Play web URI. */
    private const val GOOGLE_PLAY_WEB = "https://play.google.com/store/apps/details?id="
    /** Mi Appstore (Xiaomi Market) URI. */
    private const val MI_APPSTORE = DEFAULT_STORE_URI
    /** Mi Appstore (Xiaomi Market) web URI. */
    private const val MI_APPSTORE_WEB = "http://app.xiaomi.com/details?id="
    /** Samsung Galaxy Apps URI. */
    private const val SAMSUNG_GALAXY_APPS = "samsungapps://ProductDetail/"
    /** Samsung Galaxy Apps web URI. */
    private const val SAMSUNG_GALAXY_APPS_WEB = "https://apps.samsung.com/appquery/appDetail.as?appId="
    /** SlideME Marketplace URI. */
    private const val SLIDEME_MARKETPLACE = "sam://details?id="
    /** SlideME Marketplace web URI. */
    private const val SLIDEME_MARKETPLACE_WEB = "http://slideme.org/app/"
    /** Tencent App Store URI. */
    private const val TENCENT_APP_STORE = DEFAULT_STORE_URI
    /** Tencent App Store web URI. */
    private const val TENCENT_APP_STORE_WEB = "http://a.app.qq.com/o/simple.jsp?pkgname="
    /** Yandex.Store URI. */
    private const val YANDEX_STORE = "yastore://details?id="
    /** Yandex.Store web URI. */
    private const val YANDEX_STORE_WEB = "https://store.yandex.com/apps/details?id="

    /**
     * Creates an URI which parses the given encoded URI strings.
     *
     * @param part1 part1 of an RFC 2396-compliant, encoded URI
     * @param part2 part2 of an RFC 2396-compliant, encoded URI
     * @return the URI for this given URI strings
     */
    private fun formUri(part1: String, part2: String): Uri {
        return Uri.parse(part1 + part2)
    }

    /**
     * Returns the store URI for given [StoreType.StoreWithApplicationId] or
     * [StoreType.StoreWithoutApplicationId] and String params.
     *
     * @param appStore a [StoreType.StoreWithApplicationId] or [StoreType.StoreWithoutApplicationId]
     * param
     * @param paramName a String param
     * @return the store URI for given params
     */
    @JvmStatic
    fun getStoreUri(appStore: Int, paramName: String): Uri? {
        return when (appStore) {
            AMAZON -> formUri(AMAZON_APPSTORE, paramName)
            APPLE -> null
            BAZAAR -> formUri(CAFE_BAZAAR, paramName)
            BLACKBERRY -> formUri(BLACKBERRY_WORLD, paramName)
            CHINESESTORES -> formUri(CHINESE_STORES, paramName)
            MI -> formUri(MI_APPSTORE, paramName)
            SAMSUNG -> formUri(SAMSUNG_GALAXY_APPS, paramName)
            SLIDEME -> formUri(SLIDEME_MARKETPLACE, paramName)
            TENCENT -> formUri(TENCENT_APP_STORE, paramName)
            YANDEX -> formUri(YANDEX_STORE, paramName)
            else -> formUri(GOOGLE_PLAY, paramName)
        }
    }

    /**
     * Returns the store URI for given [StoreType.StoreWithApplicationId] or
     * [StoreType.StoreWithoutApplicationId] and String params.
     *
     * @param appStore a [StoreType.StoreWithApplicationId] or [StoreType.StoreWithoutApplicationId]
     * param
     * @param paramName a String param
     * @return the store URI for given params
     */
    @JvmStatic
    fun getStoreWebUri(appStore: Int, paramName: String): Uri? {
        return when (appStore) {
            AMAZON -> formUri(AMAZON_APPSTORE_WEB, paramName)
            APPLE -> formUri(APPLE_APP_STORE_WEB, paramName)
            BAZAAR -> formUri(CAFE_BAZAAR_WEB, paramName)
            BLACKBERRY -> formUri(BLACKBERRY_WORLD_WEB, paramName)
            CHINESESTORES -> null
            MI -> formUri(MI_APPSTORE_WEB, paramName)
            SAMSUNG -> formUri(SAMSUNG_GALAXY_APPS_WEB, paramName)
            SLIDEME -> formUri(SLIDEME_MARKETPLACE_WEB, paramName)
            TENCENT -> formUri(TENCENT_APP_STORE_WEB, paramName)
            YANDEX -> formUri(YANDEX_STORE_WEB, paramName)
            else -> formUri(GOOGLE_PLAY_WEB, paramName)
        }
    }
}