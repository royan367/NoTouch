package org.idr.notouch.app.engine;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;

import org.idr.notouch.app.R;
import org.idr.notouch.app.analyzer.SpeechActivity;
import org.idr.notouch.app.speech.MyTextToSpeech;
import org.idr.notouch.app.speech.SpeechToText;

import java.util.Map;

/**
 * Created by ismail ARILIK on 23.04.2014.
 * implements the command pattern
 * RUNS THESE REQUEST PATTERNS:
 *     - <R.string.send_message> <R.string.person> <NAME> <R.string.message> <MESSAGE> (mesaj gönder kişi derya mesaj selam)
 */
public class SendMessageCommand implements Command, SpeechToText.OnTextReceivedListener,
        TextToSpeech.OnUtteranceCompletedListener {

    // REQUEST NAME IDs
    public static final int REQUEST_SEND_MESSAGE = R.string.send_message;
    // REQUEST PARAM NAME IDs
    public static final int REQUEST_PARAM_PERSON = R.string.person;
    public static final int REQUEST_PARAM_MESSAGE = R.string.message;
    // PARAMS NAMES
    public static final String PARAM_NAME = "name";
    public static final String PARAM_MESSAGE = "message";

    private SpeechActivity mActivity;
    private Map<String, String> mParams;
    private MyTextToSpeech mTts;
    private SpeechToText mSpeechToText;


    public SendMessageCommand(SpeechActivity activity, Map<String, String> params) {
        mActivity = activity;
        mParams = params;
        mTts = mActivity.getTextToSpeech();
        mSpeechToText = mActivity.getSpeechToText();
    }


    @Override
    public void execute() {
        /*
        send a message to a person
        */
        // SEND THE MESSAGE
        String personName = mParams.get(PARAM_NAME);
        String message = mParams.get(PARAM_MESSAGE);

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

        if (phoneNumber != null) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            mTts.speak(R.string.message_sent, MyTextToSpeech.QUEUE_FLUSH, null, null, true);
        } else {
            mTts.speak(R.string.could_not_find_the_number_sending_sms_failed,
                    MyTextToSpeech.QUEUE_FLUSH, null, null, true);
        }
        // TODO yukarıdakiyle değiştirildi
        // first ask for sending message to the user
        //mTts.speak(R.string.is_message_being_sent, MyTextToSpeech.QUEUE_FLUSH, null, this, true);
    }

    @Override
    public void onTextReceived(String text) {

        // if the user says YES
        if (text.equalsIgnoreCase(mActivity.getString(android.R.string.yes))) {
            // SEND THE MESSAGE
            String personName = mParams.get(PARAM_NAME);
            String message = mParams.get(PARAM_MESSAGE);

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

            if (phoneNumber != null) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                mTts.speak(R.string.message_sent, MyTextToSpeech.QUEUE_FLUSH, null, null, true);
            } else {
                mTts.speak(R.string.could_not_find_the_number_sending_sms_failed,
                        MyTextToSpeech.QUEUE_FLUSH, null, null, true);
            }
        }
        // if the user says NO
        else if (text.equalsIgnoreCase(mActivity.getString(android.R.string.no))) {
            // DO NOT SEND THE MESSAGE AND DISPLAY AN INFO FOR THE USER
            mTts.speak(R.string.message_not_sent, MyTextToSpeech.QUEUE_FLUSH, null, null, true);
        }
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        mSpeechToText.start(this);
    }
}
