[![AndroidRate Logo](https://raw.githubusercontent.com/Vorlonsoft/AndroidRate/master/logo/152px.png)](#)

# AndroidRate [![Build Status](https://raw.githubusercontent.com/Vorlonsoft/AndroidRate/master/badges/build-status_90x20.png)](#) [![Latest Version](https://raw.githubusercontent.com/Vorlonsoft/AndroidRate/master/badges/latest-version_104x20.png)](https://github.com/Vorlonsoft/AndroidRate/releases) [![Supported APIs](https://raw.githubusercontent.com/Vorlonsoft/AndroidRate/master/badges/api_54x20.png)](#) [![Android Arsenal](https://raw.githubusercontent.com/Vorlonsoft/AndroidRate/master/badges/android-arsenal_174x20.png)](#) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/c1691c3e199c45e9834f880b70823d4a)](https://www.codacy.com/app/Vorlonsoft/AndroidRate?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Vorlonsoft/AndroidRate&amp;utm_campaign=Badge_Grade)

AndroidRate is a library to help you promote your Android app by
prompting users to rate the app after using it for a few days. Project
based on [Android-Rate](https://github.com/hotchemi/Android-Rate) by
Shintaro Katafuchi.

[![AndroidRate animated screenshots](https://raw.githubusercontent.com/Vorlonsoft/AndroidRate/master/screenshots/screenshots_360x640.gif)](#)

## Contents

* [Install](#install)
* [Usage](#usage)
  * [Configuration](#configuration)
  * [OnClickButtonListener interface](#onclickbuttonlistener-interface)
  * [Custom event requirements](#optional-custom-event-requirements)
  * [Clear show dialog flag](#clear-show-dialog-flag)
  * [Forced Rate Dialog](#forced-display-of-the-rate-dialog)
  * [Forced dismiss of the Dialog](#forced-dismiss-of-the-rate-dialog)
  * [Custom view](#set-custom-view)
  * [Custom theme](#specific-theme)
  * [Custom dialog labels](#custom-dialog-labels)
  * [Appstores](#appstores)
  * [Сustom intents](#custom-intents)
  * [Check for Google Play](#check-for-google-play)
* [Sample](#sample)
* [Javadoc Documentation](#javadoc-documentation)
* [Supported Languages](#supported-languages)
* [Already in Use](#already-in-use)
* [Contribute](#contribute)
* [License](#license)

## Install

### Latest stable version

You can download library files from JCenter, Maven Central, JFrog
Bintray (JCenter mirror),
**[GitHub](https://github.com/Vorlonsoft/AndroidRate) (main source)**,
GitLab (GitHub mirror) or SourceForge (GitHub mirror).

`latestVersion` is [![Latest Version](https://raw.githubusercontent.com/Vorlonsoft/AndroidRate/master/badges/latest-version_104x20.png)](https://github.com/Vorlonsoft/AndroidRate/releases)

Add the following in your app's `build.gradle` file:

```groovy
dependencies {
    implementation "com.vorlonsoft:androidrate:${latestVersion}"
}
```

### Latest snapshot version

If you don't want to wait for the next release, you can add the
Sonatype's snapshots repository to your project root `build.gradle`
file:

```groovy
repositories {
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}
```

Then add the following dependency to your relevant project modules
`build.gradle` files:

```groovy
dependencies {
    implementation "com.vorlonsoft:androidrate:1.2.5-SNAPSHOT"
}
```

## Usage

### Configuration

AndroidRate library provides methods to configure it's behavior. Select
the type of configuration that best describes your needs.

#### Nano configuration

Uses library's defaults.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  AppRate.quickStart(this); // Monitors the app launch times and shows the Rate Dialog when default conditions are met
}
```

#### Micro configuration

Configures basic library behavior only.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  AppRate.with(this)
      .setInstallDays((byte) 0)                  // default is 10, 0 means install day, 10 means app is launched 10 or more days later than installation
      .setLaunchTimes((byte) 3)                  // default is 10, 3 means app is launched 3 or more times
      .setRemindInterval((byte) 1)               // default is 1, 1 means app is launched 1 or more days after neutral button clicked
      .setRemindLaunchesNumber((byte) 1)         // default is 0, 1 means app is launched 1 or more times after neutral button clicked
      .monitor();                                // Monitors the app launch times
  AppRate.showRateDialogIfMeetsConditions(this); // Shows the Rate Dialog when conditions are met
}
```

#### Standard configuration

The choice of most corporate developers.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  AppRate.with(this)
      .setStoreType(StoreType.GOOGLEPLAY) /* default is GOOGLEPLAY (Google Play), other options are AMAZON (Amazon Appstore), BAZAAR (Cafe Bazaar),
                                           *         CHINESESTORES (19 chinese app stores), MI (Mi Appstore (Xiaomi Market)), SAMSUNG (Samsung Galaxy Apps),
                                           *         SLIDEME (SlideME Marketplace), TENCENT (Tencent App Store), YANDEX (Yandex.Store),
                                           *         setStoreType(BLACKBERRY, long) (BlackBerry World, long - your application ID),
                                           *         setStoreType(APPLE, long) (Apple App Store, long - your application ID),
                                           *         setStoreType(String...) (Any other store/stores, String... - an URI or array of URIs to your app) and
                                           *         setStoreType(Intent...) (Any custom intent/intents, Intent... - an intent or array of intents) */
      .setTimeToWait(Time.DAY, (short) 0) // default is 10 days, 0 means install millisecond, 10 means app is launched 10 or more time units later than installation
      .setLaunchTimes((byte) 3)           // default is 10, 3 means app is launched 3 or more times
      .setRemindTimeToWait(Time.DAY, (short) 2) // default is 1 day, 1 means app is launched 1 or more time units after neutral button clicked
      .setRemindLaunchesNumber((byte) 1)  // default is 0, 1 means app is launched 1 or more times after neutral button clicked
      .setSelectedAppLaunches((byte) 1)   // default is 1, 1 means each launch, 2 means every 2nd launch, 3 means every 3rd launch, etc
      .setShowLaterButton(true)           // default is true, true means to show the Neutral button ("Remind me later").
      .set365DayPeriodMaxNumberDialogLaunchTimes((short) 3) // default is unlimited, 3 means 3 or less occurrences of the display of the Rate Dialog within a 365-day period
      .setVersionCodeCheck(true)          // default is false, true means to re-enable the Rate Dialog if a new version of app with different version code is installed
      .setVersionNameCheck(true)          // default is false, true means to re-enable the Rate Dialog if a new version of app with different version name is installed
      .setDebug(false)                    // default is false, true is for development only, true ensures that the Rate Dialog will be shown each time the app is launched
      .setOnClickButtonListener(which -> Log.d(this.getLocalClassName(), Byte.toString(which))) // Java 8+, change for Java 7-
      .monitor();                         // Monitors the app launch times

  if (AppRate.with(this).getStoreType() == StoreType.GOOGLEPLAY) { // Checks that current app store type from library options is StoreType.GOOGLEPLAY
      if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) { // Checks that Google Play is available
          AppRate.showRateDialogIfMeetsConditions(this); // Shows the Rate Dialog when conditions are met
      }
  } else {
      AppRate.showRateDialogIfMeetsConditions(this);     // Shows the Rate Dialog when conditions are met
  }
}
```

Default options of the Rate Dialog are as below:

1. Google Play launches when you press the positive button. Change via `AppRate#setStoreType(int)`, `AppRate#setStoreType(int, long)`, `AppRate#setStoreType(String...)` or `AppRate#setStoreType(Intent...)`.
2. App is launched 10 or more days later than installation. Change via `AppRate#setTimeToWait(long, short)` or `AppRate#setInstallDays(byte)`.
3. App is launched 10 or more times. Change via `AppRate#setLaunchTimes(byte)`.
4. App is launched 1 or more days after neutral button clicked. Change via `AppRate#setRemindTimeToWait(long, short)` or `AppRate#setRemindInterval(byte)`.
5. App is launched 0 or more times after neutral button clicked. Change via `AppRate#setRemindLaunchesNumber(byte)`.
6. Each launch (the condition is satisfied if appLaunches % `param` == 0). Change via `AppRate#setSelectedAppLaunches(byte)`.
7. App shows the Neutral button ("Remind me later"). Change via `setShowLaterButton(boolean)`.
8. Unlimited occurrences of the display of the Rate Dialog within a 365-day period. Change via `AppRate#set365DayPeriodMaxNumberDialogLaunchTimes(short)`.
9. Don't re-enable the Rate Dialog if a new version of app with different version code is installed. Change via `AppRate#setVersionCodeCheck(boolean)`.
10. Don't re-enable the Rate Dialog if a new version of app with different version name is installed. Change via `AppRate#setVersionNameCheck(boolean)`.
11. Setting `AppRate#setDebug(boolean)` to `true` ensures that the Rate Dialog will be shown each time the app is launched. **This feature is for development only!**.
12. There is no default callback when the button of Rate Dialog is pressed. Change via `AppRate.with(this).setOnClickButtonListener(OnClickButtonListener)`.

### OnClickButtonListener interface

You can implement OnClickButtonListener Interface and use
`AppRate.with(this).setOnClickButtonListener(OnClickButtonListener)` to
specify the callback when the button of Rate Dialog is pressed.
`DialogInterface.BUTTON_POSITIVE`, `DialogInterface.BUTTON_NEUTRAL` or
`DialogInterface.BUTTON_NEGATIVE` will be passed in the argument of
`OnClickButtonListener#onClickButton`.

```java
// Java 7- start
AppRate.with(this).setOnClickButtonListener(new OnClickButtonListener() {
    @Override
    public void onClickButton(final byte which) {
        // Do something
    }
})
// Java 7- end
// Java 8+ start
AppRate.with(this).setOnClickButtonListener(which -> {
    // Do something
})
// Java 8+ end
```

### Optional custom event requirements

You can add additional optional requirements for showing dialog. Each
requirement can be added/referenced as a unique string. You can set a
minimum count for each such event (for e.g. "action_performed" 3 times,
"button_clicked" 5 times, etc.)

```java
AppRate.with(this).setMinimumEventCount(String, short);
AppRate.with(this).incrementEventCount(String);
AppRate.with(this).setEventCountValue(String, short);
```

### Clear show dialog flag

When you want to show the dialog again, call
`AppRate#clearAgreeShowDialog()`.

```java
AppRate.with(this).clearAgreeShowDialog();
```

### Forced display of the Rate Dialog

Use this method directly if you want to force display of the Rate
Dialog. Call it when some button presses on. Method also useful for
testing purposes. Call `AppRate#showRateDialog(Activity)`.

```java
AppRate.with(this).showRateDialog(this);
```

### Forced dismiss of the Rate Dialog

Use this method directly if you want to remove the Rate Dialog from the
screen. Call `AppRate#dismissRateDialog()`.

```java
AppRate.with(this).dismissRateDialog();
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

If you want to use your own dialog labels, override string xml resources
on your application.

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

The first Chinese app store found on the user device will be used, if
first fails, second will be used, etc. The Library doesn't check the
availability of your application on the app store.

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

### Custom intents

You can set custom action to the Positive button. For example, you want
to open your custom RateActivity when the Rate button clicked.

```java
/* Any custom intent/intents, Intent... - an intent or array of intents,
 * first will be executed (startActivity(intents[0])), if first fails,
 * second will be executed (startActivity(intents[1])), etc. */
AppRate.with(this).setStoreType(Intent...);
```

### Check for Google Play

The following code checks that Google Play is available on the user's
device. We recommend to use it if current app store type from library
options is StoreType.GOOGLEPLAY.

```java
if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) {
    // ...
}
```

## Sample

Clone this repo and check out the
[sample](https://github.com/Vorlonsoft/AndroidRate/tree/master/sample)
module.

## Javadoc Documentation

See generated javadoc
[AndroidRate documentation](https://vorlonsoft.github.io/AndroidRate/javadoc/).

## Supported Languages

AndroidRate library currently supports the following 41 languages:

* Albanian
* Arabic
* Azerbaijani
* Basque
* Benqali
* Bulgarian
* Catalan
* Chinese (zh-CN, zh-TW)
* Croatian
* Czech
* Danish
* Dutch
* English
* Finnish
* French
* German
* Greek
* Hebrew
* Hindi
* Hungarian
* Indonesian
* Italy
* Japanese
* Korean
* Malay
* Norwegian
* Persian
* Polish
* Portuguese
* Romanian
* Russian
* Serbian
* Slovak
* Slovenian
* Spanish
* Swedish
* Thai
* Turkish
* Ukrainian
* Vietnamese

## Already in Use

AndroidRate library already in use in following apps:

* [I Love You Free Edition](https://play.google.com/store/apps/details?id=com.vorlonsoft.iloveyou)
* [Rossiya.pro Mail](https://play.google.com/store/apps/details?id=com.vorlonsoft.rossiyapro)
* ...

## Contribute

1. Fork it.
2. Create your feature branch (`git checkout -b my-new-feature`).
3. Commit your changes (`git commit -am 'Added some feature'`).
4. Push to the branch (`git push origin my-new-feature`).
5. Create new Pull Request.

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
