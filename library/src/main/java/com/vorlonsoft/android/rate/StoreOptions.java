/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

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

public final class StoreOptions {

    private int storeType = GOOGLEPLAY;

    private String applicationId = null;

    private Intent[] intents = null;

    StoreOptions() {
    }

    String getApplicationId() {
        return applicationId;
    }

    @SuppressWarnings("WeakerAccess")
    void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    Intent[] getIntents() {
        return intents;
    }

    @SuppressWarnings("WeakerAccess")
    void setIntents(Intent[] intents) {
        this.intents = intents;
    }

    int getStoreType() {
        return storeType;
    }

    void setStoreType(final int storeType, final String[] stringParam, final Intent[] intentParaam) {
        this.storeType = storeType;
        switch (storeType) {
            case APPLE:
            case BLACKBERRY:
                setApplicationId(stringParam[0]);
                break;
            case INTENT:
                setIntents(intentParaam);
                break;
            case OTHER:
                final Intent[] intents;
                if (stringParam == null) {
                    intents = null;
                } else {
                    int length = stringParam.length;
                    intents = new Intent[length];
                    for (int i = 0; i < length; i++) {
                        intents[i] = new Intent(Intent.ACTION_VIEW, Uri.parse(stringParam[i]));
                    }
                }
                setIntents(intents);
                break;
            default:
                break;
        }
    }

}