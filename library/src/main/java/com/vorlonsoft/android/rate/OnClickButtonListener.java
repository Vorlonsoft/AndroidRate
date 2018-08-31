/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

/**
 * <p>OnClickButtonListener Interface -  on click button listener interface
 * of the AndroidRate library. You can implement it and use
 * {@code AppRate.with(this).setOnClickButtonListener(OnClickButtonListener)}
 * to specify the callback when the button is pressed. The same value as the
 * second argument of {@link android.content.DialogInterface.OnClickListener#onClick}
 * will be passed in the argument of {@link OnClickButtonListener#onClickButton}.</p>
 *
 * @since    0.2.0
 * @version  1.1.9
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */

public interface OnClickButtonListener {

    void onClickButton(final byte which);

}