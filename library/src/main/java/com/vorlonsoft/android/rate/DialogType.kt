/*
 * Copyright 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate

import androidx.annotation.IntDef

/**
 * DialogType Class - the dialog types class of the AndroidRate library.
 *
 * Contains dialog types constants and [AnyDialogType] annotation.
 *
 * @constructor Don't create an instance of this class. Use its members directly.
 * @since       1.2.1
 * @version     1.2.1
 * @author      Alexander Savin
 */
open class DialogType {
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
        const val APPLE: Int = 0
        /** Classic Rate Dialog. */
        const val CLASSIC: Int = 1
        /** Modern Rate Dialog. */
        const val MODERN: Int = 2
    }
}