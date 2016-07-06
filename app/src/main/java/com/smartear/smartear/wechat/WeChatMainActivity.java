package com.smartear.smartear.wechat;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.smartear.smartear.BaseActivity;
import com.smartear.smartear.R;
import com.smartear.smartear.fragment.BaseFragment;
import com.smartear.smartear.services.BTService;
import com.smartear.smartear.utils.MediaPlayerHelper;
import com.smartear.smartear.utils.commands.CommandHelper;
import com.smartear.smartear.utils.firebase.MessageHelper;
import com.smartear.smartear.voice.BaseVoiceRecognizer;
import com.smartear.smartear.voice.SphinxVoiceRecognizer;
import com.smartear.smartear.voice.VoiceRecognizer;
import com.smartear.smartear.wechat.bus.VoiceCommandEvent;
import com.smartear.smartear.wechat.fragments.WeChatAuthFragment;
import com.smartear.smartear.wechat.fragments.WeChatBaseFragment;
import com.smartear.smartear.wechat.fragments.WeChatDidiFragment;
import com.smartear.smartear.wechat.fragments.WeChatMeetingFragment;
import com.smartear.smartear.wechat.fragments.WeChatMusicFragment;
import com.smartear.smartear.wechat.fragments.WeChatRecordingFragment;
import com.smartear.smartear.wechat.fragments.WeChatWeatherFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class WeChatMainActivity extends BaseActivity implements MessageHelper.OnNewMessageListener {
    public static final String EXTRA_START_RECOGNITION = "extra_start_recognition";
    public static final String EXTRA_PLAY = "extra_play";
    public static final String EXTRA_RECORD = "extra_record";
    private CommandHelper commandHelper;
    private MessageHelper messageHelper = new MessageHelper();
    BaseVoiceRecognizer voiceRecognizer;
    MediaPlayerHelper mediaPlayerHelper = new MediaPlayerHelper();
    private RecognizedState currentState = null;

    public MessageHelper getMessageHelper() {
        return messageHelper;
    }

    public BaseVoiceRecognizer getVoiceRecognizer() {
        return voiceRecognizer;
    }

    private AudioManager audioManager;

    private MediaPlayer mediaPlayer;

    public void requestAudioFocus() {
        if (!audioFocusGranted) {
            int result = audioManager.requestAudioFocus(audioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                audioFocusGranted = true;
            }
            BTService.restart(this);
        }
    }

    private boolean audioFocusGranted = false;
    private boolean isResumed = false;

    private AudioManager.OnAudioFocusChangeListener audioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    abandonAudioFocus();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        requestAudioFocus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
        abandonAudioFocus();
    }

    private void abandonAudioFocus() {
        audioFocusGranted = false;
        audioManager.abandonAudioFocus(audioFocusListener);
    }

    private void registerMediaButtonReceiver() {
        BTService.start(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        setContentView(R.layout.wechat_activity_main);
        commandHelper = new CommandHelper(this);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        registerMediaButtonReceiver();

        if (savedInstanceState == null) {
            voiceRecognizer = new SphinxVoiceRecognizer();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.voiceContainer, voiceRecognizer)
                    .commit();
        } else {
            voiceRecognizer = (BaseVoiceRecognizer) getSupportFragmentManager().findFragmentById(R.id.voiceContainer);
        }

        parseIntent(getIntent());

        messageHelper.addNewMessageListener(this);

        findViewById(R.id.start_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVoiceRecognizer().startRecognize();
//                onVoiceEvent(new VoiceCommandEvent(RecognizedState.VOICE_MESSAGE));
//                MessageHelper.showNotification("");
            }
        });

        EventBus.getDefault().register(this);
    }

    public boolean isStartRecordingOnResume() {
        return startRecordingOnResume;
    }

    public void setStartRecordingOnResume(boolean startRecordingOnResume) {
        this.startRecordingOnResume = startRecordingOnResume;
    }

    private boolean startRecordingOnResume;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
    }

    private boolean parseIntent(Intent intent) {
        if (intent != null) {
            if ((intent.hasExtra(EXTRA_START_RECOGNITION) || (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VOICE_COMMAND)))) {
                if (voiceRecognizer != null && voiceRecognizer.getActivity() != null) {
                    voiceRecognizer.startRecognize();
                } else {
                    setStartRecordingOnResume(true);
                }
                return true;
            }
            if (intent.getBooleanExtra(EXTRA_RECORD, false)) {
                replaceFragment(new WeChatRecordingFragment(), false);
                return true;
            }

            if (intent.hasExtra(EXTRA_PLAY)) {
                String url = intent.getStringExtra(EXTRA_PLAY);
                if (url != null)
                    playUrl(url);
            }
        }

        return false;
    }

    @Subscribe
    public void onVoiceEvent(VoiceCommandEvent event) {
        if(currentState == event.recognizedState)
            return;
        abandonAudioFocus();
        currentState = event.recognizedState;
        switch (event.recognizedState) {
            case AUTH:
                replaceFragment(new WeChatAuthFragment(), false);
                break;
            case WEATHER:
                replaceFragment(new WeChatWeatherFragment(), false);
                break;
            case MEETING:
                replaceFragment(new WeChatMeetingFragment(), false);
                break;
            case VOICE_MESSAGE:
                replaceFragment(new WeChatRecordingFragment(), false);
                break;
            case MUSIC:
                replaceFragment(new WeChatMusicFragment(), false);
                break;
            case PAUSE_MUSIC:
                pauseMusic();
                break;
            case RESUME_MUSIC:
                resumeMusic();
                break;
            case DIDI:
                forcePauseMusic();
                Bundle arguments;
                WeChatDidiFragment fragment = new WeChatDidiFragment();
                if (fragment.getArguments() == null) {
                    arguments = new Bundle();
                } else {
                    arguments = fragment.getArguments();
                }
                arguments.putBoolean(BaseFragment.ADD_TO_BACK_STACK, false);
                fragment.setArguments(arguments);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                fragmentTransaction.replace(R.id.taxiContainer, fragment, fragment.getFragmentTag());
                fragmentTransaction.commitAllowingStateLoss();

                WeChatBaseFragment weChatBaseFragment = getLastWeChatFragment();
                if (weChatBaseFragment instanceof WeChatMusicFragment) {
                    ((WeChatMusicFragment) weChatBaseFragment).showSmallPlayer();
                }

                break;
        }
    }

    public WeChatBaseFragment getLastWeChatFragment() {
        return (WeChatBaseFragment) getLastFragment();
    }

    public void playMusic() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception ignore) {

            }
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.adele_flac);
        mediaPlayer.start();

    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            } else {
                forcePauseMusic();
                WeChatBaseFragment.sayText(this, RecognizedState.PAUSE_MUSIC);
            }
        }
    }

    public void forcePauseMusic(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            getLastWeChatFragment().pauseMusic();
        }
    }

    public void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            WeChatBaseFragment.sayText(this, RecognizedState.RESUME_MUSIC);
            getLastWeChatFragment().resumeMusic();
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public CommandHelper getCommandHelper() {
        return commandHelper;
    }

    @Override
    public void onNewMessage(final String url) {
//        if (!TextUtils.isEmpty(url)) {
//            replaceFragment(WeChatNewMessageFragment.newInstance(url), false);
//        }
    }

    public void playUrl(String url) {
        mediaPlayerHelper.startPlaying(url);
    }
}
