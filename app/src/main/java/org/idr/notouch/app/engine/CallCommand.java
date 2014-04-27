package org.idr.notouch.app.engine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import org.idr.notouch.app.R;
import org.idr.notouch.app.analyzer.MainActivity;
import org.idr.notouch.app.analyzer.SpeechActivity;
import org.idr.notouch.app.speech.MyTextToSpeech;

import java.util.Map;

/**
 * Created by ismail ARILIK on 26.04.2014.
 * implements the command pattern
 * RUNS THESE REQUEST PATTERNS:
 *     - <R.string.call> <R.string.person> <NAME> (Çağrı yap kişi derya)
 */
public class CallCommand implements Command {

    // REQUEST NAME IDs
    public static final int REQUEST_CALL = R.string.call;
    // REQUEST PARAM NAME IDs
    public static final int REQUEST_PARAM_PERSON = R.string.person;
    // PARAMS NAMES
    public static final String PARAM_NAME = "name";

    // REQUEST NAME IDs
    public static final int REQUEST_MUSIC_PLAYER = R.string.call;

    private SpeechActivity mActivity;
    private Map<String, String> mParams;
    private MyTextToSpeech mTts;


    public CallCommand(SpeechActivity mActivity, Map<String, String> mParams) {
        this.mActivity = mActivity;
        this.mParams = mParams;
        mTts = mActivity.getTextToSpeech();
    }

    @Override
    public void execute() {
        /*
        call a person
         */
        String personName = mParams.get(PARAM_NAME);

        // find the phone number of person
        String phoneNumber = null;
        Cursor cursor = null;
        try {
            cursor = mActivity.getContentResolver().query(
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
        // call the person
        if (phoneNumber != null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            mActivity.startActivity(callIntent);
        } else {
            mTts.speak(R.string.could_not_find_the_number_calling_failed, MyTextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
