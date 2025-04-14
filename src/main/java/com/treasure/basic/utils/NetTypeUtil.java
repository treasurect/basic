package com.treasure.basic.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.treasure.basic.logger.CrashReport;

public class NetTypeUtil {
  /**
   * Unknown network class
   */
  public static final int NETWORK_CLASS_UNKNOWN = 0;

  /**
   * wifi net work
   */
  public static final int NETWORK_WIFI = 1;

  /**
   * "2G" networks
   */
  public static final int NETWORK_CLASS_2_G = 2;

  /**
   * "3G" networks
   */
  public static final int NETWORK_CLASS_3_G = 3;

  /**
   * "4G" networks
   */

  public static final int NETWORK_CLASS_4_G = 4;


  /**
   * "5G" networks
   */

  public static final int NETWORK_CLASS_5_G = 5;


  public static int getNetWorkStatus(Context context) {
    int netWorkType = NetTypeUtil.NETWORK_CLASS_UNKNOWN;

    ConnectivityManager connectivityManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

    if (networkInfo != null && networkInfo.isConnected()) {
      int type = networkInfo.getType();

      if (type == ConnectivityManager.TYPE_WIFI) {
        netWorkType = NetTypeUtil.NETWORK_WIFI;
      } else if (type == ConnectivityManager.TYPE_MOBILE) {
        netWorkType = getNetWorkClass(context);
      }
    }

    return netWorkType;
  }


  public static int getNetWorkClass(Context context) {
    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

    switch (telephonyManager.getNetworkType()) {
      case TelephonyManager.NETWORK_TYPE_GPRS:
      case TelephonyManager.NETWORK_TYPE_EDGE:
      case TelephonyManager.NETWORK_TYPE_CDMA:
      case TelephonyManager.NETWORK_TYPE_1xRTT:
      case TelephonyManager.NETWORK_TYPE_IDEN:
        return NetTypeUtil.NETWORK_CLASS_2_G;

      case TelephonyManager.NETWORK_TYPE_UMTS:
      case TelephonyManager.NETWORK_TYPE_EVDO_0:
      case TelephonyManager.NETWORK_TYPE_EVDO_A:
      case TelephonyManager.NETWORK_TYPE_HSDPA:
      case TelephonyManager.NETWORK_TYPE_HSUPA:
      case TelephonyManager.NETWORK_TYPE_HSPA:
      case TelephonyManager.NETWORK_TYPE_EVDO_B:
      case TelephonyManager.NETWORK_TYPE_EHRPD:
      case TelephonyManager.NETWORK_TYPE_HSPAP:
        return NetTypeUtil.NETWORK_CLASS_3_G;
      case TelephonyManager.NETWORK_TYPE_LTE:
        return NetTypeUtil.NETWORK_CLASS_4_G;

      default:
        return NetTypeUtil.NETWORK_CLASS_UNKNOWN;
    }
  }


  public static String getIPAddress(Context context) {
    NetworkInfo info = ((ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    if (info != null && info.isConnected()) {
      if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
        try {
          //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
          for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
              InetAddress inetAddress = enumIpAddr.nextElement();
              if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                return inetAddress.getHostAddress();
              }
            }
          }
        } catch (SocketException e) {
          CrashReport.INSTANCE.postException(e);
          e.printStackTrace();
        }

      } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
        return ipAddress;
      }
    } else {
      //当前无网络连接,请在设置中打开网络
    }
    return "";
  }

  /**
   * 将得到的int类型的IP转换为String类型
   *
   * @param ip
   * @return
   */
  public static String intIP2StringIP(int ip) {
    return (ip & 0xFF) + "." +
        ((ip >> 8) & 0xFF) + "." +
        ((ip >> 16) & 0xFF) + "." +
        (ip >> 24 & 0xFF);
  }

}
