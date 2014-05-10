package org.idr.notouch.app.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Created by ismail ARILIK on 05.05.2014.
 */
public final class ContactUtils {

    public static String getPhoneNumberFromPersonName(Context appContext, String personName) {
        // find the phone number of person
        String phoneNumber = null;
        Cursor cursor = null;
        try {
            cursor = appContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
                int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                cursor.moveToFirst();
                do {
                    String idContact = cursor.getString(contactIdIdx);
                    String name = cursor.getString(nameIdx);
                    if (name.equalsIgnoreCase(personName)) {
                        phoneNumber = cursor.getString(phoneNumberIdx);
                        break;
                    }
                } while (cursor.moveToNext());
            } else {
                // TODO işle
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return phoneNumber;
    }
}
