package com.smartear.smartear.utils.firebase;

import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smartear.smartear.SmartEarApplication;
import com.smartear.smartear.receivers.NotificationReceiver;

public class MessageHelper {
    private boolean iAmSender = false;

    public MessageHelper() {
        super();
    }

    public void sendFile(Uri fileUri) {
//        iAmSender = true;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("message_new");
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
        final DatabaseReference reference = database.getReference("message_new");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !iAmSender) {
                    onNewMessageListener.onNewMessage(dataSnapshot.getValue().toString());
////                    dataSnapshot.getRef().removeValue();
                    showNotification(dataSnapshot.getValue().toString());
                }
//                iAmSender = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void showNotification(String url) {
        Intent intent = new Intent();
        intent.setAction(NotificationReceiver.NOTIFICATION_ACTION);
        intent.putExtra(NotificationReceiver.EXTRA_TYPE, NotificationReceiver.TYPE_MESSAGE);
        intent.putExtra(NotificationReceiver.EXTRA_URL, url);
        SmartEarApplication.getContext().sendBroadcast(intent);
    }

    public interface OnNewMessageListener {
        void onNewMessage(String url);
    }
}
