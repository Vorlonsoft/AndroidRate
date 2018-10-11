/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;

import static com.vorlonsoft.android.rate.StoreType.APPLE;
import static com.vorlonsoft.android.rate.StoreType.BLACKBERRY;
import static com.vorlonsoft.android.rate.StoreType.GOOGLEPLAY;
import static com.vorlonsoft.android.rate.StoreType.INTENT;
import static com.vorlonsoft.android.rate.StoreType.OTHER;

/**
 * <p>StoreOptions Class - store options class of the AndroidRate library.</p>
 *
 * @since    1.1.7
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */

@SuppressWarnings("WeakerAccess")
public final class StoreOptions {
    /** <p>One of the app stores defined by {@link StoreType.AnyStoreType}.</p> */
    @StoreType.AnyStoreType
    private int storeType = GOOGLEPLAY;

    private String applicationId = null;

    private Intent[] intents = null;

    StoreOptions() {
    }

    String getApplicationId() {
        return applicationId;
    }

    private void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    Intent[] getIntents() {
        return intents;
    }

    private void setIntents(Intent[] intents) {
        this.intents = intents;
    }

    /**
     * <p>Gets the app store type from library options.</p>
     * <p>NOTE: this method doesn't get an app store type from user's device.</p>
     *
     * @return one of the values defined by {@link StoreType.AnyStoreType}
     */
    @StoreType.AnyStoreType
    int getStoreType() {
        return storeType;
    }

    /**
     * <p>Sets one of the app stores defined by {@link StoreType.AnyStoreType} to the
     * Positive button.</p>
     *
     * @param storeType one of the values defined by {@link StoreType.AnyStoreType}
     * @param stringParam array of string params
     * @param intentParam array of intent params
     * @see AppRate#setStoreType(int)
     * @see AppRate#setStoreType(int,long)
     * @see AppRate#setStoreType(String...)
     * @see AppRate#setStoreType(Intent...)
     */
    @SuppressLint("SwitchIntDef")
    void setStoreType(@StoreType.AnyStoreType final int storeType, final String[] stringParam, final Intent[] intentParam) {
        this.storeType = storeType;
        switch (storeType) {
            case APPLE:
            case BLACKBERRY:
                setApplicationId(stringParam[0]);
                break;
            case INTENT:
                setIntents(intentParam);
                break;
            case OTHER:
                final Intent[] otherIntents;
                if (stringParam == null) {
                    otherIntents = null;
                } else {
                    final int length = stringParam.length;
                    otherIntents = new Intent[length];
                    for (int i = 0; i < length; i++) {
                        otherIntents[i] = new Intent(Intent.ACTION_VIEW, Uri.parse(stringParam[i]));
                    }
                }
                setIntents(otherIntents);
                break;
            default:
                break;
        }
    }
}