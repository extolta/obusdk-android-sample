# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2024-09-30

### New Features

- Added support for Android 14 (API level 34).

### Changes

- Renamed `MockedConnectionHandler.Builder.setFastestInterval` to
  `MockedConnectionHandler.Builder.setTimeInterval`.
- Renamed `OBUChargingMessageType.Common` to `OBUChargingMessageType.DetectAlertPointCommonAlert`.
- Removed `roadName` from `OBUChargingInformation`; use `content1` and `content2` to get the road
  name. Please refer to the section 'Common Alert' in the developer guide for more info.
- Removed `cardBalance` from `OBUChargingInformation`; to get the card balance, please refer
  to `balance` in `OBUDataListener.onCardInformation`.
- Minor fixes in the mock manager.
  
## [1.1.1] - 2024-02-19

- Minor fixes

## [1.1.0] - 2023-09-29

- Updated targetSdkVersion to 33
- Minor fixes

## [1.0.1] 

- Added TDCID icon 9011
- Minor fixes

## [1.0.0] 

- Initial Release



