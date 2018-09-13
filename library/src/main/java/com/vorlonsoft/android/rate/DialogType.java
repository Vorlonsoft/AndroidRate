/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import androidx.annotation.IntDef;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * <p>DialogType Class - dialog type class of the AndroidRate library.</p>
 *
 * @since    1.2.1
 * @version  1.2.1
 * @author   Alexander Savin
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class DialogType {
    /** Apple Rate Dialog */
    public static final int APPLE = 0;
    /** Classic Rate Dialog */
    public static final int CLASSIC = 1;
    /** Modern Rate Dialog */
    public static final int MODERN = 2;

    private DialogType() {
        throw new AssertionError();
    }

    /**
     * <p>Annotates element of integer type.</p>
     * <p>Annotated element, represents a logical type and its value should be one of the following
     * constants: APPLE, CLASSIC, MODERN.</p>
     */
    @Documented
    @Retention(SOURCE)
    @IntDef({
            APPLE,
            CLASSIC,
            MODERN
    })
    public @interface AnyDialogType {
    }
}