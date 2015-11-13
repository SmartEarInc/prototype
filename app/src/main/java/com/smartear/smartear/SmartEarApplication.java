package com.smartear.smartear;

import android.app.Application;

import com.nuance.nmdp.speechkit.Prompt;
import com.nuance.nmdp.speechkit.SpeechKit;
import com.smartear.smartear.speechkit.AppInfo;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 13.11.2015
 */
public class SmartEarApplication extends Application {
    private static SpeechKit speechKit;

    @Override
    public void onCreate() {
        super.onCreate();
        speechKit = SpeechKit.initialize(getApplicationContext(), AppInfo.SpeechKitAppId, AppInfo.SpeechKitServer, AppInfo.SpeechKitPort, AppInfo.SpeechKitSsl, AppInfo.SpeechKitApplicationKey);
        speechKit.connect();

        Prompt beep = speechKit.defineAudioPrompt(R.raw.beep);
        speechKit.setDefaultRecognizerPrompts(beep, Prompt.vibration(100), null, null);
    }

    public static SpeechKit getSpeechKit() {
        return speechKit;
    }
}
