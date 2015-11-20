package com.smartear.smartear.utils.commands;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.nuance.nmdp.speechkit.SpeechError;
import com.nuance.nmdp.speechkit.Vocalizer;
import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;
import com.smartear.smartear.content.Contact;

import java.util.ArrayList;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 20.11.2015
 */
public class CallCommandHelper extends BaseCommandHelper {
    public static final String COMMAND_CALL = "CALL";
    private static final int CONTACTS_ID = 1;
    private static final int CONTACTS_PHONE = 2;

    private static final String[] CONTACTS_PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
    };
    private static final String SELECTION =
            ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1";
    private static final int REQUEST_CODE = 3;
    private String phone;

    public CallCommandHelper(AppCompatActivity activity) {
        super(activity);
    }

    private boolean isNumber(String contact) {
        return TextUtils.isDigitsOnly(contact.trim());
    }


    private void callToContact(final String contact) {
        activity.getSupportLoaderManager().restartLoader(CONTACTS_ID, Bundle.EMPTY, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(activity,
                        Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(contact)),
                        CONTACTS_PROJECTION,
                        SELECTION,
                        new String[]{},
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
                );
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                ArrayList<Contact> contacts = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        contacts.add(new Contact(cursor));
                    } while (cursor.moveToNext());
                }
                cursor.close();

                if (contacts.isEmpty()) {
                    sayText(activity.getString(R.string.contactCanNotBeFound, contact));
                } else {
                    callToContact(contacts.get(0));
                }
                activity.getSupportLoaderManager().destroyLoader(CONTACTS_ID);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });
    }

    private void callToContact(final Contact contact) {
        activity.getSupportLoaderManager().restartLoader(CONTACTS_PHONE, Bundle.EMPTY, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(activity,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contact.getContactId() + ""},
                        null
                );
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    makePhoneCall(phone);
                } else {
                    sayText(activity.getString(R.string.noPhoneNumbersError, contact.getName()));
                }
                cursor.close();
                activity.getSupportLoaderManager().destroyLoader(CONTACTS_ID);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });
    }

    private void makePhoneCall(String phone) {
        this.phone = phone;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
            return;
        }
        activity.startActivity(intent);
        this.phone = null;
    }

    @Override
    public boolean parseCommand(String text) {
        if (text.toUpperCase().contains(COMMAND_CALL)) {
            String contact = text.substring(text.toUpperCase().indexOf(COMMAND_CALL) + COMMAND_CALL.length() + 1, text.length());
            if (isNumber(contact)) {
                makePhoneCall(contact);
            } else {
                callToContact(contact);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                makePhoneCall(phone);
            }
            return true;
        }
        return false;
    }
}
