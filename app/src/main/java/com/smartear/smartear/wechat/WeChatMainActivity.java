package com.smartear.smartear.wechat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.smartear.smartear.BaseActivity;
import com.smartear.smartear.R;
import com.smartear.smartear.utils.MediaPlayerHelper;
import com.smartear.smartear.utils.commands.CommandHelper;
import com.smartear.smartear.utils.firebase.MessageHelper;
import com.smartear.smartear.voice.VoiceRecognizer;
import com.smartear.smartear.wechat.bus.VoiceCommandEvent;
import com.smartear.smartear.wechat.fragments.WeChatAuthFragment;
import com.smartear.smartear.wechat.fragments.WeChatBaseFragment;
import com.smartear.smartear.wechat.fragments.WeChatMeetingFragment;
import com.smartear.smartear.wechat.fragments.WeChatMusicFragment;
import com.smartear.smartear.wechat.fragments.WeChatNewMessageFragment;
import com.smartear.smartear.wechat.fragments.WeChatRecordingFragment;
import com.smartear.smartear.wechat.fragments.WeChatWeatherFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class WeChatMainActivity extends BaseActivity implements MessageHelper.OnNewMessageListener {
    private CommandHelper commandHelper;
    private MessageHelper messageHelper = new MessageHelper();
    VoiceRecognizer voiceRecognizer;
    MediaPlayerHelper mediaPlayerHelper = new MediaPlayerHelper();

    public MessageHelper getMessageHelper() {
        return messageHelper;
    }

    public VoiceRecognizer getVoiceRecognizer() {
        return voiceRecognizer;
    }

    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wechat_activity_main);
        commandHelper = new CommandHelper(this);

        if (savedInstanceState == null) {
            voiceRecognizer = new VoiceRecognizer();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.voiceContainer, voiceRecognizer)
                    .commit();
        } else {
            voiceRecognizer = (VoiceRecognizer) getSupportFragmentManager().findFragmentById(R.id.voiceContainer);
        }

        messageHelper.addNewMessageListener(this);

        findViewById(R.id.start_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVoiceRecognizer().startRecognize();
//                onVoiceEvent(new VoiceCommandEvent(RecognizedState.MUSIC));
            }
        });

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onVoiceEvent(VoiceCommandEvent event) {
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
                pauseMusic();
                break;
        }
    }

    public WeChatBaseFragment getLastWeChatFragment() {
        return (WeChatBaseFragment) getLastFragment();
    }

    public void playMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.adele_hello);
        mediaPlayer.start();
    }

    public void pauseMusic() {
        mediaPlayer.pause();
        getLastWeChatFragment().pauseMusic();
    }

    public void resumeMusic() {
        mediaPlayer.start();
        getLastWeChatFragment().resumeMusic();
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
        if (!TextUtils.isEmpty(url)) {
            replaceFragment(WeChatNewMessageFragment.newInstance(url), false);
        }
    }

    public void playUrl(String url) {
        mediaPlayerHelper.startPlaying(url);
    }
}
