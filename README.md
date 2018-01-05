AndroidRate
============

AndroidRate is a library to help you promote your android app by prompting users to rate the app after using it for a few days. Original project [Android-Rate](https://github.com/hotchemi/Android-Rate) (The MIT License (MIT)) was developed by Shintaro Katafuchi.

![screen shot](http://i.gyazo.com/286342ba215a515f2f443a7ce996cc92.gif)

## Install

You can download from Maven Central.

${latest.version} is ![Maven Badges](https://maven-badges.herokuapp.com/maven-central/com.vorlonsoft/androidrate/badge.svg)

Gradle
```groovy
dependencies {
  implementation 'com.vorlonsoft:androidrate:{latest.version}'
}
```

## Usage

### Configuration

AndroidRate provides methods to configure its behavior.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);

  AppRate.with(this)
      .setStoreType(StoreType.GOOGLEPLAY) //default is GOOGLEPLAY (Google Play), other options are
                                          //           AMAZON (Amazon Appstore) and
                                          //           SAMSUNG (Samsung Galaxy Apps)
      .setInstallDays((byte) 0) // default 10, 0 means install day
      .setLaunchTimes((byte) 3) // default 10
      .setRemindInterval((byte) 2) // default 1
      .setRemindLaunchTimes((byte) 2) // default 1 (each launch)
      .setShowLaterButton(true) // default true
      .setDebug(false) // default false
      //Java 8+: .setOnClickButtonListener(which -> Log.d(MainActivity.class.getName(), Byte.toString(which)))
      .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
          @Override
          public void onClickButton(byte which) {
              Log.d(MainActivity.class.getName(), Byte.toString(which));
          }
      })
      .monitor();

  if (AppRate.with(this).getStoreType() == StoreType.GOOGLEPLAY) {
      //Check that Google Play is available
      if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) {
          // Show a dialog if meets conditions
          AppRate.showRateDialogIfMeetsConditions(this);
      }
  } else {
      // Show a dialog if meets conditions
      AppRate.showRateDialogIfMeetsConditions(this);
  }
}
```

The default conditions to show rate dialog is as below:

1. App is launched more than 10 days later than installation. Change via `AppRate#setInstallDays(byte)`.
2. App is launched more than 10 times. Change via `AppRate#setLaunchTimes(byte)`.
3. App is launched more than 1 days after neutral button clicked. Change via `AppRate#setRemindInterval(byte)`.
4. App is launched X times and X % 1 = 0. Change via `AppRate#setRemindLaunchTimes(byte)`.
5. App shows neutral dialog (Remind me later) by default. Change via `setShowLaterButton(boolean)`.
6. To specify the callback when the button is pressed. The same value as the second argument of `DialogInterface.OnClickListener#onClick` will be passed in the argument of `onClickButton`.
7. Setting `AppRate#setDebug(boolean)` will ensure that the rating request is shown each time the app is launched. **This feature is only for development!**.

### Optional custom event requirements for showing dialog

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

### When the button presses on

call `AppRate#showRateDialog(Activity)`.

```java
AppRate.with(this).showRateDialog(this);
```

### Set custom view

call `AppRate#setView(View)`.

```java
LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
View view = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));
AppRate.with(this).setView(view).monitor();
```

### Specific theme

You can use a specific theme to inflate the dialog.

```java
AppRate.with(this).setThemeResId(int);
```

### Custom dialog

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

### Check that Google Play is available

```java
if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) {

}
```

## Language

AndroidRate currently supports the following languages:

- Arabic
- Basque
- Catalan
- Chinese (zh-CN, zh-HK, zh-MO, zh-SG, zh-TW)
- Czech
- English
- French
- German
- Hebrew
- Indonesian
- Italy
- Japanese
- Korean
- Persian
- Polish
- Portuguese
- Russian
- Spanish
- Turkish
- Ukrainian
- Vietnamese

## Support

AndroidRate supports API level 9 and up.

## Sample

Please try to move the [sample](https://github.com/Vorlonsoft/AndroidRate/tree/master/sample).

## Already in use in following apps

* [I Love You Pro Edition](https://play.google.com/store/apps/details?id=com.vorlonsoft.iluvu)

* [I Love You Free Edition](https://play.google.com/store/apps/details?id=com.vorlonsoft.iloveyou)

* [Rossiya.pro Mail](https://play.google.com/store/apps/details?id=com.vorlonsoft.rossiyapro)

* ...

## Contribute

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
