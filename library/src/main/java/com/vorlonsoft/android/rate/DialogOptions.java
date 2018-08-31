/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.content.Context;
import android.view.View;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;

/**
 * <p>DialogOptions Class - dialog options class of the AndroidRate library.</p>
 *
 * @since    0.5.1
 * @version  1.2.0
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */

public final class DialogOptions {

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

    private String messageText = null;

    private String negativeText = null;

    private String neutralText = null;

    private String positiveText = null;

    private String titleText = null;

    private View view = null;

    private Reference<OnClickButtonListener> listener = null;

    DialogOptions() {
    }

    boolean shouldShowNeutralButton() {
        return showNeutralButton;
    }

    void setShowNeutralButton(boolean showNeutralButton) {
        this.showNeutralButton = showNeutralButton;
    }

    boolean shouldShowNegativeButton() {
        return showNegativeButton;
    }

    void setShowNegativeButton(boolean showNegativeButton) {
        this.showNegativeButton = showNegativeButton;
    }

    boolean shouldShowTitle() {
        return showTitle;
    }

    void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    boolean getCancelable() {
        return cancelable;
    }

    void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    @SuppressWarnings("unused")
    int getTitleResId() {
        return textTitleResId;
    }

    void setTitleResId(int textTitleResId) {
        this.textTitleResId = textTitleResId;
    }

    @SuppressWarnings("unused")
    int getMessageResId() {
        return textMessageResId;
    }

    void setMessageResId(int textMessageResId) {
        this.textMessageResId = textMessageResId;
    }

    @SuppressWarnings("unused")
    int getTextPositiveResId() {
        return textPositiveResId;
    }

    void setTextPositiveResId(int textPositiveResId) {
        this.textPositiveResId = textPositiveResId;
    }

    @SuppressWarnings("unused")
    int getTextNeutralResId() {
        return textNeutralResId;
    }

    void setTextNeutralResId(int textNeutralResId) {
        this.textNeutralResId = textNeutralResId;
    }

    @SuppressWarnings("unused")
    int getTextNegativeResId() {
        return textNegativeResId;
    }

    void setTextNegativeResId(int textNegativeResId) {
        this.textNegativeResId = textNegativeResId;
    }

    View getView() {
        return view;
    }

    void setView(View view) {
        this.view = view;
    }

    @Nullable
    OnClickButtonListener getListener() {
        return listener != null ? listener.get() : null;
    }

    void setListener(OnClickButtonListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    String getTitleText(Context context) {
        if (titleText == null) {
            return context.getString(textTitleResId);
        }
        return titleText;
    }

    void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    String getMessageText(Context context) {
        if (messageText == null) {
            return context.getString(textMessageResId);
        }
        return messageText;
    }

    void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    String getPositiveText(Context context) {
        if (positiveText == null) {
            return context.getString(textPositiveResId);
        }
        return positiveText;
    }

    void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
    }

    String getNeutralText(Context context) {
        if (neutralText == null) {
            return context.getString(textNeutralResId);
        }
        return neutralText;
    }

    void setNeutralText(String neutralText) {
        this.neutralText = neutralText;
    }

    String getNegativeText(Context context) {
        if (negativeText == null) {
            return context.getString(textNegativeResId);
        }
        return negativeText;
    }

    void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
    }

    int getThemeResId() {
        return themeResId;
    }

    void setThemeResId(int themeResId) {
        this.themeResId = themeResId;
    }
}
