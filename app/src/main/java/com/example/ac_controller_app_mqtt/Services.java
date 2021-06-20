package com.example.ac_controller_app_mqtt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.ImageButton;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class Services {
    private static Context context;
    /*public static String getMacAddress(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        @SuppressLint("HardwareIds") String macAddress = wInfo.getMacAddress();
        return  macAddress;
    }*/
    public static void setCurrentFanSpeedImages(Activity activity, String msgContent) {
        ImageButton fan1 = activity.findViewById(R.id.fan_level_1);
        ImageButton fan2 = activity.findViewById(R.id.fan_level_2);
        ImageButton fan3 = activity.findViewById(R.id.fan_level_3);
        ImageButton fan4 = activity.findViewById(R.id.fan_level_4);
        switch (msgContent) {
            case "FAN_SPEED_NONE":
                fan1.setImageResource(R.drawable.fan_level_1_disabled);
                fan2.setImageResource(R.drawable.fan_level_2_disabled);
                fan3.setImageResource(R.drawable.fan_level_3_disabled);
                fan4.setImageResource(R.drawable.fan_level_4_disabled);
                break;
            case "FAN_SPEED_1":
                fan1.setImageResource(R.drawable.fan_level_1_enabled);
                fan2.setImageResource(R.drawable.fan_level_2_disabled);
                fan3.setImageResource(R.drawable.fan_level_3_disabled);
                fan4.setImageResource(R.drawable.fan_level_4_disabled);
                break;
            case "FAN_SPEED_2":
                fan1.setImageResource(R.drawable.fan_level_1_enabled);
                fan2.setImageResource(R.drawable.fan_level_2_enabled);
                fan3.setImageResource(R.drawable.fan_level_3_disabled);
                fan4.setImageResource(R.drawable.fan_level_4_disabled);
                break;
            case "FAN_SPEED_3":
                fan1.setImageResource(R.drawable.fan_level_1_enabled);
                fan2.setImageResource(R.drawable.fan_level_2_enabled);
                fan3.setImageResource(R.drawable.fan_level_3_enabled);
                fan4.setImageResource(R.drawable.fan_level_4_disabled);
                break;
            case "FAN_SPEED_4":
                fan1.setImageResource(R.drawable.fan_level_1_enabled);
                fan2.setImageResource(R.drawable.fan_level_2_enabled);
                fan3.setImageResource(R.drawable.fan_level_3_enabled);
                fan4.setImageResource(R.drawable.fan_level_4_enabled);
                break;
        }
    }
}
