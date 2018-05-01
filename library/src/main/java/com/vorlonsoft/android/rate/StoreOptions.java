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

final class StoreOptions {

    private int storeType = GOOGLEPLAY;

    private String applicationId = null;

    private Uri otherStoreUri = null;

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

    Uri getOtherStoreUri() {
        return otherStoreUri;
    }

    @SuppressWarnings("WeakerAccess")
    void setOtherStoreUri(Uri otherStoreUri) {
        this.otherStoreUri = otherStoreUri;
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

    void setStoreType(final int storeType, final String stringParam, final Intent[] intentParaam) {
        this.storeType = storeType;
        switch(storeType) {
            case APPLE:
            case BLACKBERRY:
                setApplicationId(stringParam);
                break;
            case INTENT:
                setIntents(intentParaam);
                break;
            case OTHER:
                setOtherStoreUri(stringParam == null ? null : Uri.parse(stringParam));
        }
    }

}