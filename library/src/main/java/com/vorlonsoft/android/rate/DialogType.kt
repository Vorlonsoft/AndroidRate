/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import androidx.annotation.IntDef
import com.vorlonsoft.android.rate.Constants.Utils.Companion.UTILITY_CLASS_MESSAGE

/**
 * DialogType Class - the dialog types class of the AndroidRate library.
 *
 * @constructor DialogType is a utility class and it can't be instantiated.
 * @since       1.2.1
 * @version     1.2.1
 * @author      Alexander Savin
 */
class DialogType private constructor() {
    /** DialogType Class initializer block. */
    init {
        throw UnsupportedOperationException("DialogType$UTILITY_CLASS_MESSAGE")
    }

    /**
     * Denotes that the annotated element of the primitive type int represents a logical type and
     * that its value should be one of the following constants: [APPLE], [CLASSIC], [MODERN].
     *
     * @since       1.2.1
     * @version     1.2.1
     * @author      Alexander Savin
     */
    @MustBeDocumented
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(APPLE, CLASSIC, MODERN)
    annotation class AnyDialogType

    /** Contains constants for dialog types. */
    companion object {
        /** Apple Rate Dialog. */
        const val APPLE = 0
        /** Classic Rate Dialog. */
        const val CLASSIC = 1
        /** Modern Rate Dialog. */
        const val MODERN = 2
    }
}