package com.smartear.smartear.voice;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


/**
 * Created: Belozerov Sergei
 * E-mail: belozerow@gmail.com
 * Company: APPGRANULA LLC
 * Date: 05/07/16
 */
public class SphinxVoiceRecognizer extends BaseVoiceRecognizer implements RecognitionListener {
    private static final String KEYPHRASE = "snap chat";
    SpeechRecognizer recognizer;
    private static final String KWS_SEARCH = "wakeup";
    private static final String COMMANDS_SEARCH = "commands";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runRecognizerSetup();
    }


    private void runRecognizerSetup() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(getActivity());
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(getActivity(), "Can't init voice recognizer", Toast.LENGTH_SHORT).show();
                } else {
//                    startRecording();
                }
            }
        }.execute();
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);
        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.beep);
        mp.start();
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "smartear-commands.dict"))

//                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .setKeywordThreshold(1e-40f) // Threshold to tune for keyphrase to balance between false alarms and misses
                .setBoolean("-allphone_ci", true)  // Use context-independent phonetic search, context-dependent is too slow for mobile

                .getRecognizer();
        recognizer.addListener(this);

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
//        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
//        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
        recognizer.addKeywordSearch(KWS_SEARCH, new File(assetsDir, "keyphrase.list"));
//        recognizer.addGrammarSearch(COMMANDS_SEARCH, new File(assetsDir, "commands.gram"));

    }

    @Override
    protected void startRecording() {
        switchSearch(KWS_SEARCH);
    }

    @Override
    protected void stopRecording() {
        if (recognizer != null)
            recognizer.stop();
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {
        recognizer.stop();
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
//            if (text.equals(KEYPHRASE))
//                switchSearch(COMMANDS_SEARCH);
//            else {
            Toast.makeText(SmartEarApplication.getContext(), text, Toast.LENGTH_SHORT).show();
            getCommandHelper().parseCommand(text);
//            }
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
//        if (hypothesis != null) {
//            String text = hypothesis.getHypstr();
//            Toast.makeText(SmartEarApplication.getContext(), text, Toast.LENGTH_SHORT).show();
//            getCommandHelper().parseCommand(text);
//            stopBtMicrophone();
//        }

//        stopBtMicrophone();
//        stopRecording();
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {
        startRecording();
    }
}
