package com.smartear.smartear.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;

/**
 * Created: Belozerov Sergei
 * E-mail: belozerow@gmail.com
 * Company: APPGRANULA LLC
 * Date: 03/06/16
 */
public class NotificationReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_ACTION = "com.smartear.smartear.NOTIFICATION";
    public static final String EXTRA_URL = "extra_audio_url";
    public static final String EXTRA_TYPE = "extra_notification_type";
    public static final String TYPE_MESSAGE = "type_message";
    public static final String TYPE_RECORDING = "type_recording";
    public static final String TYPE_PLAY = "type_play";
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private boolean isRecording = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getStringExtra(EXTRA_URL);
        String type = intent.getStringExtra(EXTRA_TYPE);
        showNotification(type, url);
    }

    protected void showNotification(final String type, final String url) {
        RemoteViews remoteViews = null;
        switch (type) {
            case TYPE_RECORDING:
                remoteViews = getRecordingView(url);
                break;
            case TYPE_PLAY:
                playUrl(url);
            case TYPE_MESSAGE:
            default:
                remoteViews = getMessageView(url);
                break;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SmartEarApplication.getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContent(remoteViews);
        Notification notificationCompat = builder.build();
        notificationCompat.bigContentView = remoteViews;
        NotificationManager mNotificationManager = (NotificationManager) SmartEarApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, notificationCompat);

        if (TYPE_PLAY.equals(type) && !isPlaying) {
            isPlaying = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (; progress <= 6; progress++) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showNotification(type, url);
                                if (progress > 6) {
                                    progress = 0;
                                    mediaPlayer = null;
                                    isPlaying = false;
                                }
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        if (TYPE_RECORDING.equals(type) && !isRecording) {
            isRecording = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (; recordingProgress <= 6; recordingProgress++) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showNotification(type, url);
                                if (recordingProgress > 6) {
                                    recordingProgress = 0;
                                    isRecording = false;
                                }
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    private boolean isPlaying = false;

    int progress = 0;
    int recordingProgress = 0;

    private void playUrl(String url) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(SmartEarApplication.getContext(), Uri.parse(url));
            mediaPlayer.start();
        }
    }

    private RemoteViews getMessageView(String url) {
        RemoteViews remoteViews = new RemoteViews(SmartEarApplication.getContext().getPackageName(), R.layout.widget_notification);

        Intent playIntent = new Intent();
        playIntent.setAction(NOTIFICATION_ACTION);
        playIntent.putExtra(NotificationReceiver.EXTRA_TYPE, NotificationReceiver.TYPE_PLAY);
        playIntent.putExtra(NotificationReceiver.EXTRA_URL, url);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(SmartEarApplication.getContext(), 1, playIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.play, resultPendingIntent);

        Intent recordIntent = new Intent();
        recordIntent.setAction(NOTIFICATION_ACTION);
        recordIntent.putExtra(NotificationReceiver.EXTRA_TYPE, NotificationReceiver.TYPE_RECORDING);
        PendingIntent voiceAnswerPendingIntent = PendingIntent.getBroadcast(SmartEarApplication.getContext(), 2, recordIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.voiceAnswer, voiceAnswerPendingIntent);
        remoteViews.setProgressBar(R.id.playing_progress, 6, progress, false);

        return remoteViews;
    }

    private RemoteViews getRecordingView(String url) {
        RemoteViews remoteViews = new RemoteViews(SmartEarApplication.getContext().getPackageName(), R.layout.widget_notification_recording);
        remoteViews.setProgressBar(R.id.recording_progress, 6, recordingProgress, false);
        if (recordingProgress >= 6) {
            remoteViews.setTextViewText(R.id.recordingMessage, "Message sent");
        }
        return remoteViews;
    }
}
