/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import com.vorlonsoft.android.rate.Constants.Utils.EMPTY_STRING
import com.vorlonsoft.android.rate.Constants.Utils.TAG
import com.vorlonsoft.android.rate.DialogType.Companion.CLASSIC
import java.lang.ref.SoftReference

/**
 * DialogOptions Class - dialog options class of the AndroidRate library.
 *
 * Contains dialog properties with setters and getters.
 *
 * @since    0.5.1
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */
class DialogOptions internal constructor() {
    /** Whether the Rate Dialog is cancelable or not. */
    var cancelable: Boolean = false

    /** Whether the current Rate Dialog had redrawn one or more times. */
    var isRedrawn: Boolean = false

    /** Whether the Rate Dialog should show an icon. Default value is null. */
    var isShowIcon: Boolean? = null
        get() = if (field != null) field else (type != CLASSIC)

    /** Whether the Rate Dialog should show the message. */
    var isShowMessage: Boolean = true
    /**
     * Returns whether the Rate Dialog should show the message.
     *
     * @return true to show the message, false otherwise
     */
    @Suppress("unused")
    fun shouldShowMessage(): Boolean {
        return isShowMessage
    }

    /** Whether the Rate Dialog should show the Negative button. */
    var isShowNegativeButton: Boolean = true
    /**
     * Returns whether the Rate Dialog should show the Negative button.
     *
     * @return true to show the Negative button, false otherwise
     */
    @Suppress("unused")
    fun shouldShowNegativeButton(): Boolean {
        return isShowNegativeButton
    }

    /** Whether the Rate Dialog should show the Neutral button. */
    var isShowNeutralButton: Boolean = true
    /**
     * Returns whether the Rate Dialog should show the Neutral button.
     *
     * @return true to show the Neutral button, false otherwise
     */
    @Suppress("unused")
    fun shouldShowNeutralButton(): Boolean {
        return isShowNeutralButton
    }

    /** Whether the Rate Dialog should show the title. */
    var isShowTitle: Boolean = true
    /**
     * Returns whether the Rate Dialog should show the title.
     *
     * @return true to show the title, false otherwise
     */
    @Suppress("unused")
    fun shouldShowTitle(): Boolean {
        return isShowTitle
    }

    /** The current rating set by the app user, 0 means that the user hasn't set a rating yet. */
    var currentRating: Byte = 0

    /** The Rate Dialog icon resource ID. */
    var iconResId: Int = 0

    /** The Rate Dialog message text resource ID. */
    var messageTextResId: Int = R.string.rate_dialog_message

    /** The Rate Dialog negative text resource ID. */
    var negativeTextResId: Int = R.string.rate_dialog_no

    /** The Rate Dialog neutral text resource ID. */
    var neutralTextResId: Int = R.string.rate_dialog_cancel

    /** The Rate Dialog positive text resource ID. */
    var positiveTextResId: Int = R.string.rate_dialog_ok

    /** The Rate Dialog theme resource ID. */
    var themeResId: Int? = null
        @StyleRes get() = if (field != null) field else
            if (type != CLASSIC) R.style.RateDialogTransparentTheme else 0

    /** The Rate Dialog title text resource ID. */
    var titleTextResId: Int = R.string.rate_dialog_title

    /** The Rate Dialog type. One of the values defined by [DialogType.AnyDialogType]. */
    @DialogType.AnyDialogType
    var type: Int = DialogType.CLASSIC
        @DialogType.AnyDialogType get
    /**
     * Returns the Rate Dialog type.
     *
     * @return one of the values defined by [DialogType.AnyDialogType]
     */
    @Suppress("unused")
    @DialogType.AnyDialogType
    fun getDialogType(): Int {
        return type
    }

    /** The Rate Dialog message text. */
    private var messageText: String? = null
    /**
     * Creates the Rate Dialog message text.
     *
     * @param context activity context
     * @return the Rate Dialog message text
     */
    fun getMessageText(context: Context): String {
        return if (messageText == null) context.getString(messageTextResId)
        else messageText ?: EMPTY_STRING
    }
    /**
     * Sets the Rate Dialog message text.
     *
     * @param messageText the Rate Dialog message text
     */
    fun setMessageText(messageText: String) {
        this.messageText = messageText
    }

    /** The Rate Dialog negative text. */
    private var negativeText: String? = null
    /**
     * Creates the Rate Dialog negative text.
     *
     * @param context activity context
     * @return the Rate Dialog negative text
     */
    fun getNegativeText(context: Context): String {
        return if (negativeText == null) context.getString(negativeTextResId)
        else negativeText ?: EMPTY_STRING
    }
    /**
     * Sets the Rate Dialog negative text.
     *
     * @param negativeText the Rate Dialog negative text
     */
    fun setNegativeText(negativeText: String) {
        this.negativeText = negativeText
    }

    /** The Rate Dialog neutral text. */
    private var neutralText: String? = null
    /**
     * Creates the Rate Dialog neutral text.
     *
     * @param context activity context
     * @return the Rate Dialog neutral text
     */
    fun getNeutralText(context: Context): String {
        return if (neutralText == null) context.getString(neutralTextResId)
        else neutralText ?: EMPTY_STRING
    }
    /**
     * Sets the Rate Dialog neutral text.
     *
     * @param neutralText the Rate Dialog neutral text
     */
    fun setNeutralText(neutralText: String) {
        this.neutralText = neutralText
    }

    /** The Rate Dialog positive text. */
    private var positiveText: String? = null
    /**
     * Creates the Rate Dialog positive text.
     *
     * @param context activity context
     * @return the Rate Dialog positive text
     */
    fun getPositiveText(context: Context): String {
        return if (positiveText == null) context.getString(positiveTextResId)
        else positiveText ?: EMPTY_STRING
    }
    /**
     * Sets the Rate Dialog positive text.
     *
     * @param positiveText the Rate Dialog positive text
     */
    fun setPositiveText(positiveText: String) {
        this.positiveText = positiveText
    }

    /** The Rate Dialog title text. */
    private var titleText: String? = null
    /**
     * Creates the Rate Dialog title text.
     *
     * @param context activity context
     * @return the Rate Dialog title text
     */
    fun getTitleText(context: Context): String {
        return if (titleText == null) context.getString(titleTextResId)
        else titleText ?: EMPTY_STRING
    }
    /**
     * Sets the Rate Dialog title text.
     *
     * @param titleText the Rate Dialog title text
     */
    fun setTitleText(titleText: String) {
        this.titleText = titleText
    }

    /** The Rate Dialog SoftReference to the buttons listener implemented by a library user. */
    private var buttonListener: SoftReference<OnClickButtonListener>? = null
    /**
     * Returns the Rate Dialog buttons on-click listener implemented by a library user.
     *
     * @return the Rate Dialog buttons on-click listener
     */
    fun getButtonListener(): OnClickButtonListener? {
        return buttonListener?.get()
    }
    /**
     * Sets the Rate Dialog SoftReference to the buttons on-click listener implemented by a library
     * user.
     *
     * @param buttonListener the Rate Dialog buttons on-click listener
     */
    fun setButtonListener(buttonListener: OnClickButtonListener) {
        this.buttonListener = SoftReference(buttonListener)
    }

    /** The Rate Dialog icon. */
    private var icon: Drawable? = null
    /**
     * Returns the Rate Dialog icon.
     *
     * @param context activity context
     * @return the Rate Dialog icon
     */
    fun getIcon(context: Context): Drawable? {
        return when {
            (iconResId != 0) -> try {
                if (SDK_INT >= LOLLIPOP) {
                    context.getDrawable(iconResId)
                } else {
                    @Suppress("DEPRECATION")
                    context.resources.getDrawable(iconResId)
                }
            } catch (e: android.content.res.Resources.NotFoundException) {
                Log.i(TAG, "Dialog icon with the given ResId doesn't exist.")
                if (icon != null) icon else AppInformation.getIcon(context)
            }
            (icon != null) -> icon
            else -> AppInformation.getIcon(context)
        }
    }
    /**
     * Sets the Rate Dialog icon.
     *
     * @param icon the Rate Dialog icon
     */
    fun setIcon(icon: Drawable?) {
        this.icon = icon
    }
    /**
     * Returns the Rate Dialog icon.
     *
     * @param context activity context
     * @return the Rate Dialog icon
     */
    @Suppress("unused")
    fun getDialogIcon(context: Context): Drawable? {
        return getIcon(context)
    }

    /** The Rate Dialog view. */
    private var view: View? = null
    /**
     * Returns the Rate Dialog view.
     *
     * @return the Rate Dialog view
     */
    @Deprecated("Since 1.2.5 use DialogOptions.getView(Context) instead.")
    fun getView(): View? {
        if (type != CLASSIC) {
            Log.w(TAG, "For other than DialogType.CLASSIC dialog types call the " +
                           "DialogOptions.getView(Context) instead of the DialogOptions.getView().")
        }
        return getView(null)
    }
    /**
     * Returns the Rate Dialog view.
     *
     * @param dialogContext a Context with an appropriate theme for built dialogs
     * @return the Rate Dialog view
     * @since 1.2.5
     */
    @SuppressLint("InflateParams")
    fun getView(dialogContext: Context?): View? {
        return if ((view == null) && (type != CLASSIC)) {
            var dialogView: View? = null
            if (dialogContext != null) try {
                dialogView = LayoutInflater.from(dialogContext).inflate(R.layout.rate_dialog, null)
            } catch (e: AssertionError) {
                Log.i(TAG, "Can't inflate the R.layout.rate_dialog, layoutInflater not found, " +
                                                          "DialogType.CLASSIC dialog will be used.")
            } else Log.w(TAG, "Can't inflate the R.layout.rate_dialog, dialogContext == null, " +
                                                          "DialogType.CLASSIC dialog will be used.")
            dialogView
        } else view
    }
    /**
     * Sets the Rate Dialog view.
     *
     * @param view the Rate Dialog view
     */
    fun setView(view: View?) {
        this.view = view
    }
}