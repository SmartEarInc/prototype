package com.smartear.smartear.utils.commands;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 20.11.2015
 */
public class CommandHelper {

    private ArrayList<BaseCommandHelper> helpers = new ArrayList<>();

    public CommandHelper(AppCompatActivity activity) {
        helpers.add(new CallCommandHelper(activity));
        helpers.add(new LaunchCommandHelper(activity));
        helpers.add(new VolumeCommandHelper(activity));
    }

    public void parseCommand(String text) {
        for (BaseCommandHelper helper : helpers) {
            if (helper.parseCommand(text))
                return;
        }
    }


    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        for (BaseCommandHelper helper : helpers) {
            if (helper.onActivityResult(requestCode, resultCode, data)) {
                return true;
            }
        }
        return false;
    }
}
