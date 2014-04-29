package org.idr.notouch.app.speech;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import org.idr.notouch.app.analyzer.MainActivity;
import org.idr.notouch.app.analyzer.SpeechActivity;

import java.util.HashMap;

/**
 * Created by ms on 12.04.2014.
 * implements singleton pattern, use getInstance static method to get THE instance
 */
public class MyTextToSpeech extends TextToSpeech {

    private Context mAppContext;
    private SpeechActivity mActivity;
    private OnErrorListener mOnErrorListener;

    private static MyTextToSpeech instance;


    public static MyTextToSpeech getInstance(Context appContext, SpeechActivity activity,
                                             OnInitListener onInitListener,
                                             OnErrorListener onErrorListener) {
        if (instance == null) {
            instance = new MyTextToSpeech(appContext, activity, onInitListener, onErrorListener);
        }
        return instance;
    }

    private MyTextToSpeech(Context appContext, SpeechActivity activity, OnInitListener listener,
                           OnErrorListener onErrorListener) {
        super(appContext, listener);
        mAppContext = appContext;
        mActivity = activity;
        mOnErrorListener = onErrorListener;
    }

    // TODO hataları daha iyi işleyebilmek için utterance progress listener kullan
    public void speak(int textId, int queueMode, HashMap<String, String> params,
                      OnUtteranceCompletedListener onUtteranceCompletedListener,
                      boolean writeToOutput) {
        // write the speech as a text to the output text view
        if (writeToOutput) {
            ((MainActivity) mActivity).writeOutput(textId);
        }
        // set the param utterance completed listener to this instance of MyTextToSpeech class
        setOnUtteranceCompletedListener(onUtteranceCompletedListener);
        // speak and handle possible errors
        int resultCode = speak(mAppContext.getString(textId), queueMode, params);
        if (resultCode == ERROR) {
            mOnErrorListener.onError(ERROR);
        }
    }
}
