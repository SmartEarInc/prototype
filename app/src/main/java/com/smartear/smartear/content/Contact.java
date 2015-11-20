package com.smartear.smartear.content;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 20.11.2015
 */
public class Contact {
    private String name;
    private ArrayList<String> phones;
    private long contactId;

    public Contact(Cursor cursor) {
        name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
        contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }
}
