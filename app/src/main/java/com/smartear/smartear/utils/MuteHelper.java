package com.smartear.smartear.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import java.util.ArrayList;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 11.11.2015
 */
public class MuteHelper {
    private final Context context;
    private static final ArrayList<Stream> streams = new ArrayList<>();

    static {
        streams.add(new Stream(AudioManager.STREAM_MUSIC, "music"));
//        streams.add(new Stream(AudioManager.STREAM_RING, "ring"));
    }

    public MuteHelper(Context context) {
        this.context = context;
    }

    public void muteSound() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MuteHelper", Context.MODE_PRIVATE);
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Stream stream : streams) {
            editor.putInt(stream.key, am.getStreamVolume(stream.stream));
            am.setStreamVolume(stream.stream, 0, 0);
        }
        editor.apply();
    }

    public void unmuteSound() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MuteHelper", Context.MODE_PRIVATE);
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        for (Stream stream : streams) {
            am.setStreamVolume(stream.stream, sharedPreferences.getInt(stream.key, stream.stream), 0);
        }
    }

    public boolean isMute() {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        for (Stream stream : streams) {
            if (am.getStreamVolume(stream.stream) > 0) {
                return false;
            }
        }
        return true;
    }

    public void muteMic() {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setMicrophoneMute(true);
    }

    public void unMuteMic() {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setMicrophoneMute(false);
    }

    public boolean isMicMute() {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.isMicrophoneMute();
    }

    private static class Stream {
        int stream;
        String key;

        public Stream(int stream, String key) {
            this.stream = stream;
            this.key = key;
        }
    }
}
