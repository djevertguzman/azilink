#!/bin/bash -x

echo "Azilink Network Script v2.0.5"

# Validate Azilink server is available before we start
if ! timeout 1 bash -c 'cat < /dev/null > /dev/tcp/192.168.43.1/41927'; then echo "Azilink is not accessible"; exit 255; fi

## Start Pseudo-VPN
###
sudo openvpn azilink_net.ovpn

## Stop gateway
###
# OpenVPN will perform all the cleanups
