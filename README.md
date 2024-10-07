# obusdk-android-sample

![Static Badge](https://img.shields.io/badge/release-v2.0.0-blue)
[![build](https://github.com/extolta/obusdk-android-sample/actions/workflows/build.yml/badge.svg)](https://github.com/extolta/obusdk-android-sample/actions/workflows/build.yml)

This repository provides a sample app to assist you in integrating the obusdk and its APIs into your
applications.

The Extended OBU Library (EXTOL) enables your applications to connect to the On-Board Unit (OBU) in
a user's car to receive Electronic Road Pricing (ERP) and Traffic related messages. It provides a
set of APIs that you can use in your app to get data from the OBU. The SDK requires Bluetooth to
make a connection, and on Android, it is using Serial Port Profile (SPP).

## Requirements

Please make sure you are using Android Studio version Giraffe or higher.

## Getting Started

To get started with the SDK, you will need an SDK Account key, which can be obtained by registering
at [DataMall](https://datamall.lta.gov.sg/content/datamall/en/request-for-api.html). After
submitting the form, you will receive two emails:

i. The first email contains the DataMall API Access Key, which is to access the DataMall APIs.<br/>
ii. The second email contains the SDK Account Key for the SDK.

Add the SDK key `sdkKey = "<SDK Account Key>"`
in [MainViewModel](app/src/main/java/com/example/obusdk/sampleapp/MainViewModel.kt)

## SDK Integration

To integrate the SDK into your own applications, add the dependency:

```groovy
dependencies {
    implementation "sg.gov.lta:extol:2.0.0"
}
```

and declare the repository in your project:

```groovy
repositories {
    maven {
        url 'https://extol.mycloudrepo.io/public/repositories/extol-android'
    }
}
```

## Changelog

All changes are documented in [CHANGELOG.md](CHANGELOG.md)

## License

The SDK is distributed under the terms of use described in the [License](LICENSE) file in this
repository.
