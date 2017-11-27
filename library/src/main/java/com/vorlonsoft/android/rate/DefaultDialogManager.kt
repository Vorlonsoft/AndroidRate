/*
 * Copyright 2017 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.OnClickListener

import com.vorlonsoft.android.rate.IntentHelper.createIntentForAmazonAppstore
import com.vorlonsoft.android.rate.IntentHelper.createIntentForGooglePlay
import com.vorlonsoft.android.rate.PreferenceHelper.setAgreeShowDialog
import com.vorlonsoft.android.rate.PreferenceHelper.setRemindInterval
import com.vorlonsoft.android.rate.Utils.getDialogBuilder
import java.lang.ref.Reference

class DefaultDialogManager(private val context: Context, private val options: DialogOptions) : DialogManager {
    private val listener = options.listener

    protected val positiveListener: OnClickListener = OnClickListener { dialog, which ->
        val intentToAppstore = if (options.storeType == StoreType.GOOGLEPLAY)
            createIntentForGooglePlay(context)
        else
            createIntentForAmazonAppstore(context)
        context.startActivity(intentToAppstore)
        setAgreeShowDialog(context, false)
        listener?.onClickButton(which)
    }
    protected val negativeListener: OnClickListener = OnClickListener { dialog, which ->
        setAgreeShowDialog(context, false)
        if (this@DefaultDialogManager.listener != null) this@DefaultDialogManager.listener.onClickButton(which)
    }
    protected val neutralListener: OnClickListener = OnClickListener { dialog, which ->
        setRemindInterval(context)
        listener?.onClickButton(which)
    }

    internal class Factory : DialogManager.Factory {
        override fun createDialogManager(context: Context, options: DialogOptions): DialogManager {
            return DefaultDialogManager(context, options)
        }
    }

    override fun createDialog(): Dialog {
        val builder = getDialogBuilder(context, options.themeResId)
        builder.setMessage(options.getMessageText(context))

        if (options.shouldShowTitle()) builder.setTitle(options.getTitleText(context))

        builder.setCancelable(options.cancelable)

        val view = options.view
        if (view != null) builder.setView(view)

        builder.setPositiveButton(options.getPositiveText(context), positiveListener)

        if (options.shouldShowNeutralButton()) {
            builder.setNeutralButton(options.getNeutralText(context), neutralListener)
        }

        if (options.shouldShowNegativeButton()) {
            builder.setNegativeButton(options.getNegativeText(context), negativeListener)
        }

        return builder.create()
    }

}

private fun <T> Reference<T>.onClickButton(which: Int) {}
