package org.idr.notouch.app.engine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import org.idr.notouch.app.R;
import org.idr.notouch.app.analyzer.MainActivity;
import org.idr.notouch.app.analyzer.SpeechActivity;
import org.idr.notouch.app.utils.ContactUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ismail ARILIK on 26.04.2014.
 * implements the command pattern
 * RUNS THESE REQUEST PATTERNS:
 *     - <R.string.call> <R.string.person> <NAME> (Çağrı yap kişi derya)
 *     - <R.string.call> (Çağrı yap)
 */
public class CallCommand implements Command, RecognitionListener,
        TextToSpeech.OnUtteranceCompletedListener, TextToSpeech.OnInitListener {

    // REQUEST NAME IDs
    public static final int REQUEST_CALL = R.string.call;
    // REQUEST PARAM NAME IDs
    public static final int REQUEST_PARAM_PERSON = R.string.person;
    // PARAMS NAMES
    public static final String PARAM_NAME = "name";

    private SpeechActivity mActivity;
    private Map<String, String> mParams;
    private TextToSpeech mTts;
    private SpeechRecognizer mSpeechRecognizer;

    private enum State {PERSON, VALIDATE}
    private State state;

    private String person;

    private final String UTTERANCE_ID = CallCommand.class.getSimpleName() + "Utterance";
    private HashMap<String, String> ttsSpeakParams = new HashMap<String, String>(1);


    public CallCommand(SpeechActivity mActivity, Map<String, String> mParams) {
        this.mActivity = mActivity;
        this.mParams = mParams;
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

        if (mParams == null) {    // if the user said only "Call"
            state = State.PERSON;
            mTts.speak(mActivity.getString(R.string.whom), TextToSpeech.QUEUE_FLUSH, ttsSpeakParams);
            ((MainActivity) mActivity).writeToListView(R.string.whom, false);
        } else {    // if the user said "Call" with person parameter
            // CALL THE PERSON
            String personName = mParams.get(PARAM_NAME);

            // TODO ContactUtils teki metodla yap
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
                mTts.speak(mActivity.getString(R.string.could_not_find_the_number_calling_failed),
                        TextToSpeech.QUEUE_FLUSH, null);
                ((MainActivity) mActivity).writeToListView(
                        R.string.could_not_find_the_number_calling_failed, false);
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
                state = State.VALIDATE;
                mTts.speak(mActivity.getString(R.string.is_the_person_called),
                        TextToSpeech.QUEUE_FLUSH, ttsSpeakParams);
                ((MainActivity) mActivity).writeToListView(R.string.is_the_person_called, false);
                break;
            case VALIDATE:
                if (text.equalsIgnoreCase(mActivity.getString(R.string.yes))) {
                    // TODO kişi ya da numarayı kontrol et
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + ContactUtils.getPhoneNumberFromPersonName(
                            mActivity.getApplicationContext(), person)));
                    mActivity.startActivity(callIntent);
                } else if (text.equalsIgnoreCase(mActivity.getString(R.string.no))) {
                    mTts.speak(mActivity.getString(R.string.call_does_not_have_made),
                            TextToSpeech.QUEUE_FLUSH, null);
                    ((MainActivity) mActivity).writeToListView(
                            R.string.call_does_not_have_made, false);
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
}
