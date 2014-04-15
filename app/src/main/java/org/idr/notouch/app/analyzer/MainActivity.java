package org.idr.notouch.app.analyzer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.idr.notouch.app.R;
import org.idr.notouch.app.speech.SpeechToText;
import org.idr.notouch.app.speech.TextToSpeech;


public class MainActivity extends SpeechActivity implements SpeechToText.OnTextReceivedListener,
        SpeechToText.OnErrorListener {

    private SpeechToText speechToText;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechToText = SpeechToText.getInstance(getApplicationContext(), this, this);
        speechToText.start();
    }

    @Override
    protected void onRestart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechToText.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onTextReceived(String text) {

    }

    @Override
    public void onError(int errorCode) {
        // handle speech recognition errors here
        switch (errorCode) {
            case SpeechToText.ERROR_RECOGNITION_NOT_AVAILABLE:
                break;
            case SpeechToText.ERROR_AUDIO:
                break;
            case SpeechToText.ERROR_CLIENT:
                break;
            case SpeechToText.ERROR_INSUFFICIENT_PERMISSIONS:
                break;
            case SpeechToText.ERROR_NETWORK:
                break;
            case SpeechToText.ERROR_NETWORK_TIMEOUT:
                break;
            case SpeechToText.ERROR_NO_MATCH:
                break;
            case SpeechToText.ERROR_RECOGNIZER_BUSY:
                break;
            case SpeechToText.ERROR_SERVER:
                break;
            case SpeechToText.ERROR_SPEECH_TIMEOUT:
                break;
        }
    }
}
