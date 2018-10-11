/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vorlonsoft.android.rate.Constants.Utils.EMPTY_STRING;
import static com.vorlonsoft.android.rate.Constants.Utils.LOG_MESSAGE_PART_1;
import static com.vorlonsoft.android.rate.Constants.Utils.TAG;
import static com.vorlonsoft.android.rate.DialogType.CLASSIC;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForStore;
import static com.vorlonsoft.android.rate.PreferenceHelper.getDialogFirstLaunchTime;
import static com.vorlonsoft.android.rate.PreferenceHelper.increment365DayPeriodDialogLaunchTimes;
import static com.vorlonsoft.android.rate.PreferenceHelper.setDialogFirstLaunchTime;
import static com.vorlonsoft.android.rate.PreferenceHelper.setIsAgreeShowDialog;
import static com.vorlonsoft.android.rate.PreferenceHelper.setRemindInterval;
import static com.vorlonsoft.android.rate.PreferenceHelper.setRemindLaunchesNumber;
import static com.vorlonsoft.android.rate.StoreType.AMAZON;
import static com.vorlonsoft.android.rate.StoreType.APPLE;
import static com.vorlonsoft.android.rate.StoreType.BAZAAR;
import static com.vorlonsoft.android.rate.StoreType.BLACKBERRY;
import static com.vorlonsoft.android.rate.StoreType.CHINESESTORES;
import static com.vorlonsoft.android.rate.StoreType.GOOGLEPLAY;
import static com.vorlonsoft.android.rate.StoreType.INTENT;
import static com.vorlonsoft.android.rate.StoreType.MI;
import static com.vorlonsoft.android.rate.StoreType.OTHER;
import static com.vorlonsoft.android.rate.StoreType.SAMSUNG;
import static com.vorlonsoft.android.rate.StoreType.SLIDEME;
import static com.vorlonsoft.android.rate.StoreType.TENCENT;
import static com.vorlonsoft.android.rate.StoreType.YANDEX;
import static com.vorlonsoft.android.rate.Utils.isLollipop;

/**
 * <p>DefaultDialogManager Class - default dialog manager class implements {@link DialogManager}
 * interface of the AndroidRate library.</p>
 * <p>You can extend DefaultDialogManager Class and use
 * {@link AppRate#setDialogManagerFactory(DialogManager.Factory)} if you want to use fully custom
 * dialog (from v7 AppCompat library etc.). DefaultDialogManager Class is thread-safe and a fast
 * singleton implementation inside library, not outside (protected, not private constructor).</p>
 *
 * @since    1.0.2
 * @version  1.2.1
 * @author   Alexander Savin
 * @author   Antoine Vianey
 * @see DefaultDialogManager.Factory
 * @see DialogManager
 */

public class DefaultDialogManager implements DialogManager {
    /** <p>The WeakReference to the {@link DefaultDialogManager} singleton object.</p> */
    private static volatile WeakReference<DefaultDialogManager> singleton = null;
    private final StoreOptions storeOptions;
    private final OnClickButtonListener listener;
    @SuppressWarnings({"WeakerAccess", "UnusedAssignment"})
    protected DialogOptions dialogOptions = null;
    @SuppressWarnings({"WeakerAccess", "UnusedAssignment"})
    protected Context context = null;
    /** <p>Listener used to allow to run some code when the Rate Dialog is shown.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnShowListener showListener = new DialogInterface.OnShowListener() {
        /**
         * <p>This method will be invoked when the Rate Dialog is shown.</p>
         *
         * @param dialog the Rate Dialog that was shown will be passed into the method
         */
        @Override
        public void onShow(DialogInterface dialog) {
            if (dialogOptions.isOrientationChanged()) {
                AppRate.with(context).clearRateDialog();
                AppRate.with(context).setRateDialog(new WeakReference<>((Dialog) dialog));
                if (dialogOptions.getDialogType() != CLASSIC) {
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
            if (isLollipop() && (dialogOptions.getDialogType() == CLASSIC)) {
                try {
                    final Button positiveButton = ((AlertDialog) dialog).getButton(BUTTON_POSITIVE);
                    if (positiveButton != null) {
                        final LinearLayout linearLayout = (LinearLayout) positiveButton.getParent();
                        if ((linearLayout != null) &&
                                (positiveButton.getLeft() + positiveButton.getWidth() > linearLayout.getWidth())) {
                            final Button neutralButton = ((AlertDialog) dialog).getButton(BUTTON_NEUTRAL);
                            final Button negativeButton = ((AlertDialog) dialog).getButton(BUTTON_NEGATIVE);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
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
                    }
                } catch (Exception e) {
                    Log.i(TAG, "Positive button may not fits in the window, can't change layout " +
                            "orientation to vertical.");
                }
            }
        }
    };
    /** <p>Listener used to allow to run some code when the Rate Dialog is dismissed.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
        /**
         * <p>This method will be invoked when the Rate Dialog is dismissed.</p>
         *
         * @param dialog the Rate Dialog that was dismissed will be passed into the method
         */
        @Override
        public void onDismiss(DialogInterface dialog) {
            dialogOptions.setOrientationChanged(true);
            AppRate.with(context).clearRateDialog();
        }
    };
    /** <p>Listener used to notify when the rating on the Rate Dialog has been changed.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final RatingBar.OnRatingBarChangeListener ratingBarListener = new RatingBar.OnRatingBarChangeListener() {
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
            final boolean showNeutralButton = dialogOptions.shouldShowNeutralButton() && (buttonNeutral != null);
            final boolean showNegativeButton = dialogOptions.shouldShowNegativeButton() && (buttonNegative != null);
            final boolean showPositiveButton = buttonPositive != null;

            if (fromUser) {
                dialogOptions.setCurrentRating((byte) rating);
            }

            if (!showNegativeButton && showPositiveButton) {
                buttonPositive.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_bottom);
                buttonPositive.setVisibility(VISIBLE);
            } else if (showNegativeButton && !showPositiveButton) {
                buttonNegative.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_bottom);
                buttonNegative.setVisibility(VISIBLE);
            } else if (showNegativeButton) {
                buttonNegative.setVisibility(VISIBLE);
                buttonPositive.setVisibility(VISIBLE);
            }
            if (showNeutralButton) {
                buttonNeutral.setVisibility(GONE);
            }

            if (layoutRatingBar != null) {
                if ((dialogOptions.shouldShowDialogIcon() && (view.findViewById(R.id.rate_dialog_icon) != null)) ||
                        (dialogOptions.shouldShowTitle() && (view.findViewById(R.id.rate_dialog_text_dialog_title) != null)) ||
                        (dialogOptions.shouldShowMessage() && (view.findViewById(R.id.rate_dialog_text_dialog_message) != null))) {
                    if (showNeutralButton && !(showNegativeButton || showPositiveButton)) {
                        layoutRatingBar.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_bottom);
                    } else if (!showNeutralButton && (showNegativeButton || showPositiveButton)) {
                        layoutRatingBar.setBackgroundResource(R.color.rateDialogColorBackground);
                    }
                } else if (!showNeutralButton && (showNegativeButton || showPositiveButton)) {
                    layoutRatingBar.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_top);
                } else if (showNeutralButton && !(showNegativeButton || showPositiveButton)) {
                    layoutRatingBar.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded);
                }
            }
        }
    };
    /** <p>Listener used to allow to run some code when a button on the Rate Dialog is clicked.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final View.OnClickListener buttonListener = new View.OnClickListener() {
        /**
         * <p>Called when a button has been clicked in the non-{@link DialogType#CLASSIC CLASSIC}
         * Rate Dialog.</p>
         *
         * @param button the button that was clicked.
         */
        @Override
        public void onClick(final View button) {
            final int buttonResId = button.getId();
            if (buttonResId == R.id.rate_dialog_button_positive) {
                positiveListener.onClick(null, BUTTON_POSITIVE);
            } else if (buttonResId == R.id.rate_dialog_button_negative) {
                negativeListener.onClick(null, BUTTON_NEGATIVE);
            } else if (buttonResId == R.id.rate_dialog_button_neutral) {
                neutralListener.onClick(null, BUTTON_NEUTRAL);
            } else {
                Log.w(TAG, LOG_MESSAGE_PART_1 + "dialog button with the given ResId doesn't exist.");
            }
            AppRate.with(context).dismissRateDialog();
        }
    };
    /** <p>Listener used to allow to run some code when a positive button on the Rate Dialog is clicked.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        /**
         * <p>This method will be invoked when a button in the Rate Dialog is clicked.</p>
         *
         * @param dialog the Rate Dialog that received the click
         * @param which the button that was clicked (ex.
         *              {@link DialogInterface#BUTTON_POSITIVE BUTTON_POSITIVE}) or the position
         */
        @Override
        public void onClick(@Nullable final DialogInterface dialog, final int which) {
            final String packageName = AppInformation.getInstance(context).getAppPackageName();
            if ((packageName != null) && (packageName.hashCode() != EMPTY_STRING.hashCode())) {
                final Intent[] intentsToAppStores = getIntentsForStores(packageName);
                try {
                    if (intentsToAppStores.length == 0) {
                        Log.w(TAG, LOG_MESSAGE_PART_1 + "no intent found for startActivity " +
                                   "(intentsToAppStores.length == 0).");
                    } else if (intentsToAppStores[0] == null) {
                        throw new ActivityNotFoundException(LOG_MESSAGE_PART_1 + "no intent found" +
                                " for startActivity (intentsToAppStores[0] == null).");
                    } else {
                        context.startActivity(intentsToAppStores[0]);
                    }
                } catch (ActivityNotFoundException e) {
                    Log.w(TAG, LOG_MESSAGE_PART_1 + "no activity found for " + intentsToAppStores[0], e);
                    final byte intentsToAppStoresNumber = (byte) intentsToAppStores.length;
                    if (intentsToAppStoresNumber > 1) {
                        boolean isCatch;
                        for (byte b = 1; b < intentsToAppStoresNumber; b++) { // intentsToAppStores[1] - second intent in the array
                            try {
                                if (intentsToAppStores[b] == null) {
                                    throw new ActivityNotFoundException(LOG_MESSAGE_PART_1 +
                                            "no intent found for startActivity (intentsToAppStores["
                                            + b + "] == null).");
                                } else {
                                    context.startActivity(intentsToAppStores[b]);
                                }
                                isCatch = false;
                            } catch (ActivityNotFoundException ex) {
                                Log.w(TAG, LOG_MESSAGE_PART_1 + "no activity found for " +
                                        intentsToAppStores[b], ex);
                                isCatch = true;
                            }
                            if (!isCatch) {
                                break;
                            }
                        }
                    }
                }
            } else {
                Log.w(TAG, LOG_MESSAGE_PART_1 + "can't get app package name.");
            }
            setIsAgreeShowDialog(context, false);
            if (listener != null) {
                listener.onClickButton((byte) which);
            }
        }
    };
    /** <p>Listener used to allow to run some code when a negative button on the Rate Dialog is clicked.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        /**
         * <p>This method will be invoked when a button in the Rate Dialog is clicked.</p>
         *
         * @param dialog the Rate Dialog that received the click
         * @param which the button that was clicked (ex.
         *              {@link DialogInterface#BUTTON_POSITIVE BUTTON_POSITIVE}) or the position
         */
        @Override
        public void onClick(@Nullable final DialogInterface dialog, final int which) {
            setIsAgreeShowDialog(context, false);
            if (listener != null) {
                listener.onClickButton((byte) which);
            }
        }
    };
    /** <p>Listener used to allow to run some code when a neutral button on the Rate Dialog is clicked.</p> */
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener neutralListener = new DialogInterface.OnClickListener() {
        /**
         * <p>This method will be invoked when a button in the Rate Dialog is clicked.</p>
         *
         * @param dialog the Rate Dialog that received the click
         * @param which the button that was clicked (ex.
         *              {@link DialogInterface#BUTTON_POSITIVE BUTTON_POSITIVE}) or the position
         */
        @Override
        public void onClick(@Nullable final DialogInterface dialog, final int which) {
            setRemindInterval(context);
            setRemindLaunchesNumber(context);
            if (listener != null) {
                listener.onClickButton((byte) which);
            }
        }
    };

    @SuppressWarnings("WeakerAccess")
    protected DefaultDialogManager(final Context context, final DialogOptions dialogOptions,
                                   final StoreOptions storeOptions) {
        this.context = context;
        this.dialogOptions = dialogOptions;
        this.storeOptions = storeOptions;
        this.listener = dialogOptions.getListener();
    }

    @SuppressLint("SwitchIntDef")
    @NonNull
    private Intent[] getIntentsForStores(@NonNull final String packageName) {
        switch (storeOptions.getStoreType()) {
            case AMAZON:
                return createIntentsForStore(context, AMAZON, packageName);
            case APPLE:
                return createIntentsForStore(context, APPLE, storeOptions.getApplicationId());
            case BAZAAR:
                return createIntentsForStore(context, BAZAAR, packageName);
            case BLACKBERRY:
                return createIntentsForStore(context, BLACKBERRY, storeOptions.getApplicationId());
            case CHINESESTORES:
                return createIntentsForStore(context, CHINESESTORES, packageName);
            case MI:
                return createIntentsForStore(context, MI, packageName);
            case SAMSUNG:
                return createIntentsForStore(context, SAMSUNG, packageName);
            case SLIDEME:
                return createIntentsForStore(context, SLIDEME, packageName);
            case TENCENT:
                return createIntentsForStore(context, TENCENT, packageName);
            case YANDEX:
                return createIntentsForStore(context, YANDEX, packageName);
            case INTENT:
            case OTHER:
                return storeOptions.getIntents();
            default:
                return createIntentsForStore(context, GOOGLEPLAY, packageName);
        }
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
    @Nullable
    protected AlertDialog.Builder getDialogBuilder(@NonNull final Context context, final int themeResId) {
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
    protected void supplyClassicDialogArguments(@NonNull AlertDialog.Builder builder, @NonNull Context dialogContext) {
        if (dialogOptions.shouldShowDialogIcon()) {
            builder.setIcon(dialogOptions.getDialogIcon(dialogContext));
        }
        if (dialogOptions.shouldShowTitle()) {
            builder.setTitle(dialogOptions.getTitleText(context));
        }
        if (dialogOptions.shouldShowMessage()) {
            builder.setMessage(dialogOptions.getMessageText(context));
        }
        if (dialogOptions.shouldShowNeutralButton()) {
            builder.setNeutralButton(dialogOptions.getNeutralText(context), neutralListener);
        }
        if (dialogOptions.shouldShowNegativeButton()) {
            builder.setNegativeButton(dialogOptions.getNegativeText(context), negativeListener);
        }
        builder.setPositiveButton(dialogOptions.getPositiveText(context), positiveListener);
    }

    /**
     * <p>Supplies the arguments to the non-{@link DialogType#CLASSIC CLASSIC} Rate Dialog Builder.</p>
     *
     * @param view the non-{@link DialogType#CLASSIC CLASSIC} Rate Dialog View
     * @param dialogContext a Context for Rate Dialogs created with this View
     */
    @SuppressWarnings("WeakerAccess")
    protected void supplyNonClassicDialogArguments(@NonNull View view, @NonNull Context dialogContext) {
        final View layoutHead = view.findViewById(R.id.rate_dialog_layout_head);
        final View icon = view.findViewById(R.id.rate_dialog_icon);
        final View textDialogTitle = view.findViewById(R.id.rate_dialog_text_dialog_title);
        final View textDialogMessage = view.findViewById(R.id.rate_dialog_text_dialog_message);
        final View layoutRatingBar = view.findViewById(R.id.rate_dialog_layout_rating_bar);
        final View ratingBar = view.findViewById(R.id.rate_dialog_rating_bar);
        final View buttonNeutral = view.findViewById(R.id.rate_dialog_button_neutral);
        final View buttonNegative = view.findViewById(R.id.rate_dialog_button_negative);
        final View buttonPositive = view.findViewById(R.id.rate_dialog_button_positive);
        final boolean showDialogIcon = dialogOptions.shouldShowDialogIcon() && (icon != null);
        final boolean showTitle = dialogOptions.shouldShowTitle() && (textDialogTitle != null);
        final boolean showMessage = dialogOptions.shouldShowMessage() && (textDialogMessage != null);
        final boolean showNeutralButton = dialogOptions.shouldShowNeutralButton() && (buttonNeutral != null);

        if (showDialogIcon) {
            ((ImageView) icon).setImageDrawable(dialogOptions.getDialogIcon(dialogContext));
        } else if (icon != null) {
            icon.setVisibility(GONE);
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

        if (dialogOptions.shouldShowNegativeButton() && (buttonNegative != null)) {
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
                layoutRatingBar.setBackgroundResource(R.drawable.rate_dialog_rectangle_rounded_bottom);
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
    @Nullable
    @Override
    public Dialog createDialog() {

        final AlertDialog.Builder builder = getDialogBuilder(context, dialogOptions.getThemeResId());
        final Context dialogContext;

        if (builder == null) {
            return null;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dialogContext = builder.getContext();
        } else {
            dialogContext = context;
        }

        final View view = dialogOptions.getView(dialogContext);

        if ((dialogOptions.getDialogType() == CLASSIC) || (view == null)) {
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
        @Override
        public DialogManager createDialogManager(final Context context,
                                                 final DialogOptions dialogOptions,
                                                 final StoreOptions storeOptions) {
            if ((singleton == null) || (singleton.get() == null)) {
                synchronized (DefaultDialogManager.class) {
                    if ((singleton == null) || (singleton.get() == null)) {
                        if (singleton != null) {
                            singleton.clear();
                        }
                        singleton = new WeakReference<>(new DefaultDialogManager(context, dialogOptions, storeOptions));
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