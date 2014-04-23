package org.idr.notouch.app.analyzer;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import org.idr.notouch.app.engine.SpeechContextManagerImpl;
import org.idr.notouch.app.speech.MyTextToSpeech;
import org.idr.notouch.app.speech.OnErrorListener;
import org.idr.notouch.app.speech.SpeechToText;

/**
 * Created by ms on 14.04.2014.
 */
public abstract class SpeechActivity extends Activity implements
        SpeechToText.OnTextReceivedListener, OnErrorListener, TextToSpeech.OnInitListener {

    private SpeechContextManagerImpl speechContextManager;
    private SpeechToText speechToText;
    private MyTextToSpeech textToSpeech;

    protected SpeechActivity() {
        speechContextManager = onGenerateSpeechContextManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speechToText = SpeechToText.getInstance(getApplicationContext(), this, this);
        textToSpeech = MyTextToSpeech.getInstance(getApplicationContext(), this, this);
    }

    protected SpeechContextManagerImpl getSpeechContextManager() {
        return speechContextManager;
    }

    public SpeechToText getSpeechToText() {
        return speechToText;
    }

    public MyTextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    abstract protected SpeechContextManagerImpl onGenerateSpeechContextManager();
}
