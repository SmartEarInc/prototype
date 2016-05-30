package com.smartear.smartear.utils.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;
import com.smartear.smartear.wechat.WeChatMainActivity;

public class MessageHelper {
    private boolean iAmSender = false;

    public MessageHelper() {
        super();
    }

    public void sendFile(Uri fileUri) {
        iAmSender = true;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("message");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference fileRef = storageReference.child("messages/message.mp3");

        UploadTask uploadTask = fileRef.putFile(fileUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.setValue(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }

    public void addNewMessageListener(final OnNewMessageListener onNewMessageListener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("message");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !iAmSender) {
                    onNewMessageListener.onNewMessage(dataSnapshot.getValue().toString());
//                    dataSnapshot.getRef().removeValue();
                    showNotification(dataSnapshot.getValue().toString());
                }
                iAmSender = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void showNotification(String url) {
        RemoteViews remoteViews = new RemoteViews(SmartEarApplication.getContext().getPackageName(), R.layout.widget_notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SmartEarApplication.getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContent(remoteViews);
        Notification notificationCompat = builder.build();
        notificationCompat.bigContentView = remoteViews;

        Intent playIntent = new Intent(SmartEarApplication.getContext(), WeChatMainActivity.class);
        playIntent.putExtra(WeChatMainActivity.EXTRA_PLAY, url);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(SmartEarApplication.getContext(), 1, playIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.play, resultPendingIntent);



        Intent recordIntent = new Intent(SmartEarApplication.getContext(), WeChatMainActivity.class);
        recordIntent.putExtra(WeChatMainActivity.EXTRA_RECORD, true);
        PendingIntent voiceAnswerPendingIntent = PendingIntent.getActivity(SmartEarApplication.getContext(), 2, recordIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.voiceAnswer, voiceAnswerPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) SmartEarApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, notificationCompat);
    }

    public interface OnNewMessageListener {
        void onNewMessage(String url);
    }
}
