/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;

import java.lang.ref.SoftReference;

import androidx.annotation.Nullable;

import static com.vorlonsoft.android.rate.Constants.Utils.TAG;

/**
 * <p>DialogOptions Class - dialog options class of the AndroidRate library.</p>
 *
 * @since    0.5.1
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */

public final class DialogOptions {

    private boolean cancelable = false;

    private boolean showMessage = true;

    private boolean showNegativeButton = true;

    private boolean showNeutralButton = true;

    private boolean showTitle = true;
    /** Default value is null. */
    private Boolean showDialogIcon = null;

    private int dialogType = DialogType.CLASSIC;

    private int dialogIconResId = 0;

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

    private Drawable dialogIcon = null;

    private View view = null;

    private SoftReference<OnClickButtonListener> listener = null;

    DialogOptions() {
    }

    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowMessage() {
        return showMessage;
    }

    void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowNeutralButton() {
        return showNeutralButton;
    }

    void setShowNeutralButton(boolean showNeutralButton) {
        this.showNeutralButton = showNeutralButton;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowNegativeButton() {
        return showNegativeButton;
    }

    void setShowNegativeButton(boolean showNegativeButton) {
        this.showNegativeButton = showNegativeButton;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowTitle() {
        return showTitle;
    }

    void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public boolean shouldShowDialogIcon() {
        return (showDialogIcon == null) ? ((dialogType == DialogType.APPLE) ||
                                           (dialogType == DialogType.MODERN)) : showDialogIcon;
    }

    void setShowDialogIcon(boolean showDialogIcon) {
        this.showDialogIcon = showDialogIcon;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean getCancelable() {
        return cancelable;
    }

    void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    @SuppressWarnings("unused")
    int getDialogIconResId() {
        return dialogIconResId;
    }

    void setDialogIconResId(int dialogIconResId) {
        this.dialogIconResId = dialogIconResId;
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

    public View getView() {
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
        this.listener = new SoftReference<>(listener);
    }

    @SuppressWarnings("WeakerAccess")
    public String getTitleText(Context context) {
        if (titleText == null) {
            return context.getString(textTitleResId);
        }
        return titleText;
    }

    void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getMessageText(Context context) {
        if (messageText == null) {
            return context.getString(textMessageResId);
        }
        return messageText;
    }

    void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getPositiveText(Context context) {
        if (positiveText == null) {
            return context.getString(textPositiveResId);
        }
        return positiveText;
    }

    void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getNeutralText(Context context) {
        if (neutralText == null) {
            return context.getString(textNeutralResId);
        }
        return neutralText;
    }

    void setNeutralText(String neutralText) {
        this.neutralText = neutralText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getNegativeText(Context context) {
        if (negativeText == null) {
            return context.getString(textNegativeResId);
        }
        return negativeText;
    }

    void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
    }

    @SuppressWarnings("WeakerAccess")
    @Nullable
    public Drawable getDialogIcon(Context context) {
        if (dialogIconResId != 0) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return context.getDrawable(dialogIconResId);
                } else {
                    //noinspection deprecation
                    return context.getResources().getDrawable(dialogIconResId);
                }
            } catch (android.content.res.Resources.NotFoundException e) {
                Log.i(TAG, "Dialog icon with the given ResId doesn't exist.");
            }
        }
        return (dialogIcon != null) ? dialogIcon : AppInformation.getInstance(context).getAppIcon();
    }

    void setDialogIcon(Drawable dialogIcon) {
        this.dialogIcon = dialogIcon;
    }

    @SuppressWarnings("WeakerAccess")
    public int getThemeResId() {
        return themeResId;
    }

    void setThemeResId(int themeResId) {
        this.themeResId = themeResId;
    }
}
