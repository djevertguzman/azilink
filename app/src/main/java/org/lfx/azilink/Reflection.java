/* AziLink: USB tethering for Android
 * Copyright (C) 2009 by James Perry
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lfx.azilink;

import android.app.Notification;
import android.app.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Reflection class to access private APIs and APIs that change
 * between versions
 *
 * @author jperry (10/28/2009)
 */
public class Reflection {
    public static final int NOTIFY_ID = 1;
    private static Method mSystemProperties_get;
    private static Method mStartForeground;
    private static Method mStopForeground;

    static {
        try {
            Class sysProp = Class.forName("android.os.SystemProperties");
            mSystemProperties_get = sysProp.getMethod("get",
                    String.class);
        } catch (ClassNotFoundException c) {
        } catch (NoSuchMethodException e) {
        }

        try {
            mStartForeground = android.app.Service.class.getMethod("startForeground",
                    int.class, Notification.class);
        } catch (NoSuchMethodException e) {
        }

        try {
            mStopForeground = android.app.Service.class.getMethod("stopForeground",
                    boolean.class);
        } catch (NoSuchMethodException e) {
        }
    }

    /**
     * Start foreground service
     *
     * @param service Service that's going in the foreground
     * @param notify  Notification to display while running
     */
    public static void startForeground(Service service, Notification notify) {
        if (mStartForeground != null) {
            try {
                mStartForeground.invoke(service, NOTIFY_ID, notify);
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    /**
     * Reverses whatever startForeground did.
     *
     * @param service
     * @param notify
     */
    public static void stopForeground(Service service) {
        if (mStopForeground != null) {
            try {
                mStopForeground.invoke(service, true);
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    /**
     * Returns the current DNS server, or 4.2.2.2 if one couldn't be found.
     * Starcom 2016 Edit: Changed DNS Server to Google's Public DNS.
     */
    public static String getDNS() {
        if (mSystemProperties_get == null) {
            return "8.8.4.4";
        }
        try {
            String dns = (String) mSystemProperties_get.invoke(null, "net.dns1");
            if (dns != "") return dns;
            return "8.8.4.4";
        } catch (Exception mye) {
            return "8.8.4.4";
        }
    }
}
