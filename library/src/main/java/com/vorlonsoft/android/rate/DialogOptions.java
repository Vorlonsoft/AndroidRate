/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.SoftReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import static com.vorlonsoft.android.rate.Constants.Utils.TAG;
import static com.vorlonsoft.android.rate.DialogType.CLASSIC;

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
    /** <p>Whether the Rate Dialog should show the message.</p> */
    private boolean showMessage = true;

    private boolean showNegativeButton = true;

    private boolean showNeutralButton = true;

    private boolean showTitle = true;
    /** Default value is null. */
    private Boolean showDialogIcon = null;

    private int dialogIconResId = 0;
    /** <p>One of the dialog types defined by {@link DialogType.AnyDialogType}.</p> */
    @DialogType.AnyDialogType
    private int dialogType = DialogType.CLASSIC;

    private int textMessageResId = R.string.rate_dialog_message;

    private int textNegativeResId = R.string.rate_dialog_no;

    private int textNeutralResId = R.string.rate_dialog_cancel;

    private int textPositiveResId = R.string.rate_dialog_ok;

    private int textTitleResId = R.string.rate_dialog_title;

    private Integer themeResId = null;

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

    /**
     * <p>Returns whether the Rate Dialog should show the message.</p>
     *
     * @return true to show the message, false otherwise
     */
    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowMessage() {
        return showMessage;
    }

    /**
     * <p>Sets whether the Rate Dialog should show the message.</p>
     *
     * @param showMessage true to show the message, false otherwise
     * @see AppRate#setShowMessage(boolean)
     */
    void setShowMessage(final boolean showMessage) {
        this.showMessage = showMessage;
    }

    /**
     * <p>Decides whether the Neutral button ("Remind Me Later") appears in the Rate Dialog or
     * not.</p>
     *
     * @return true to show the Neutral button, false otherwise
     */
    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowNeutralButton() {
        return showNeutralButton;
    }

    /**
     * <p>Sets whether the Neutral button ("Remind Me Later") appears in the Rate Dialog or
     * not.</p>
     *
     * @param showNeutralButton default is true, true means to show the Neutral button
     * @see AppRate#setShowLaterButton(boolean)
     */
    void setShowNeutralButton(final boolean showNeutralButton) {
        this.showNeutralButton = showNeutralButton;
    }

    /**
     * <p>Decides if the Negative button appears in the Rate Dialog or not.</p>
     *
     * @return true to show the Negative button, false otherwise
     */
    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowNegativeButton() {
        return showNegativeButton;
    }

    /**
     * <p>Sets if the Negative button appears in the Rate Dialog or not.</p>
     *
     * @param showNegativeButton default is true, true means to show the Negative button
     * @see AppRate#setShowNeverButton(boolean)
     */
    void setShowNegativeButton(final boolean showNegativeButton) {
        this.showNegativeButton = showNegativeButton;
    }

    /**
     * <p>Whether the Rate Dialog should show the title.</p>
     *
     * @return true to show the title, false otherwise
     */
    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowTitle() {
        return showTitle;
    }

    /**
     * <p>Sets whether the Rate Dialog should show the title.</p>
     *
     * @param showTitle true to show the title, false otherwise
     * @see AppRate#setShowTitle(boolean)
     */
    void setShowTitle(final boolean showTitle) {
        this.showTitle = showTitle;
    }

    /**
     * <p>Whether the Rate Dialog should show the icon.</p>
     *
     * @return true to show the icon, false otherwise
     */
    @SuppressWarnings("WeakerAccess")
    public boolean shouldShowDialogIcon() {
        return (showDialogIcon == null) ? !(dialogType == CLASSIC) : showDialogIcon;
    }

    /**
     * <p>Sets whether the Rate Dialog should show the icon.</p>
     *
     * @param showDialogIcon true to show the icon, false otherwise, default values are false for
     *                       {@link DialogType#CLASSIC DialogType.CLASSIC} and true for
     *                       {@link DialogType#APPLE DialogType.APPLE} and
     *                       {@link DialogType#MODERN DialogType.MODERN}
     * @see AppRate#setShowDialogIcon(boolean)
     */
    void setShowDialogIcon(final boolean showDialogIcon) {
        this.showDialogIcon = showDialogIcon;
    }

    /**
     * <p>Whether the Rate Dialog is cancelable or not.</p>
     *
     * @return true if the Rate Dialog is cancelable, false otherwise
     */
    @SuppressWarnings("WeakerAccess")
    public boolean getCancelable() {
        return cancelable;
    }

    /**
     * <p>Sets whether the Rate Dialog is cancelable or not.</p>
     *
     * @param cancelable default is false, true will set the Rate Dialog cancelable
     * @see AppRate#setCancelable(boolean)
     */
    void setCancelable(final boolean cancelable) {
        this.cancelable = cancelable;
    }

    /**
     * <p>Returns the Rate Dialog type.</p>
     *
     * @return one of the values defined by {@link DialogType.AnyDialogType}
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    @DialogType.AnyDialogType
    public int getDialogType() {
        return dialogType;
    }

    /**
     * <p>Sets the Rate Dialog type.</p>
     *
     * @param dialogType one of the values defined by {@link DialogType.AnyDialogType}
     */
    void setDialogType(@DialogType.AnyDialogType final int dialogType) {
        this.dialogType = dialogType;
    }

    /**
     * <p>Returns the Rate Dialog icon resource ID.</p>
     *
     * @return the Rate Dialog icon resource ID
     */
    @SuppressWarnings("unused")
    int getDialogIconResId() {
        return dialogIconResId;
    }

    /**
     * <p>Sets the Rate Dialog icon resource ID.</p>
     *
     * @param dialogIconResId the Rate Dialog icon resource ID
     * @see AppRate#setDialogIcon(int)
     * @see AppRate##setShowDialogIcon(boolean)
     */
    void setDialogIconResId(final int dialogIconResId) {
        this.dialogIconResId = dialogIconResId;
    }

    /**
     * <p>Returns the Rate Dialog title text.</p>
     *
     * @return the Rate Dialog title text resource ID
     */
    @SuppressWarnings("unused")
    int getTitleResId() {
        return textTitleResId;
    }

    /**
     * <p>Sets the Rate Dialog title text.</p>
     *
     * @param textTitleResId the Rate Dialog title text resource ID
     * @see AppRate#setTitle(int)
     * @see AppRate#setShowTitle(boolean)
     */
    void setTitleResId(final int textTitleResId) {
        this.textTitleResId = textTitleResId;
    }

    @SuppressWarnings("unused")
    int getMessageResId() {
        return textMessageResId;
    }

    void setMessageResId(final int textMessageResId) {
        this.textMessageResId = textMessageResId;
    }

    @SuppressWarnings("unused")
    int getTextPositiveResId() {
        return textPositiveResId;
    }

    void setTextPositiveResId(final int textPositiveResId) {
        this.textPositiveResId = textPositiveResId;
    }

    @SuppressWarnings("unused")
    int getTextNeutralResId() {
        return textNeutralResId;
    }

    void setTextNeutralResId(final int textNeutralResId) {
        this.textNeutralResId = textNeutralResId;
    }

    @SuppressWarnings("unused")
    int getTextNegativeResId() {
        return textNegativeResId;
    }

    void setTextNegativeResId(final int textNegativeResId) {
        this.textNegativeResId = textNegativeResId;
    }

    /**
     * <p>Returns Rate Dialog View. <b>Use only for {@link DialogType#CLASSIC DialogType.CLASSIC}
     * dialogs!</b></p>
     *
     * @return Rate Dialog View
     * @deprecated since 1.2.2 use {@link #getView(Context)} instead
     * @see #getView(Context)
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    @Deprecated
    public View getView() {
        if (dialogType != CLASSIC) {
            Log.w(TAG, "For other than DialogType.CLASSIC dialog types call " +
                       "DialogOptions#getView(Context) instead of DialogOptions#getView().");
        }
        return getView(null);
    }

    /**
     * <p>Returns Rate Dialog View.</p>
     *
     * @param dialogContext a Context with the appropriate theme for built dialogs
     * @return Rate Dialog View
     * @since 1.2.2
     */
    @SuppressLint("InflateParams")
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public View getView(final Context dialogContext) {
        if ((view == null) && !(dialogType == CLASSIC)) {
            if (dialogContext != null) {
                try {
                    return LayoutInflater.from(dialogContext).inflate(R.layout.rate_dialog,
                                                                      null);
                } catch (AssertionError e) {
                    Log.i(TAG, "Can't inflate the R.layout.rate_dialog, layoutInflater not " +
                               "found, DialogType.CLASSIC dialog will be used.");
                }
            } else {
                Log.w(TAG, "Can't inflate the R.layout.rate_dialog, dialogContext == null, " +
                           "DialogType.CLASSIC dialog will be used.");
            }
        }
        return view;
    }

    void setView(@Nullable final View view) {
        this.view = view;
    }

    @Nullable
    OnClickButtonListener getListener() {
        return listener != null ? listener.get() : null;
    }

    void setListener(final OnClickButtonListener listener) {
        this.listener = new SoftReference<>(listener);
    }

    /**
     * <p>Returns the Rate Dialog title text.</p>
     *
     * @param context activity context
     * @return the Rate Dialog title text
     */
    @SuppressWarnings("WeakerAccess")
    public String getTitleText(final Context context) {
        if (titleText == null) {
            return context.getString(textTitleResId);
        }
        return titleText;
    }

    /**
     * <p>Sets the Rate Dialog title text.</p>
     *
     * @param titleText the Rate Dialog title text
     * @see AppRate#setTitle(int)
     * @see AppRate#setShowTitle(boolean)
     */
    void setTitleText(final String titleText) {
        this.titleText = titleText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getMessageText(final Context context) {
        if (messageText == null) {
            return context.getString(textMessageResId);
        }
        return messageText;
    }

    void setMessageText(final String messageText) {
        this.messageText = messageText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getPositiveText(final Context context) {
        if (positiveText == null) {
            return context.getString(textPositiveResId);
        }
        return positiveText;
    }

    void setPositiveText(final String positiveText) {
        this.positiveText = positiveText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getNeutralText(final Context context) {
        if (neutralText == null) {
            return context.getString(textNeutralResId);
        }
        return neutralText;
    }

    void setNeutralText(final String neutralText) {
        this.neutralText = neutralText;
    }

    @SuppressWarnings("WeakerAccess")
    public String getNegativeText(final Context context) {
        if (negativeText == null) {
            return context.getString(textNegativeResId);
        }
        return negativeText;
    }

    void setNegativeText(final String negativeText) {
        this.negativeText = negativeText;
    }

    @SuppressWarnings("WeakerAccess")
    @Nullable
    public Drawable getDialogIcon(@NonNull final Context context) {
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

    void setDialogIcon(final Drawable dialogIcon) {
        this.dialogIcon = dialogIcon;
    }

    @SuppressWarnings("WeakerAccess")
    @StyleRes
    public int getThemeResId() {
        if (themeResId == null) {
            return !(dialogType == CLASSIC) ? R.style.RateDialogTransparentTheme : 0;
        }
        return themeResId;
    }

    void setThemeResId(@StyleRes final int themeResId) {
        this.themeResId = themeResId;
    }
}