/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

/**
 * OnClickButtonListener Interface - the Rate Dialog buttons on-click listener interface of the
 * AndroidRate library.
 *
 * You can implement it and use [AppRate.setOnClickButtonListener] to specify a callback when the
 * Rate Dialog button is pressed. The same value as the second argument of
 * [android.content.DialogInterface.OnClickListener.onClick] will be passed in the argument of
 * [OnClickButtonListener.onClickButton].
 *
 * @since    0.2.0
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */
interface OnClickButtonListener {
    /**
     * A callback when the Rate Dialog button is pressed.
     *
     * @param which the same value as the second argument of
     * [android.content.DialogInterface.OnClickListener.onClick].
     */
    fun onClickButton(which: Byte)
}