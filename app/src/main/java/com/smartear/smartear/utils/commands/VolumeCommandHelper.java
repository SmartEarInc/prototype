package com.smartear.smartear.utils.commands;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;

import com.smartear.smartear.fragment.StartFragment;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 20.11.2015
 */
public class VolumeCommandHelper extends BaseCommandHelper {
    private static final String COMMAND_VOLUME_UP = "VOLUME UP";
    private static final String COMMAND_INCREASE_VOLUME = "INCREASE VOLUME";
    private static final String COMMAND_VOLUME_DOWN = "VOLUME DOWN";
    private static final String COMMAND_DECREASE_VOLUME = "DECREASE VOLUME";

    public VolumeCommandHelper(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public boolean parseCommand(String text) {
        if (text.toUpperCase().contains(COMMAND_VOLUME_UP) || text.toUpperCase().contains(COMMAND_INCREASE_VOLUME)) {
            increaseVolume();
            return true;
        }
        if (text.toUpperCase().contains(COMMAND_VOLUME_DOWN) || text.toUpperCase().contains(COMMAND_DECREASE_VOLUME)) {
            decreaseVolume();
            return true;
        }
        return false;
    }

    private void decreaseVolume() {
        AudioManager am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int volume = am.getStreamVolume(StartFragment.BT_STREAM) - 1;
        am.setStreamVolume(StartFragment.BT_STREAM, volume, AudioManager.FLAG_SHOW_UI);
    }

    private void increaseVolume() {
        AudioManager am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int volume = am.getStreamVolume(StartFragment.BT_STREAM) + 1;
        am.setStreamVolume(StartFragment.BT_STREAM, volume, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
