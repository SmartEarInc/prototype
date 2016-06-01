package com.smartear.smartear.wechat.fragments;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.smartear.smartear.R;
import com.smartear.smartear.wechat.RecognizedState;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created: Belozerov Sergei
 * E-mail: belozerow@gmail.com
 * Company: APPGRANULA LLC
 * Date: 26/05/16
 */
public class WeChatMusicFragment extends WeChatBaseFragment {
    private View retrieving;
    private View playingContainer;
    private SeekBar seekbar;
    private Timer timer;
    private TextView statusText;
    private View smallPlayer;
    private View player;
    private View musicIconContainer;

    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wechat_music_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrieving = view.findViewById(R.id.retrieving);
        playingContainer = view.findViewById(R.id.musicPlaying);
        seekbar = (SeekBar) view.findViewById(R.id.seekbarProgress);
        statusText = (TextView) view.findViewById(R.id.statusText);
        player = view.findViewById(R.id.musicPlayer);
        smallPlayer = view.findViewById(R.id.smallMusicPlayer);
        musicIconContainer = view.findViewById(R.id.musicIconContainer);

        sayText(getWeChatActivity(), RecognizedState.MUSIC);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                retrieving.setVisibility(View.GONE);
                playingContainer.setVisibility(View.VISIBLE);
                startPlaying();
            }
        }, 2000);
    }

    private MediaPlayer getMediaPlayer() {
        return getWeChatActivity().getMediaPlayer();
    }

    private void startPlaying() {
        getWeChatActivity().playMusic();
        seekbar.setMax(getMediaPlayer().getDuration());
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                seekbar.setProgress(getMediaPlayer().getCurrentPosition());
            }
        }, 0, 1000);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            timer.cancel();
        } catch (Exception ignore) {

        }
    }

    @Override
    public void pauseMusic() {
        super.pauseMusic();
        statusText.setText("Paused");
        statusText.setTextColor(getResources().getColor(R.color.smartGrayTextColor));
    }

    @Override
    public void resumeMusic() {
        super.resumeMusic();
        statusText.setText("Playing");
        statusText.setTextColor(getResources().getColor(R.color.greenText));
    }

    public void showSmallPlayer() {
        smallPlayer.setAlpha(0);
        smallPlayer.setVisibility(View.VISIBLE);
        smallPlayer.animate().alpha(1);

        player.animate().alpha(0);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        musicIconContainer.animate().setDuration(200).translationXBy(-size.x);
    }
}
