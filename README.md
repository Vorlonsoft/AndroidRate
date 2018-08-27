[![AndroidRate Logo](https://raw.githubusercontent.com/Vorlonsoft/AndroidRate/master/logo/152px.png)](#) [![Get automatic notifications about new "AndroidRate library" versions](https://www.bintray.com/docs/images/bintray_badge_color.png)](https://bintray.com/bintray/jcenter/com.vorlonsoft%3Aandroidrate?source=watch)

# AndroidRate [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidRate-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7084) [![Latest Version](https://api.bintray.com/packages/bintray/jcenter/com.vorlonsoft%3Aandroidrate/images/download.svg)](https://bintray.com/bintray/jcenter/com.vorlonsoft%3Aandroidrate/_latestVersion)

AndroidRate is a library to help you promote your Android app by prompting users to rate the app after using it for a few days. Project based on [Android-Rate](https://github.com/hotchemi/Android-Rate) by Shintaro Katafuchi.

[![AndroidRate animated screenshots](https://raw.githubusercontent.com/Vorlonsoft/AndroidRate/master/screenshots/screenshots_360x640.gif)](#)

## Contents

* [Install](#install)
* [Usage](#usage)
  * [Minimal Configuration](#minimal-configuration)
  * [Configuration](#configuration)
  * [Custom event requirements](#optional-custom-event-requirements)
  * [Clear show dialog flag](#clear-show-dialog-flag)
  * [Forced Rate Dialog](#forced-display-of-the-rate-dialog)
  * [Custom view](#set-custom-view)
  * [Custom theme](#specific-theme)
  * [Custom dialog labels](#custom-dialog-labels)
  * [Appstores](#appstores)
  * [Сustom Intents](#custom-intents)
  * [Availability of Google Play](#check-the-availability-of-google-play)
* [Languages](#languages)
* [Supported APIs](#supported-apis)
* [Sample](#sample)
* [Javadoc documentation](#javadoc-documentation)
* [Already in use](#already-in-use-in-following-apps)
* [Contribute](#contribute)
* [License](#license)

## Install

You can download library files from JCenter, Maven Central or GitHub.

`latestVersion` is [![Latest Version](https://api.bintray.com/packages/bintray/jcenter/com.vorlonsoft%3Aandroidrate/images/download.svg)](https://bintray.com/bintray/jcenter/com.vorlonsoft%3Aandroidrate/_latestVersion)

Add the following in your app's `build.gradle` file:

```groovy
dependencies {
    implementation "com.vorlonsoft:androidrate:${latestVersion}"
}
```

## Usage

### Minimal Configuration

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  AppRate.with(this)
      .setInstallDays((byte) 0)           // default 10, 0 means install day, 10 means app is launched more than 10 days later than installation
      .setLaunchTimes((byte) 3)           // default 10, 3 means app is launched more than 3 times
      .setRemindInterval((byte) 1)        // default 1, 1 means app is launched more than 1 day after neutral button clicked
      .monitor();

  AppRate.showRateDialogIfMeetsConditions(this);
}
```

### Configuration

AndroidRate library provides methods to configure its behavior.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  AppRate.with(this)
      .setStoreType(StoreType.GOOGLEPLAY) /* default GOOGLEPLAY (Google Play), other options are AMAZON (Amazon Appstore), BAZAAR (Cafe Bazaar),
                                           *         CHINESESTORES (19 chinese app stores), MI (Mi Appstore (Xiaomi Market)), SAMSUNG (Samsung Galaxy Apps),
                                           *         SLIDEME (SlideME Marketplace), TENCENT (Tencent App Store), YANDEX (Yandex.Store),
                                           *         setStoreType(BLACKBERRY, long) (BlackBerry World, long - your application ID),
                                           *         setStoreType(APPLE, long) (Apple App Store, long - your application ID) and
                                           *         setStoreType(String) (Any other store, String - a full URI to your app) */
      .setInstallDays((byte) 0)           // default 10, 0 means install day
      .setLaunchTimes((byte) 3)           // default 10
      .setRemindInterval((byte) 2)        // default 1
      .setRemindLaunchTimes((byte) 2)     // default 1 (each launch)
      .setShowLaterButton(true)           // default true
      .set365DayPeriodMaxNumberDialogLaunchTimes((short) 3) // default Short.MAX_VALUE, Short.MAX_VALUE means unlimited occurrences within a 365-day period
      .setDebug(false)                    // default false
      .setOnClickButtonListener(which -> Log.d(MainActivity.class.getName(), Byte.toString(which))) // Java 8+, change for Java 7-
      .monitor();

  if (AppRate.with(this).getStoreType() == StoreType.GOOGLEPLAY) {
      if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) { // Check that Google Play is available
          AppRate.showRateDialogIfMeetsConditions(this); // Show a dialog if meets conditions
      }
  } else {
      AppRate.showRateDialogIfMeetsConditions(this); // Show a dialog if meets conditions
  }
}
```

The default conditions to show rate dialog is as below:

1. Google Play is launched when the positive button is pressed. Change via `AppRate#setStoreType(int)`.
2. App is launched more than 10 days later than installation. Change via `AppRate#setInstallDays(byte)`.
3. App is launched more than 10 times. Change via `AppRate#setLaunchTimes(byte)`.
4. App is launched more than 1 days after neutral button clicked. Change via `AppRate#setRemindInterval(byte)`.
5. App is launched X times and X % 1 = 0. Change via `AppRate#setRemindLaunchTimes(byte)`.
6. App shows neutral dialog (Remind me later) by default. Change via `setShowLaterButton(boolean)`.
7. Maximum number of the display of the dialog within a 365-day period is less than 3. Change via `AppRate#set365DayPeriodMaxNumberDialogLaunchTimes(short)`.
8. Setting `AppRate#setDebug(boolean)` will ensure that the rating request is shown each time the app is launched. **This feature is only for development!**.
9. To specify the callback when the button is pressed. The same value as the second argument of `DialogInterface.OnClickListener#onClick` will be passed in the argument of `onClickButton`.

### Optional custom event requirements

You can add additional optional requirements for showing dialog. Each requirement can be added/referenced as a unique string. You can set a minimum count for each such event (for e.g. "action_performed" 3 times, "button_clicked" 5 times, etc.)

```java
AppRate.with(this).setMinimumEventCount(String, short);
AppRate.with(this).incrementEventCount(String);
AppRate.with(this).setEventCountValue(String, short);
```

### Clear show dialog flag

When you want to show the dialog again, call `AppRate#clearAgreeShowDialog()`.

```java
AppRate.with(this).clearAgreeShowDialog();
```

### Forced display of the Rate Dialog

Use this method directly if you want to force display of the Rate Dialog, for example when some button presses on. Call `AppRate#showRateDialog(Activity)`.

```java
AppRate.with(this).showRateDialog(this);
```

### Set custom view

Call `AppRate#setView(View)`.

```java
LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
View view = inflater.inflate(R.layout.custom_dialog, (ViewGroup) findViewById(R.id.layout_root));
AppRate.with(this).setView(view);
```

### Specific theme

You can use a specific theme to inflate the dialog.

```java
AppRate.with(this).setThemeResId(int);
```

### Custom dialog labels

If you want to use your own dialog labels, override string xml resources on your application.

```xml
<resources>
    <string name="rate_dialog_title">Rate this app</string>
    <string name="rate_dialog_message">If you enjoy playing this app, would you mind taking a moment to rate it? It won\'t take more than a minute. Thanks for your support!</string>
    <string name="rate_dialog_ok">Rate It Now</string>
    <string name="rate_dialog_cancel">Remind Me Later</string>
    <string name="rate_dialog_no">No, Thanks</string>
</resources>
```

### Appstores

You can use different app stores.

#### Google Play, Amazon Appstore, Cafe Bazaar, Mi Appstore (Xiaomi Market), Samsung Galaxy Apps, SlideME Marketplace, Tencent App Store, Yandex.Store

```java
AppRate.with(this).setStoreType(StoreType.GOOGLEPLAY); // Google Play
AppRate.with(this).setStoreType(StoreType.AMAZON);     // Amazon Appstore
AppRate.with(this).setStoreType(StoreType.BAZAAR);     // Cafe Bazaar
AppRate.with(this).setStoreType(StoreType.MI);         // Mi Appstore (Xiaomi Market)
AppRate.with(this).setStoreType(StoreType.SAMSUNG);    // Samsung Galaxy Apps
AppRate.with(this).setStoreType(StoreType.SLIDEME);    // SlideME Marketplace
AppRate.with(this).setStoreType(StoreType.TENCENT);    // Tencent App Store
AppRate.with(this).setStoreType(StoreType.YANDEX);     // Yandex.Store
```

#### Apple App Store

```java
/* Apple App Store, long - your Apple App Store application ID
 * e. g. 284882215 for Facebook (https://itunes.apple.com/app/id284882215) */
AppRate.with(this).setStoreType(StoreType.APPLE, long);
```

#### BlackBerry World

```java
/* BlackBerry World, long - your BlackBerry World application ID
 * e. g. 50777 for Facebook (https://appworld.blackberry.com/webstore/content/50777) */
AppRate.with(this).setStoreType(StoreType.BLACKBERRY, long);
```

#### Chinese app stores

The first Chinese app store found on the user device will be used, if first fails, second will be used, etc. The Library doesn't check the availability of your application on the app store.

```java
/* 19 chinese app stores: 腾讯应用宝, 360手机助手, 小米应用商店, 华为应用商店, 百度手机助手,
 * OPPO应用商店, 中兴应用商店, VIVO应用商店, 豌豆荚, PP手机助手, 安智应用商店, 91手机助手,
 * 应用汇, QQ手机管家, 机锋应用市场, GO市场, 宇龙Coolpad应用商店, 联想应用商店, cool市场 */
AppRate.with(this).setStoreType(StoreType.CHINESESTORES);
```

#### Other store

```java
/* Any other store/stores,
 * String... - an RFC 2396-compliant URI or array of URIs to your app,
 * e. g. "https://otherstore.com/app?id=com.yourapp"
 * or "otherstore://apps/com.yourapp" */
AppRate.with(this).setStoreType(String...);
```

### Custom Intents

You can set custom action to the Rate button. For example, you want to open your custom RateActivity when the Rate button clicked.

```java
/* Any custom intents, Intent... - intent or array of intents,
 * first will be executed (startActivity(intents[0])), if first fails,
 * second will be executed (startActivity(intents[1])), etc. */
AppRate.with(this).setStoreType(Intent...);
```

### Check the availability of Google Play

```java
if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) {
    // ...
}
```

## Languages

AndroidRate currently supports the following languages:

- Albanian
- Arabic
- Azerbaijani
- Basque
- Benqali
- Bulgarian
- Catalan
- Chinese (zh-CN, zh-TW)
- Croatian
- Czech
- Danish
- Dutch
- English
- Finnish
- French
- German
- Greek
- Hebrew
- Hungarian
- Indonesian
- Italy
- Japanese
- Korean
- Persian
- Polish
- Portuguese
- Russian
- Serbian
- Slovak
- Slovenian
- Spanish
- Swedish
- Thai
- Turkish
- Ukrainian
- Vietnamese

## Supported APIs

AndroidRate library supports API level 9 and up.

## Sample

Please try to move the [sample](https://github.com/Vorlonsoft/AndroidRate/tree/master/sample).

## Javadoc documentation

See [AndroidRate documentation](https://vorlonsoft.github.io/AndroidRate/javadoc/)

## Already in use in following apps

* [I Love You Free Edition](https://play.google.com/store/apps/details?id=com.vorlonsoft.iloveyou)

* [Rossiya.pro Mail](https://play.google.com/store/apps/details?id=com.vorlonsoft.rossiyapro)

* ...

## Contribute

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

## License

    The MIT License (MIT)

    Copyright (c) 2017 - 2018 Vorlonsoft LLC

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.