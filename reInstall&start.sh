#!/usr/bin/env bash

adb install -r ././app/build/outputs/apk/app*debug.apk
adb shell am start -n com.m3gv.news/com.m3gv.news.business.homepage.HomeActivity