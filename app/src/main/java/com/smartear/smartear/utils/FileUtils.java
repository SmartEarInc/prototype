package com.smartear.smartear.utils;

import android.os.Environment;

import com.smartear.smartear.SmartEarApplication;

import java.io.File;


/**
 * Created: Belozerov Sergei
 * E-mail: belozerow@gmail.com
 * Company: APPGRANULA LLC
 * Date: 22/04/16
 */
public class FileUtils {
    private static final String DIRECTORY = "SmartEarMessages";

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String newFilePath(String name) {
        File filesDir = null;
        if (FileUtils.isExternalStorageWritable()) {
            filesDir = SmartEarApplication.getContext().getExternalFilesDir(null);
        }
        if (filesDir == null) {
            filesDir = SmartEarApplication.getContext().getFilesDir();
        }
        File mentalDir = new File(filesDir.getAbsoluteFile() + File.separator + DIRECTORY);
        if (!mentalDir.exists()) {
            mentalDir.mkdirs();
        }
        return mentalDir.getAbsoluteFile() + File.separator + name;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String newFilePath(String entryID, String name) {
        File filesDir = null;
        if (FileUtils.isExternalStorageWritable()) {
            filesDir = SmartEarApplication.getContext().getExternalFilesDir(null);
        }
        if (filesDir == null) {
            filesDir = SmartEarApplication.getContext().getFilesDir();
        }
        File mentalDir = new File(filesDir.getAbsoluteFile() + File.separator + DIRECTORY + File.separator + entryID);
        if (!mentalDir.exists()) {
            mentalDir.mkdirs();
        }
        return mentalDir.getAbsoluteFile() + File.separator + name;
    }
}
