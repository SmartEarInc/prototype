package com.smartear.smartear;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.nuance.nmdp.speechkit.Prompt;
import com.nuance.nmdp.speechkit.SpeechKit;
import com.smartear.smartear.speechkit.AppInfo;

import io.fabric.sdk.android.Fabric;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 13.11.2015
 */
public class SmartEarApplication extends Application {
    private static SpeechKit speechKit;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        speechKit = SpeechKit.initialize(getApplicationContext(), AppInfo.SpeechKitAppId, AppInfo.SpeechKitServer, AppInfo.SpeechKitPort, AppInfo.SpeechKitSsl, AppInfo.SpeechKitApplicationKey);
        speechKit.connect();

        Prompt beep = speechKit.defineAudioPrompt(R.raw.beep);
        speechKit.setDefaultRecognizerPrompts(beep, Prompt.vibration(100), null, null);
        context = this;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously();
    }

    public static Context getContext() {
        return context;
    }

    public static SpeechKit getSpeechKit() {
        return speechKit;
    }
}
