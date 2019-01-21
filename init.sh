#!/bin/bash

./gradlew clean :plugin:assembleDebug
adb shell mkdir sdcard/com.knight.plugin
adb shell rm sdcard/com.knight.plugin/plugin-debug.apk
adb push ./plugin/build/outputs/apk/debug/plugin-debug.apk sdcard/com\.knight\.plugin/
