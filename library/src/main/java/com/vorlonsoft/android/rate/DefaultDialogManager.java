/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.LinearLayout.VERTICAL;
import static com.vorlonsoft.android.rate.Constants.Utils.TAG;
import static com.vorlonsoft.android.rate.DialogType.CLASSIC;
import static com.vorlonsoft.android.rate.PreferenceHelper.getDialogFirstLaunchTime;
import static com.vorlonsoft.android.rate.PreferenceHelper.increment365DayPeriodDialogLaunchTimes;
import static com.vorlonsoft.android.rate.PreferenceHelper.setDialogFirstLaunchTime;

/**
 * <p>DefaultDialogManager Class - default dialog manager class implements {@link DialogManager}
 * interface of the AndroidRate library.</p>
 * <p>You can extend DefaultDialogManager Class and use
 * {@link AppRate#setDialogManagerFactory(DialogManager.Factory)} if you want to use fully custom
 * dialog (from v7 AppCompat library etc.). DefaultDialogManager Class is an thread-safe and fast
 * singleton implementation inside library, not outside (protected, not private constructor).</p>
 *
 * @since    1.0.2
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Antoine Vianey
 * @see DefaultDialogManager.Factory
 * @see AppCompatDialogManager
 * @see DialogManager
 */
@SuppressWarnings("WeakerAccess")
public class DefaultDialogManager implements DialogManager {
    /** <p>The WeakReference to the {@link DefaultDialogManager} singleton object.</p> */
    private static volatile WeakReference<DefaultDialogManager> singleton = null;
    @SuppressWarnings({"WeakerAccess", "UnusedAssignment"})
    protected DialogOptions dialogOptions = null;
    @SuppressWarnings({"WeakerAccess", "UnusedAssignment"})
    protected Context context = null;
    /** <p>Listener used to allow to run some code when the Rate Dialog is shown.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnShowListener showListener =
                                                              new DialogInterface.OnShowListener() {
        /**
         * <p>This method will be invoked when the Rate Dialog is shown.</p>
         *
         * @param dialog the Rate Dialog that was shown will be passed into the method
         */
        @Override
        public void onShow(DialogInterface dialog) {
            if (dialogOptions.isRedrawn()) {
                if (dialogOptions.getType() != CLASSIC) {
                    View ratingBar = ((Dialog) dialog).findViewById(R.id.rate_dialog_rating_bar);
                    if (ratingBar != null) {
                        ((RatingBar) ratingBar).setRating(dialogOptions.getCurrentRating());
                    }
                }
            } else {
                if (getDialogFirstLaunchTime(context) == 0L) {
                    setDialogFirstLaunchTime(context);
                }
                increment365DayPeriodDialogLaunchTimes(context);
            }
            if ((SDK_INT >= LOLLIPOP) && (dialogOptions.getType() == CLASSIC) &&
                (dialog instanceof android.app.AlertDialog)) {
                try {
                    final Button positiveButton = ((AlertDialog) dialog).getButton(BUTTON_POSITIVE);
                    final LinearLayout linearLayout = (LinearLayout) positiveButton.getParent();
                    if ((linearLayout.getOrientation() != VERTICAL) &&
                        (positiveButton.getLeft() + positiveButton.getWidth() >
                                                                         linearLayout.getWidth())) {
                        final Button neutralButton = ((AlertDialog) dialog)
                                                                         .getButton(BUTTON_NEUTRAL);
                        final Button negativeButton = ((AlertDialog) dialog)
                                                                        .getButton(BUTTON_NEGATIVE);
                        linearLayout.setOrientation(VERTICAL);
                        linearLayout.setGravity(Gravity.END);
                        if ((neutralButton != null) && (negativeButton != null)) {
                            linearLayout.removeView(neutralButton);
                            linearLayout.removeView(negativeButton);
                            linearLayout.addView(negativeButton);
                            linearLayout.addView(neutralButton);
                        } else if (neutralButton != null) {
                            linearLayout.removeView(neutralButton);
                            linearLayout.addView(neutralButton);
                        } else if (negativeButton != null) {
                            linearLayout.removeView(negativeButton);
                            linearLayout.addView(negativeButton);
                        }
                    }
                } catch (Exception e) {
                    Log.i(TAG, "The Positive button may not fit in the window, can't check " +
                               "it and/or change the layout orientation to vertical if needed.");
                }
            }
        }
    };
    /** <p>Listener used to allow to run some code when the Rate Dialog is dismissed.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnDismissListener dismissListener =
                                                           new DialogInterface.OnDismissListener() {
        /**
         * <p>This method will be invoked when the Rate Dialog is dismissed.</p>
         *
         * @param dialog the Rate Dialog that was dismissed will be passed into the method
         */
        @Override
        public void onDismiss(@Nullable DialogInterface dialog) {
            dialogOptions.setRedrawn(true);
        }
    };
    /** <p>Listener used to notify when the rating on the Rate Dialog has been changed.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final RatingBar.OnRatingBarChangeListener ratingBarListener =
                                                         new RatingBar.OnRatingBarChangeListener() {
        /**
         * <p>Notification that the rating on the Rate Dialog has changed.</p>
         * <p>You can use the {@code fromUser} parameter to distinguish user-initiated changes from
         * those that occurred programmatically. This will not be called continuously while the user
         * is dragging, only when the user finalizes a rating by lifting the touch.</p>
         *
         * @param ratingBar the RatingBar whose rating has changed
         * @param rating the current rating. This will be in the range 0..numStars.
         * @param fromUser true if the rating change was initiated by a user
         */
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            final View view = ratingBar.getRootView();
            final View layoutRatingBar = view.findViewById(R.id.rate_dialog_layout_rating_bar);
            final View buttonNeutral = view.findViewById(R.id.rate_dialog_button_neutral);
            final View buttonNegative = view.findViewById(R.id.rate_dialog_button_negative);
            final View buttonPositive = view.findViewById(R.id.rate_dialog_button_positive);
            final boolean showNeutralButton = dialogOptions.isShowNeutralButton() &&
                                                                            (buttonNeutral != null);
            final boolean showNegativeButton = dialogOptions.isShowNegativeButton() &&
                                                                           (buttonNegative != null);
            final boolean showPositiveButton = buttonPositive != null;

            if (fromUser) {
                dialogOptions.setCurrentRating((byte) rating);
            }

            if (!showNegativeButton && showPositiveButton) {
                buttonPositive
                            .setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_bottom);
                buttonPositive.setVisibility(VISIBLE);
            } else if (showNegativeButton && !showPositiveButton) {
                buttonNegative
                            .setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_bottom);
                buttonNegative.setVisibility(VISIBLE);
            } else if (showNegativeButton) {
                buttonNegative.setVisibility(VISIBLE);
                buttonPositive.setVisibility(VISIBLE);
            }
            if (showNeutralButton) {
                buttonNeutral.setVisibility(GONE);
            }

            if (layoutRatingBar != null) {
                if ((dialogOptions.isShowIcon() &&
                     (view.findViewById(R.id.rate_dialog_icon) != null)) ||
                    (dialogOptions.isShowTitle() &&
                     (view.findViewById(R.id.rate_dialog_text_dialog_title) != null)) ||
                    (dialogOptions.isShowMessage() &&
                     (view.findViewById(R.id.rate_dialog_text_dialog_message) != null))) {
                    if (showNeutralButton && !(showNegativeButton || showPositiveButton)) {
                        layoutRatingBar
                            .setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_bottom);
                    } else if (!showNeutralButton && (showNegativeButton || showPositiveButton)) {
                        layoutRatingBar.setBackgroundResource(R.color.rateDialogColorBackground);
                    }
                } else if (!showNeutralButton && (showNegativeButton || showPositiveButton)) {
                    layoutRatingBar
                            .setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_top);
                } else if (showNeutralButton && !(showNegativeButton || showPositiveButton)) {
                    layoutRatingBar.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded);
                }
            }
        }
    };
    /** <p>Listener used to allow to run some code when a button on the Rate Dialog is
     * clicked.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final View.OnClickListener buttonListener;
    /** <p>Listener used to allow to run some code when a positive button on the Rate Dialog is
     * clicked.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener positiveListener;
    /** <p>Listener used to allow to run some code when a negative button on the Rate Dialog is
     * clicked.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener negativeListener;
    /** <p>Listener used to allow to run some code when a neutral button on the Rate Dialog is
     * clicked.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener neutralListener;

    @SuppressWarnings("WeakerAccess")
    protected DefaultDialogManager(final Context context, final DialogOptions dialogOptions,
                                   final StoreOptions storeOptions) {
        this.context = context;
        this.dialogOptions = dialogOptions;
        positiveListener = DefaultDialogOnClickListener.getInstance(context, storeOptions,
                                                                 dialogOptions.getButtonListener());
        negativeListener = positiveListener;
        neutralListener = positiveListener;
        buttonListener = (View.OnClickListener) positiveListener;
    }

    @SuppressWarnings("WeakerAccess")
    protected void setContext(Context context){
        this.context = context;
    }

    /**
     * <p>Creates {@link android.app.AlertDialog.Builder}.</p>
     *
     * @param context activity context
     * @param themeResId theme resource ID
     * @return created {@link android.app.AlertDialog.Builder} object
     * @see AppCompatDialogManager#getAppCompatDialogBuilder(Context, int)
     */
    @SuppressWarnings("WeakerAccess")
    @NonNull
    protected AlertDialog.Builder getDialogBuilder(@NonNull final Context context,
                                                            final int themeResId) {
        return Utils.getDialogBuilder(context, themeResId);
    }

    /**
     * <p>Supplies the arguments to the {@link DialogType#CLASSIC CLASSIC} Rate Dialog
     * {@link android.app.AlertDialog.Builder}.</p>
     *
     * @param builder the {@link DialogType#CLASSIC CLASSIC} Rate Dialog
     *                {@link android.app.AlertDialog.Builder}
     * @param dialogContext a Context for Rate Dialogs created by this Builder
     * @see AppCompatDialogManager#supplyAppCompatClassicDialogArguments(androidx.appcompat.app.AlertDialog.Builder, Context)
     */
    @SuppressWarnings("WeakerAccess")
    protected void supplyClassicDialogArguments(@NonNull AlertDialog.Builder builder,
                                                @NonNull Context dialogContext) {
        if (dialogOptions.isShowIcon()) {
            builder.setIcon(dialogOptions.getIcon(dialogContext));
        }
        if (dialogOptions.isShowTitle()) {
            builder.setTitle(dialogOptions.getTitleText(context));
        }
        if (dialogOptions.isShowMessage()) {
            builder.setMessage(dialogOptions.getMessageText(context));
        }
        if (dialogOptions.isShowNeutralButton()) {
            builder.setNeutralButton(dialogOptions.getNeutralText(context), neutralListener);
        }
        if (dialogOptions.isShowNegativeButton()) {
            builder.setNegativeButton(dialogOptions.getNegativeText(context), negativeListener);
        }
        builder.setPositiveButton(dialogOptions.getPositiveText(context), positiveListener);
    }

    /**
     * <p>Supplies the arguments to the non-{@link DialogType#CLASSIC CLASSIC} Rate Dialog
     * Builder.</p>
     *
     * @param view the non-{@link DialogType#CLASSIC CLASSIC} Rate Dialog View
     * @param dialogContext a Context for Rate Dialogs created with this View
     */
    @SuppressWarnings("WeakerAccess")
    protected void supplyNonClassicDialogArguments(@NonNull View view,
                                                   @NonNull Context dialogContext) {
        final View layoutHead = view.findViewById(R.id.rate_dialog_layout_head);
        final View icon = view.findViewById(R.id.rate_dialog_icon);
        final View textDialogTitle = view.findViewById(R.id.rate_dialog_text_dialog_title);
        final View textDialogMessage = view.findViewById(R.id.rate_dialog_text_dialog_message);
        final View layoutRatingBar = view.findViewById(R.id.rate_dialog_layout_rating_bar);
        final View ratingBar = view.findViewById(R.id.rate_dialog_rating_bar);
        final View buttonNeutral = view.findViewById(R.id.rate_dialog_button_neutral);
        final View buttonNegative = view.findViewById(R.id.rate_dialog_button_negative);
        final View buttonPositive = view.findViewById(R.id.rate_dialog_button_positive);
        final boolean showDialogIcon = dialogOptions.isShowIcon() && (icon != null);
        final boolean showTitle = dialogOptions.isShowTitle() && (textDialogTitle != null);
        final boolean showMessage = dialogOptions.isShowMessage() &&
                                                                        (textDialogMessage != null);
        final boolean showNeutralButton = dialogOptions.isShowNeutralButton() &&
                                                                            (buttonNeutral != null);

        if (showDialogIcon) {
            ((ImageView) icon).setImageDrawable(dialogOptions.getIcon(dialogContext));
        } else {
            if (icon != null) {
                icon.setVisibility(GONE);
            }
            if (showTitle) {
                ViewGroup.LayoutParams layoutParams = textDialogTitle.getLayoutParams();
                if (layoutParams != null) {
                    ((LinearLayout.LayoutParams) layoutParams).setMargins(0,0,0,0);
                    textDialogTitle.setLayoutParams(layoutParams);
                }
            }
        }

        if (showTitle) {
            ((TextView) textDialogTitle).setText(dialogOptions.getTitleText(context));
        } else if (textDialogTitle != null) {
            textDialogTitle.setVisibility(GONE);
        }

        if (showMessage) {
            ((TextView) textDialogMessage).setText(dialogOptions.getMessageText(context));
        } else if (textDialogMessage != null) {
            textDialogMessage.setVisibility(GONE);
        }

        if (ratingBar != null) {
            ((RatingBar) ratingBar).setOnRatingBarChangeListener(ratingBarListener);
        }

        if (showNeutralButton) {
            ((Button) buttonNeutral).setText(dialogOptions.getNeutralText(context));
            buttonNeutral.setOnClickListener(buttonListener);
        } else if (buttonNeutral != null) {
            buttonNeutral.setVisibility(GONE);
        }

        if (dialogOptions.isShowNegativeButton() && (buttonNegative != null)) {
            ((Button) buttonNegative).setText(dialogOptions.getNegativeText(context));
            buttonNegative.setOnClickListener(buttonListener);
        } else if (buttonNegative != null) {
            buttonNegative.setVisibility(GONE);
        }

        if (buttonPositive != null) {
            ((Button) buttonPositive).setText(dialogOptions.getPositiveText(context));
            buttonPositive.setOnClickListener(buttonListener);
        }

        if (showDialogIcon || showTitle || showMessage) {
            if ((layoutRatingBar != null) && !showNeutralButton) {
                layoutRatingBar
                            .setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_bottom);
            } else if ((layoutHead != null) && !showNeutralButton) {
                layoutHead.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded);
            }
        } else if (layoutRatingBar != null) {
            if (showNeutralButton) {
                layoutRatingBar.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_top);
            } else {
                layoutRatingBar.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded);
            }
        } else if (showNeutralButton) {
            buttonNeutral.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded);
        }
    }

    /**
     * <p>Creates Rate Dialog.</p>
     *
     * @return created dialog
     */
    @SuppressWarnings("unused")
    @Nullable
    @Override
    public Dialog createDialog() {

        AlertDialog.Builder builder = getDialogBuilder(context, dialogOptions.getThemeResId());
        Context dialogContext;

        if (SDK_INT >= HONEYCOMB) {
            dialogContext = builder.getContext();
        } else {
            dialogContext = context;
        }

        final View view = dialogOptions.getView(dialogContext);

        if ((dialogOptions.getType() == CLASSIC) || (view == null)) {
            if (dialogOptions.getType() != CLASSIC) {
                builder = getDialogBuilder(context, 0);
                if (SDK_INT >= HONEYCOMB) {
                    dialogContext = builder.getContext();
                }
            }
            supplyClassicDialogArguments(builder, dialogContext);
        } else {
            supplyNonClassicDialogArguments(view, dialogContext);
        }

        final AlertDialog alertDialog = builder
                .setCancelable(dialogOptions.getCancelable())
                .setView(view)
                .create();

        alertDialog.setOnShowListener(showListener);
        alertDialog.setOnDismissListener(dismissListener);

        return alertDialog;
    }

    /**
     * <p>DefaultDialogManager.Factory Class - default dialog manager factory class implements
     * {@link DialogManager.Factory} interface of the AndroidRate library.</p>
     * <p>You can extend DefaultDialogManager.Factory Class and use
     * {@link AppRate#setDialogManagerFactory(DialogManager.Factory)} if you want to use fully
     * custom dialog (from v7 AppCompat library etc.).</p>
     *
     * @since    1.0.2
     * @version  1.2.1
     * @author   Alexander Savin
     * @author   Antoine Vianey
     * @see DialogManager.Factory
     */
    static class Factory implements DialogManager.Factory {

        Factory() {
            if (singleton != null) {
                singleton.clear();
            }
        }

        /**
         * <p>Clears {@link DefaultDialogManager} singleton.</p>
         */
        @SuppressWarnings("unused")
        @Override
        public void clearDialogManager() {
            if (singleton != null) {
                singleton.clear();
            }
        }

        /**
         * <p>Creates {@link DefaultDialogManager} singleton object.</p>
         *
         * @param context activity context
         * @param dialogOptions Rate Dialog options
         * @param storeOptions App store options
         * @return {@link DefaultDialogManager} singleton object
         */
        @SuppressWarnings("unused")
        @NonNull
        @Override
        public DialogManager createDialogManager(@NonNull final Context context,
                                                 @NonNull final DialogOptions dialogOptions,
                                                 @NonNull final StoreOptions storeOptions) {
            if ((singleton == null) || (singleton.get() == null)) {
                synchronized (DefaultDialogManager.class) {
                    if ((singleton == null) || (singleton.get() == null)) {
                        if (singleton != null) {
                            singleton.clear();
                        }
                        singleton = new WeakReference<>(new DefaultDialogManager(context,
                                                                      dialogOptions, storeOptions));
                    } else {
                        singleton.get().setContext(context);
                    }
                }
            } else {
                singleton.get().setContext(context);
            }
            return singleton.get();
        }
    }
}