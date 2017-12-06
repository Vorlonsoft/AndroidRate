/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.content.Context;
import android.view.View;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

final class DialogOptions {

    private boolean showNeutralButton = true;

    private boolean showNegativeButton = true;

    private boolean showTitle = true;

    private boolean cancelable = false;

    private StoreType storeType = StoreType.GOOGLEPLAY;

    private int titleResId = R.string.rate_dialog_title;

    private int messageResId = R.string.rate_dialog_message;

    private int textPositiveResId = R.string.rate_dialog_ok;

    private int textNeutralResId = R.string.rate_dialog_cancel;

    private int textNegativeResId = R.string.rate_dialog_no;

    private String titleText = null;

    private Integer themeResId;

    private String messageText = null;

    private String positiveText = null;

    private String neutralText = null;

    private String negativeText = null;

    private View view;

    private Reference<OnClickButtonListener> listener;

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
    public void setStoreType( StoreType appstore ) {
        storeType = appstore;
    }

    @SuppressWarnings("unused")
    public int getTitleResId() {
        return titleResId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setTitleResId(int titleResId) {
        this.titleResId = titleResId;
    }

    @SuppressWarnings("unused")
    public int getMessageResId() {
        return messageResId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setMessageResId(int messageResId) {
        this.messageResId = messageResId;
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
            return context.getString(titleResId);
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
            return context.getString(messageResId);
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
    public Integer getThemeResId() {
        return themeResId;
    }

    @SuppressWarnings("WeakerAccess")
    public void setThemeResId(Integer themeResId) {
        this.themeResId = themeResId;
    }
}
