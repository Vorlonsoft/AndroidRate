/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

final class DialogOptions {

    private boolean cancelable = false;

    private boolean showNegativeButton = true;

    private boolean showNeutralButton = true;

    private boolean showTitle = true;

    private int textMessageResId = R.string.rate_dialog_message;

    private int textNegativeResId = R.string.rate_dialog_no;

    private int textNeutralResId = R.string.rate_dialog_cancel;

    private int textPositiveResId = R.string.rate_dialog_ok;

    private int textTitleResId = R.string.rate_dialog_title;

    private int themeResId = 0;

    private String blackBerryWorldApplicationId = null;

    private String messageText = null;

    private String negativeText = null;

    private String neutralText = null;

    private String positiveText = null;

    private String titleText = null;

    private StoreType storeType = StoreType.GOOGLEPLAY;

    private Uri otherStoreUri = null;

    private View view = null;

    private Reference<OnClickButtonListener> listener = null;

    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowNeutralButton() {
        return showNeutralButton;
    }

    @SuppressWarnings("WeakerAccess")
    public void setShowNeutralButton(boolean showNeutralButton) {
        this.showNeutralButton = showNeutralButton;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowNegativeButton() {
        return showNegativeButton;
    }

    @SuppressWarnings("WeakerAccess")
    public void setShowNegativeButton(boolean showNegativeButton) {
        this.showNegativeButton = showNegativeButton;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowTitle() {
        return showTitle;
    }

    @SuppressWarnings("WeakerAccess")
    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean getCancelable() {
        return cancelable;
    }

    @SuppressWarnings("WeakerAccess")
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    @SuppressWarnings("WeakerAccess")
    public StoreType getStoreType() {
        return storeType;
    }

    @SuppressWarnings("WeakerAccess")
    public String getBlackBerryWorldApplicationId() {
        return blackBerryWorldApplicationId;
    }

    @SuppressWarnings("WeakerAccess")
    public Uri getOtherStoreUri() {
        return otherStoreUri;
    }

    @SuppressWarnings("WeakerAccess, unused")
    public void setStoreType(StoreType appStore) {
        setStoreType(appStore, null);
    }

    @SuppressWarnings("WeakerAccess")
    public void setStoreType(StoreType appStore, String param) {
        storeType = appStore;
        if (appStore == StoreType.BLACKBERRY) {
            if (param == null) {
                throw new IllegalArgumentException("For StoreType.BLACKBERRY you must use setStoreType(StoreType appStore, String applicationId) and 'applicationId' must be != null");
            } else {
                blackBerryWorldApplicationId = param;
            }
        } else if (appStore == StoreType.OTHER) {
            if (param == null) {
                throw new IllegalArgumentException("For StoreType.OTHER you must use setStoreType(StoreType appStore, String uri) and 'uri' must be != null");
            } else {
                otherStoreUri = Uri.parse(param);
            }
        }
    }

    @SuppressWarnings("unused")
    public int getTitleResId() {
        return textTitleResId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setTitleResId(int textTitleResId) {
        this.textTitleResId = textTitleResId;
    }

    @SuppressWarnings("unused")
    public int getMessageResId() {
        return textMessageResId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setMessageResId(int textMessageResId) {
        this.textMessageResId = textMessageResId;
    }

    @SuppressWarnings("unused")
    public int getTextPositiveResId() {
        return textPositiveResId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setTextPositiveResId(int textPositiveResId) {
        this.textPositiveResId = textPositiveResId;
    }

    @SuppressWarnings("unused")
    public int getTextNeutralResId() {
        return textNeutralResId;
    }
    @SuppressWarnings("WeakerAccess")
    public void setTextNeutralResId(int textNeutralResId) {
        this.textNeutralResId = textNeutralResId;
    }

    @SuppressWarnings("unused")
    public int getTextNegativeResId() {
        return textNegativeResId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setTextNegativeResId(int textNegativeResId) {
        this.textNegativeResId = textNegativeResId;
    }

    @SuppressWarnings("WeakerAccess")
    public View getView() {
        return view;
    }

    @SuppressWarnings("WeakerAccess")
    public void setView(View view) {
        this.view = view;
    }

    @SuppressWarnings("WeakerAccess")
    public OnClickButtonListener getListener() {
        return listener != null ? listener.get() : null;
    }

    @SuppressWarnings("WeakerAccess")
    public void setListener(OnClickButtonListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    @SuppressWarnings("WeakerAccess")
    public String getTitleText(Context context) {
        if (titleText == null) {
            return context.getString(textTitleResId);
        }
        return titleText;
    }

    @SuppressWarnings("WeakerAccess")
    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getMessageText(Context context) {
        if (messageText == null) {
            return context.getString(textMessageResId);
        }
        return messageText;
    }

    @SuppressWarnings("WeakerAccess")
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getPositiveText(Context context) {
        if (positiveText == null) {
            return context.getString(textPositiveResId);
        }
        return positiveText;
    }

    @SuppressWarnings("WeakerAccess")
    public void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getNeutralText(Context context) {
        if (neutralText == null) {
            return context.getString(textNeutralResId);
        }
        return neutralText;
    }

    @SuppressWarnings("WeakerAccess")
    public void setNeutralText(String neutralText) {
        this.neutralText = neutralText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getNegativeText(Context context) {
        if (negativeText == null) {
            return context.getString(textNegativeResId);
        }
        return negativeText;
    }

    @SuppressWarnings("WeakerAccess")
    public void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
    }

    @SuppressWarnings("WeakerAccess")
    public int getThemeResId() {
        return themeResId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setThemeResId(int themeResId) {
        this.themeResId = themeResId;
    }
}
