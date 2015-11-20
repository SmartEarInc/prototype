package com.smartear.smartear.utils.commands;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.smartear.smartear.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 20.11.2015
 */
public class LaunchCommandHelper extends BaseCommandHelper {
    private static final String COMMAND_LAUNCH = "LAUNCH";
    private static final String COMMAND_OPEN = "OPEN";
    public LaunchCommandHelper(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public boolean parseCommand(String text) {
        String appName = "";
        if (text.toUpperCase().startsWith(COMMAND_LAUNCH)) {
            appName = text.substring(text.toUpperCase().indexOf(COMMAND_LAUNCH) + COMMAND_LAUNCH.length() + 1, text.length());
        }

        if (text.toUpperCase().startsWith(COMMAND_OPEN)) {
            appName = text.substring(text.toUpperCase().indexOf(COMMAND_OPEN) + COMMAND_OPEN.length() + 1, text.length());
        }

        if (!TextUtils.isEmpty(appName)) {
            launchApp(appName);
        }
        return false;
    }

    private void launchApp(String appName) {
        PackageManager packageManager = activity.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(mainIntent, 0);
        String label;
        for (ResolveInfo resolveInfo : resolveInfoList) {
            label = packageManager.getApplicationLabel(resolveInfo.activityInfo.applicationInfo).toString();
            if (label.toUpperCase().contains(appName.toUpperCase())) {
                try {
                    activity.startActivity(packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName));
                } catch (ActivityNotFoundException e) {
                    sayText(activity.getString(R.string.noActivityError, appName));
                }
                return;
            }
        }
        sayText(activity.getString(R.string.noActivityError, appName));
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
