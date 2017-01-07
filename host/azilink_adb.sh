#!/bin/bash -x

echo "Azilink ADB Script v2.0.5"

# Validate ADB is operable before we start
if ! adb get-state; then echo "No Android connected"; exit 255; fi

## Start gateway
###
adb wait-for-device
# Start Azilink UI
adb shell am start org.lfx.azilink/.MainActivity
sleep 2
# Start Azilink service
adb shell am startservice -n org.lfx.azilink/.ForwardService
sleep 2
adb forward tcp:41927 tcp:41927

## Start Pseudo-VPN
###
sudo openvpn azilink_adb.ovpn

## Stop gateway
###
adb wait-for-device
# Stop Azilink service
adb shell am stopservice -n org.lfx.azilink/.ForwardService
