/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

/**
 * <p>AndroidRate is a library to help you promote your Android app
 * by prompting users to rate the app after using it for a few days.</p>
 * <p>OnClickButtonListener Interface -  on click button listener interface
 * of the AndroidRate library. You can implements it and use
 * {@code AppRate.with(this).setOnClickButtonListener(OnClickButtonListener)]}
 * to specify the callback when the button is pressed. The same value as the
 * second argument of {@link android.content.DialogInterface.OnClickListener#onClick}
 * will be passed in the argument of {@link OnClickButtonListener#onClickButton}.</p>

 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 * @version  1.1.9
 * @since    0.2.0
 */

public interface OnClickButtonListener {

    void onClickButton(final byte which);

}