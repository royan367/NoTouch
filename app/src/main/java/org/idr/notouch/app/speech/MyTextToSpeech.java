package org.idr.notouch.app.speech;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;

/**
 * Created by ms on 12.04.2014.
 * implements singleton pattern, use getInstance static method to get THE instance
 */
public class MyTextToSpeech extends TextToSpeech {

    private Context mAppContext;
    private OnErrorListener mOnErrorListener;

    private static MyTextToSpeech instance;


    public static MyTextToSpeech getInstance(Context appContext, OnInitListener onInitListener,
                                             OnErrorListener onErrorListener) {
        if (instance == null) {
            instance = new MyTextToSpeech(appContext, onInitListener, onErrorListener);
        }
        return instance;
    }

    private MyTextToSpeech(Context appContext, OnInitListener listener,
                           OnErrorListener onErrorListener) {
        super(appContext, listener);
        mAppContext = appContext;
        mOnErrorListener = onErrorListener;
    }


    // TODO hataları daha iyi işleyebilmek için utterance progress listener kullan
    public void speak(int textId, int queueMode, HashMap<String, String> params) {
        int resultCode = speak(mAppContext.getString(textId), queueMode, params);
        if (resultCode == ERROR) {
            mOnErrorListener.onError(ERROR);
        }
    }
}
