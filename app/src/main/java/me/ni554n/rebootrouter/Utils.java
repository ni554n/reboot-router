package me.ni554n.rebootrouter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for the project.
 */
public class Utils {

    /**
     * Parse an integer address to valid IP format.
     */
    public static String parseIp(int address) {
        return String.valueOf(address & 0xFF) +
                "." +
                ((address >>>= 8) & 0xFF) +
                "." +
                ((address >>>= 8) & 0xFF) +
                "." +
                (address >>> 8 & 0xFF);
    }

    /**
     * Extract String value from given InputStream.
     */
    public static String inputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString("UTF-8");
    }

    /**
     * Returns whether the given String contains only digits.
     * Extracted from android.text.TextUtils.
     */
    public static boolean isDigitsOnly(CharSequence str) {
        final int len = str.length();

        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if device is connected to Wi-fi or not.
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null &&
                activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * Returns the Gateway IP of current network.
     */
    public static String refreshGateway(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        return Utils.parseIp(dhcpInfo.gateway);
    }
}