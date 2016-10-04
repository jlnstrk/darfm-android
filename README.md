# darfm-android

This project is an Android wrapper for the DAR.fm radio API (UberStations / OnRad.io) written in Kotlin.
All methods are synchronous, so you must access them asynchronously.

[ ![0.1.0](https://jitpack.io/v/julianostarek/darfm-android.svg)](https://jitpack.io/#julianostarek/darfm-android)

## Demo

The demo of this project is on Google Play:

<a href="https://play.google.com/store/apps/details?id=de.julianostarek.android.radiostationsdemo" target="_blank">
  <img alt="Get it on Google Play"
       src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60"/>
</a>

## Importing

Add this to your project-level build.gradle file:

```

allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

```

And this to the module's build.gradle dependencies section:

 ```compile ('com.github.julianostarek:darfm-android:LATEST_RELEASE@aar') { transitive = true } ```

Because this is written in Kotlin, make sure you got the latest version of its Plugin configured in your project. In Android Studio you can just go to Settings/Preferences -> Plugins

## Usage

Example:

```

// Creates a RadioAPI instance that all methods are invoked on
val radioApi = RadioAPI(YOUR_PARTNER_TOKEN)

// Searches for a station with callsign "OMG.fm"
val stationsMatchingCallsign = radioApi.searchStation("OMG.fm")



```