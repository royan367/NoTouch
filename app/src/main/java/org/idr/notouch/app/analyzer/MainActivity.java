package org.idr.notouch.app.analyzer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.color.speechbubble.AwesomeAdapter;
import com.color.speechbubble.Message;

import org.idr.notouch.app.AboutActivity;
import org.idr.notouch.app.HelpActivity;
import org.idr.notouch.app.R;
import org.idr.notouch.app.SettingsActivity;
import org.idr.notouch.app.engine.AlarmCommand;
import org.idr.notouch.app.engine.CallCommand;
import org.idr.notouch.app.engine.Command;
import org.idr.notouch.app.engine.MusicPlayerCommand;
import org.idr.notouch.app.engine.SendMessageCommand;
import org.idr.notouch.app.engine.TalkCommand;
import org.idr.notouch.app.utils.LocaleUtils;
import org.idr.notouch.app.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends SpeechActivity implements TextToSpeech.OnInitListener,
        RecognitionListener, View.OnClickListener {

    private static final int ERROR_RECOGNITION_NOT_AVAILABLE = 0;
    private static final int TEXT_TO_SPEECH_NOT_INITIALIZED = 100;
    private static final String TAG = MainActivity.class.getSimpleName();

    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private AnalyzerEngine mAnalyzer;
    private ImageButton btnMicrophone;
    private BaseAdapter listAdapter;
    private ListView listView;
    private ArrayList<Message> msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMicrophone = (ImageButton) findViewById(R.id.btn_microphone);
        listView = (ListView) findViewById(R.id.list);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        textToSpeech = new TextToSpeech(this, this);
        mAnalyzer = new AnalyzerEngine(this);

        msgs = new ArrayList<Message>();
        listAdapter = new AwesomeAdapter(this, msgs);
        listView.setAdapter(listAdapter);
        btnMicrophone.setOnClickListener(this);

        // TODO dil değiştirme özelliği yapıldığında aç, doğru çalıştığından da emin ol
        /*// set locale of the application if the user set one before
        SharedPreferences prefs = SharedPreferencesUtils.getDefaultSharedPreferences(getApplicationContext());
        String lang = prefs.getString(SharedPreferencesUtils.KEY_LANGUAGE, null);
        if (lang != null) {
            LocaleUtils.setLocale(getApplicationContext(), lang);
        }*/
    }

    @Override
    protected void onRestart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        speechRecognizer.setRecognitionListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        textToSpeech.stop();
        speechRecognizer.stopListening();
        speechRecognizer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
        speechRecognizer.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_help:
                Intent helpIntent = new Intent(this, HelpActivity.class);
                startActivity(helpIntent);
                break;
            case R.id.action_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_microphone:
                if (SpeechRecognizer.isRecognitionAvailable(this)) {
                    speechRecognizer.startListening(new Intent(RecognizerIntent
                            .ACTION_RECOGNIZE_SPEECH));
                } else {
                    onError(ERROR_RECOGNITION_NOT_AVAILABLE);
                }
                showLoadingAnimation();
                break;
            default:
                break;
        }
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link android.speech.tts.TextToSpeech#SUCCESS} or {@link android.speech.tts.TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {

    }

    public void onTextReceived(String text) {
        // stop loading animation back of the microphone first
        stopLoadingAnimation();
        // write the text user said to the appropriate text view
        writeToListView(text, true);

        Request userRequest = mAnalyzer.analyze(text);

        if (userRequest != null) {
            // if 'userRequest' is a 'Send Message' command
            if (userRequest.getNameId() == SendMessageCommand.REQUEST_SEND_MESSAGE) {
                // generate the command and run it!
                Command sendMsgCmd = new SendMessageCommand(this, userRequest.getParams());
                sendMsgCmd.execute();
            }
            // if 'userRequest' is a 'Call' command
            else if (userRequest.getNameId() == CallCommand.REQUEST_CALL) {
                // generate the command and run it!
                Command callCmd = new CallCommand(this, userRequest.getParams());
                callCmd.execute();
            }
            // if 'userRequest' is a 'Music Player' command
            else if (userRequest.getNameId() == MusicPlayerCommand.REQUEST_PLAY_MUSIC) {
                // generate the command and run it!
                Command music = new MusicPlayerCommand(this, userRequest.getParams(), getContentResolver());
                music.execute();
            }
            // if 'userRequest' is a 'Set Alarm' command
            else if (userRequest.getNameId() == AlarmCommand.REQUEST_SET_ALARM) {
                // generate the command and run it!
                Command alarm = new AlarmCommand(this, userRequest.getParams());
                alarm.execute();
            }
            // if 'userRequest' is a 'Hello' command
            else if (userRequest.getNameId() == TalkCommand.REQUEST_SAY_HELLO) {
                // generate the command and run it!
                Command say_hello = new TalkCommand(this, userRequest.getParams());
                say_hello.execute();
                textToSpeech.speak(getString(R.string.say_hello), TextToSpeech.QUEUE_FLUSH, null);
                writeToListView(R.string.say_hello, false);
            }
            // if 'userRequest' is a 'Say Hi' command
            else if (userRequest.getNameId() == TalkCommand.REQUEST_SAY_HI1) {
                // generate the command and run it!
                Command say_hi = new TalkCommand(this, userRequest.getParams());
                say_hi.execute();
                textToSpeech.speak(getString(R.string.say_hi2), TextToSpeech.QUEUE_FLUSH, null);
                writeToListView(R.string.say_hi2, false);

            }
           // if 'userRequest' is a 'Say idiot' command
            else if (userRequest.getNameId() == TalkCommand.REQUEST_SAY_IDIOT) {
                // generate the command and run it!
                Command say_idiot = new TalkCommand(this, userRequest.getParams());
                say_idiot.execute();
                textToSpeech.speak(getString(R.string.idiot1), TextToSpeech.QUEUE_FLUSH, null);
                writeToListView(R.string.idiot1, false);
            }
           // if 'userRequest' is a 'Say how are you?' command
            else if (userRequest.getNameId() == TalkCommand.REQUEST_SAY_HOW) {
                // generate the command and run it!
                Command say_how = new TalkCommand(this, userRequest.getParams());
                say_how.execute();
                textToSpeech.speak(getString(R.string.how1), TextToSpeech.QUEUE_FLUSH, null);
                writeToListView(R.string.how1, false);
            }
            // if 'userRequest' is a 'Say what's up?' command
            else if (userRequest.getNameId() == TalkCommand.REQUEST_SAY_WHAT) {
                // generate the command and run it!
                Command say_what = new TalkCommand(this, userRequest.getParams());
                say_what.execute();
                textToSpeech.speak(getString(R.string.what1), TextToSpeech.QUEUE_FLUSH, null);
                writeToListView(R.string.what1, false);
            }

        } else {
            textToSpeech.speak(getString(R.string.i_could_not_understand), TextToSpeech.QUEUE_FLUSH,
                    null);
            writeToListView(R.string.i_could_not_understand, false);
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.e(TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.e(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        //Log.e(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.e(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.e(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int errorCode) {
        // stop loading animation back of the microphone first
        stopLoadingAnimation();
        // handle speech errors then
        String errorStr = "";
        switch (errorCode) {
            case ERROR_RECOGNITION_NOT_AVAILABLE:
                errorStr = "ERROR_RECOGNITION_NOT_AVAILABLE";
                break;
            case SpeechRecognizer.ERROR_AUDIO:
                errorStr = "ERROR_AUDIO";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                errorStr = "ERROR_CLIENT";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                errorStr = "ERROR_INSUFFICIENT_PERMISSIONS";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                errorStr = "ERROR_NETWORK";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                errorStr = "ERROR_NETWORK_TIMEOUT";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                errorStr = "ERROR_NO_MATCH";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                errorStr = "ERROR_RECOGNIZER_BUSY";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                errorStr = "ERROR_SERVER";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                errorStr = "ERROR_SPEECH_TIMEOUT";
                break;
            case TextToSpeech.ERROR:
                errorStr = "ERROR_(TEXT_TO_SPEECH)";
                break;
            case TEXT_TO_SPEECH_NOT_INITIALIZED:
                errorStr = "TEXT_TO_SPEECH_NOT_INITIALIZED";
                break;
            default:
                break;
        }
        // inform the developer finally
        Log.e(TAG, "onError: " + errorStr);
    }

    @Override
    public void onResults(Bundle results) {
        List<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String dbgResult = "<empty>";
        if (resultList != null && resultList.size() >= 1) {
            dbgResult = resultList.get(0);
            onResults(resultList.get(0), false);
        }
        Log.e(TAG, "onResults: " + dbgResult);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        List<String> resultList = partialResults.getStringArrayList(SpeechRecognizer
                .RESULTS_RECOGNITION);
        String dbgResult = "<empty>";
        if (resultList != null && resultList.size() >= 1) {
            dbgResult = resultList.get(0);
            onResults(resultList.get(0), true);
        }
        Log.e(TAG, "onPartialResults: " + dbgResult);
    }

    private void onResults(String text, boolean partial) {
        if (text != null) {
            onTextReceived(text);
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.e(TAG, "onEvent");
    }

    public void writeToListView(int strId, boolean isMine) {
        writeToListView(getString(strId), isMine);
    }

    public void writeToListView(String str, boolean isMine) {
        Message msg = new Message(str, isMine);
        msgs.add(msg);
        listAdapter.notifyDataSetChanged();
        listView.setSelection(listAdapter.getCount() - 1);
    }

    private void showLoadingAnimation() {
        // start loading animation
        ImageButton btn = (ImageButton) this.findViewById(R.id.btn_microphone);
        ImageButton btnAnim = (ImageButton) this.findViewById(R.id.btn_microphone_animation);
        btnAnim.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        assert anim != null;
        btnAnim.startAnimation(anim);
    }

    private void stopLoadingAnimation() {
        // stop loading animation
        ImageButton btnAnim = (ImageButton) this.findViewById(R.id.btn_microphone_animation);
        btnAnim.clearAnimation();
        btnAnim.setVisibility(View.INVISIBLE);
    }
}
