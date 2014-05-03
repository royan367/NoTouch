package org.idr.notouch.app.engine;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.telephony.SmsManager;

import org.idr.notouch.app.R;
import org.idr.notouch.app.analyzer.MainActivity;
import org.idr.notouch.app.analyzer.SpeechActivity;
import org.idr.notouch.app.speech.MyTextToSpeech;
import org.idr.notouch.app.speech.SpeechToText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ismail ARILIK on 23.04.2014.
 * implements the command pattern
 * RUNS THESE REQUEST PATTERNS:
 *     - <R.string.send_message> <R.string.person> <NAME> <R.string.message> <MESSAGE> (mesaj gönder kişi derya mesaj selam)
 *     - <R.string.send_message> (mesaj gönder)
 */
public class SendMessageCommand implements Command, RecognitionListener,
        TextToSpeech.OnUtteranceCompletedListener, TextToSpeech.OnInitListener {

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
    private TextToSpeech mTts;
    private SpeechRecognizer mSpeechRecognizer;

    private enum State {PERSON, MESSAGE, VALIDATE}
    private State state;

    private String person;
    private String message;

    private final String UTTERANCE_ID = SendMessageCommand.class.getSimpleName() + "Utterance";
    private HashMap<String, String> ttsSpeakParams = new HashMap<String, String>(1);


    public SendMessageCommand(SpeechActivity activity, Map<String, String> params) {
        mActivity = activity;
        mParams = params;
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mActivity);
        mSpeechRecognizer.setRecognitionListener(this);

        ttsSpeakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID);
    }


    @Override
    public void execute() {
        mTts = new TextToSpeech(mActivity, this);
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSpeechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (Build.VERSION.SDK_INT >= 15) {
            mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {

                }

                @Override
                public void onDone(String utteranceId) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSpeechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
                        }
                    });
                }

                @Override
                public void onError(String utteranceId) {

                }
            });
        } else {
            mTts.setOnUtteranceCompletedListener(this);
        }

        /*
        send a message to a person
        */
        if (mParams == null) {    // if the user said only "Send Message"
            state = State.PERSON;
            mTts.speak(mActivity.getString(R.string.whom), TextToSpeech.QUEUE_FLUSH, ttsSpeakParams);
            ((MainActivity) mActivity).writeToListView(R.string.whom, false);
        } else {    // if the user said "Send Message" with person and/or message parameters
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
                mTts.speak(mActivity.getString(R.string.message_sent), MyTextToSpeech.QUEUE_FLUSH,
                        null);
                ((MainActivity) mActivity).writeToListView(R.string.message_sent, false);
            } else {
                mTts.speak(mActivity.getString(R.string.could_not_find_the_number_sending_sms_failed),
                        MyTextToSpeech.QUEUE_FLUSH, null);
                ((MainActivity) mActivity).writeToListView(R.string.could_not_find_the_number_sending_sms_failed, false);
            }
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        List<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = resultList.get(0);
        ((MainActivity) mActivity).writeToListView(text, true);
        switch (state) {
            case PERSON:
                person = text;
                state = State.MESSAGE;
                mTts.speak(mActivity.getString(R.string.message), TextToSpeech.QUEUE_FLUSH, ttsSpeakParams);
                ((MainActivity) mActivity).writeToListView(R.string.message, false);
                break;
            case MESSAGE:
                message = text;
                state = State.VALIDATE;
                mTts.speak(mActivity.getString(R.string.is_message_sent), TextToSpeech.QUEUE_FLUSH, ttsSpeakParams);
                ((MainActivity) mActivity).writeToListView(R.string.is_message_sent, false);
                break;
            case VALIDATE:
                if (text.equalsIgnoreCase(mActivity.getString(R.string.yes))) {
                    SmsManager smsManager = SmsManager.getDefault();
                    // TODO getPhoneNumberFromPersonName metodundan dönen değerin null olup olmadığını kontrol et
                    smsManager.sendTextMessage(getPhoneNumberFromPersonName(person), null, message, null, null);
                    mTts.speak(mActivity.getString(R.string.message_sent), MyTextToSpeech.QUEUE_FLUSH,
                            null);
                    ((MainActivity) mActivity).writeToListView(R.string.message_sent, false);
                } else if (text.equalsIgnoreCase(mActivity.getString(R.string.no))) {
                    mTts.speak(mActivity.getString(R.string.message_not_sent),
                            MyTextToSpeech.QUEUE_FLUSH, null);
                    ((MainActivity) mActivity).writeToListView(R.string.message_not_sent, false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    private String getPhoneNumberFromPersonName(String personName) {
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

        return phoneNumber;
    }
}
