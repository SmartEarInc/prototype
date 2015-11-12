package com.smartear.smartear.utils;

import android.os.Build;

/**
 * Created by Belozerow on 12.11.2015.
 */
public class VersionUtils {
    public static boolean lollipopOrHigher(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
