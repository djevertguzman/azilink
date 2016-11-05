#!/bin/bash
echo "Azilink Tether Script V1"
echo "Starting..."
sleep 2s
#Check if script is being run as root
(( EUID != 0 )) && exec sudo -- "$0" "$@"
adb get-state
adb wait-for-device
adb forward tcp:41927 tcp:41927
sudo sh -c "echo nameserver 192.168.56.1 >> /etc/resolv.conf"
sudo openvpn Azilink/azilink.ovpn
