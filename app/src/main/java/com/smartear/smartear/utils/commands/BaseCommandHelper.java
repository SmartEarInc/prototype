package com.smartear.smartear.utils.commands;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.nuance.nmdp.speechkit.SpeechError;
import com.nuance.nmdp.speechkit.Vocalizer;
import com.smartear.smartear.BaseActivity;
import com.smartear.smartear.SmartEarApplication;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 20.11.2015
 */
public abstract class BaseCommandHelper {
    protected final AppCompatActivity activity;

    public BaseCommandHelper(AppCompatActivity activity){
        this.activity = activity;
    }

    public abstract boolean parseCommand(String text);

    public abstract boolean onActivityResult(int requestCode, int resultCode, Intent data);

    protected void sayText(String text) {
        Vocalizer vocalizer = SmartEarApplication.getSpeechKit().createVocalizerWithLanguage("en_US", new Vocalizer.Listener() {
            @Override
            public void onSpeakingBegin(Vocalizer vocalizer, String s, Object o) {

            }

            @Override
            public void onSpeakingDone(Vocalizer vocalizer, String s, SpeechError speechError, Object o) {

            }
        }, new Handler());
        vocalizer.speakString(text, new Object());
    }
}
