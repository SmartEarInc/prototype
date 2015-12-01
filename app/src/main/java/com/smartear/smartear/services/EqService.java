package com.smartear.smartear.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.smartear.smartear.R;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 30.11.2015
 */
public class EqService extends Service {
    private static final int NOTIFICATION_ID = 10;
    private IBinder binder = new EqBinder();
    private Equalizer equalizer;
    private BassBoost bassBoost;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initEq();
    }

    public static void start(Context context) {
        context.startService(new Intent(context, EqService.class));
    }

    public static void bind(Activity activity, ServiceConnection serviceConnection) {
        activity.bindService(new Intent(activity, EqService.class), serviceConnection, 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentText(getString(R.string.equalizer))
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_bt_device);
        startForeground(NOTIFICATION_ID, builder.build());
        return START_STICKY;
    }

    private void initEq() {
        equalizer = new Equalizer(100, 0);
        equalizer.setEnabled(true);
        bassBoost = new BassBoost(100, 0);
        bassBoost.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (equalizer != null)
            equalizer.release();
        if (bassBoost != null)
            bassBoost.release();
    }

    public Equalizer getEqualizer() {
        return equalizer;
    }

    public BassBoost getBassBoost() {
        return bassBoost;
    }

    public class EqBinder extends Binder {
        public EqService getService() {
            return EqService.this;
        }
    }
}
