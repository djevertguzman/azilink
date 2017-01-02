#!/bin/bash -x

echo "Azilink Tether Script v2.0.4"

# Find more details about USB tethering method number (33 for Nougat) at:
# http://android.stackexchange.com/questions/29954/is-it-possible-to-activate-the-usb-tethering-android-setting-from-the-command

if ! adb get-state; then echo "No Android connected"; exit 255; fi

## Stop gateway
#adb wait-for-device
## Disable USB tethering
##adb shell su -c "\"service call connectivity 33 i32 0\""
##adb wait-for-device
## Stop Azilink service
##adb shell am stopservice -n org.lfx.azilink/.ForwardService
#sleep 2

## Start gateway
# Enable USB tethering
adb shell su -c "\"service call connectivity 33 i32 1\""
adb wait-for-device
# Start Azilink UI
adb shell am start org.lfx.azilink/.MainActivity
sleep 2
# Start Azilink service
adb shell am startservice -n org.lfx.azilink/.ForwardService
sleep 2
adb forward tcp:41927 tcp:41927

## Start Pseudo-VPN
#sudo sh -c "echo nameserver 192.168.56.1 >> /etc/resolv.conf"
sudo openvpn azilink.ovpn

## Stop gateway
adb wait-for-device
# Disable USB tethering
adb shell su -c "\"service call connectivity 33 i32 0\""
adb wait-for-device
# Stop Azilink service
adb shell am stopservice -n org.lfx.azilink/.ForwardService
