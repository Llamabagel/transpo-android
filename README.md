# Transpo-Android \[🚧 Under Construction 🚧\]
The open source project behind Route 613. Next stop: 2.0!

[![CircleCI](https://circleci.com/gh/Llamabagel/transpo-android.svg?style=svg)](https://circleci.com/gh/Llamabagel/transpo-android)
[![codecov](https://codecov.io/gh/Llamabagel/transpo-android/branch/master/graph/badge.svg)](https://codecov.io/gh/Llamabagel/transpo-android)

This is a complete open source rewrite of Route 613. The main objective of this rewrite is to adopt a better architecture to improve the reliability of the app and make it easier to add new features.

## Development Setup
Development on this project requires Android Studio 3.4 or newer, as well as the Android SDK for Android P (28).

The built setup requires a `config.properties` file to be created in the root directory of the project that includes configuration information for the API backend and for using the Mapbox SDK.  
The configuration must include:
* `api.endpoint` - The base URL of the API endpoint being used. This would direct towards a hosted instance of [transpo-server](https://github.com/dellisd/transpo-server).
* `mapbox.key` - An API Access Token for using the Mapbox SDK. [You can get one from Mapbox.](https://docs.mapbox.com/help/how-mapbox-works/access-tokens/)
