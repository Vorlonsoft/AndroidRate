AndroidRateKotlin
=================

AndroidRateKotlin is a Kotlin library to help you promote your android app by prompting users to rate the app after using it for a few days. Original project [AndroidRate](https://github.com/Vorlonsoft/AndroidRate) (The MIT License (MIT)) was developed by Vorlonsoft LLC.

![screen shot](http://i.gyazo.com/286342ba215a515f2f443a7ce996cc92.gif)

## Install

You can download from Maven Central.

${latest.version} is ![Maven Badges](https://maven-badges.herokuapp.com/maven-central/com.vorlonsoft/android-rate/badge.svg)

Gradle
```groovy
dependencies {
  implementation 'com.vorlonsoft:android-rate:{latest.version}'
}
```

## Usage

### Configuration

AndroidRateKotlin provides methods to configure its behavior.

```Kotlin
override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContentView(R.layout.activity_main)

  val storeType = StoreType.GOOGLEPLAY // options: GOOGLEPLAY or AMAZON

  AppRate.with(this)
      ?.setStoreType(storeType) //default is GOOGLEPLAY, other option is AMAZON
      ?.setInstallDays(0) // default 10, 0 means install day
      ?.setLaunchTimes(3) // default 10
      ?.setRemindInterval(2) // default 1
      ?.setRemindLaunchTimes(2) // default 1 (each launch)
      ?.setShowLaterButton(true) // default true
      ?.setDebug(false) // default false
      ?.monitor()

  if (storeType == StoreType.GOOGLEPLAY) {
    //Check that Google Play is available
    if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) {
      // Show a dialog if meets conditions
      AppRate.showRateDialogIfMeetsConditions(this)
    }
  } else {
    // Show a dialog if meets conditions
    AppRate.showRateDialogIfMeetsConditions(this)
  }
}
```

The default conditions to show rate dialog is as below:

1. App is launched more than 10 days later than installation. Change via `AppRate#setInstallDays(Int)`.
2. App is launched more than 10 times. Change via `AppRate#setLaunchTimes(Int)`.
3. App is launched more than 1 days after neutral button clicked. Change via `AppRate#setRemindInterval(Int)`.
4. App is launched X times and X % 1 = 0. Change via `AppRate#setRemindLaunchTimes(Int)`.
5. App shows neutral dialog (Remind me later) by default. Change via `setShowLaterButton(Boolean)`.
6. Setting `AppRate#setDebug(Boolean)` will ensure that the rating request is shown each time the app is launched. **This feature is only for development!**.

### Optional custom event requirements for showing dialog

You can add additional optional requirements for showing dialog. Each requirement can be added/referenced as a unique string. You can set a minimum count for each such event (for e.g. "action_performed" 3 times, "button_clicked" 5 times, etc.)

```Kotlin
AppRate.with(this)?.setMinimumEventCount(String, Long)
AppRate.with(this)?.incrementEventCount(String)
AppRate.with(this)?.setEventCountValue(String, Long)
```

### Clear show dialog flag

When you want to show the dialog again, call `AppRate#clearAgreeShowDialog()`.

```Kotlin
AppRate.with(this)?.clearAgreeShowDialog()
```

### When the button presses on

call `AppRate#showRateDialog(Activity)`.

```Kotlin
AppRate.with(this)?.showRateDialog(this)
```

### Set custom view

call `AppRate#setView(View)`.

```Kotlin
internal var inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
internal var view = inflater.inflate(R.layout.custom_dialog, findViewById(R.id.layout_root) as ViewGroup)
AppRate.with(this)?.setView(view)?.monitor()
```

### Specific theme

You can use a specific theme to inflate the dialog.

```Kotlin
AppRate.with(this)?.setThemeResId(Integer)
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

```Kotlin
if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) {

}
```

## Language

AndroidRateKotlin currently supports the following languages:

- Arabic
- Basque
- Catalan
- Chinese (zh-CN, zh-HK, zh-MO, zh-SG, zh-TW)
- Czech
- English
- French
- German
- Hebrew
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

AndroidRateKotlin supports API level 9 and up.

## Sample

Please try to move the [sample](https://github.com/AlexanderLS/AndroidRateKotlin/tree/master/sample).

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
