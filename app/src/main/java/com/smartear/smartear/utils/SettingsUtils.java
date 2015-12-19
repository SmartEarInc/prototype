package com.smartear.smartear.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 17.12.2015
 */
public class SettingsUtils {
    public static final String MODE_STREAMING = "0";
    public static final String MODE_RAE = "1";
    public static final String KEY_PLAYER_MODE = "player_mode";
    public static final String KEY_ASSISTANT_PACKAGE_NAME = "assistant_package_name";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("SmartEarSettings", Context.MODE_PRIVATE);
    }
}