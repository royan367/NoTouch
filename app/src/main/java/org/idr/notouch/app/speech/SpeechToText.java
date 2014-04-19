package org.idr.notouch.app.speech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.List;

/**
 * Created by ismail ARILIK on 12.04.2014.
 * implements singleton pattern, use getInstance static method to get THE instance
 */
public class SpeechToText {

    private static final String TAG = SpeechToText.class.getSimpleName();

    // error codes
    public static final int ERROR_RECOGNITION_NOT_AVAILABLE = 0;
    public static final int ERROR_AUDIO = SpeechRecognizer.ERROR_AUDIO;
    public static final int ERROR_CLIENT = SpeechRecognizer.ERROR_CLIENT;
    public static final int ERROR_INSUFFICIENT_PERMISSIONS = SpeechRecognizer
            .ERROR_INSUFFICIENT_PERMISSIONS;
    public static final int ERROR_NETWORK = SpeechRecognizer.ERROR_NETWORK;
    public static final int ERROR_NETWORK_TIMEOUT = SpeechRecognizer.ERROR_NETWORK_TIMEOUT;
    public static final int ERROR_NO_MATCH = SpeechRecognizer.ERROR_NO_MATCH;
    public static final int ERROR_RECOGNIZER_BUSY = SpeechRecognizer.ERROR_RECOGNIZER_BUSY;
    public static final int ERROR_SERVER = SpeechRecognizer.ERROR_SERVER;
    public static final int ERROR_SPEECH_TIMEOUT = SpeechRecognizer.ERROR_SPEECH_TIMEOUT;

    private Context appContext;
    private OnTextReceivedListener onTextReceivedListener;
    private OnErrorListener onErrorListener;
    private SpeechRecognizer speechRecognizer;

    private static SpeechToText instance;

    public static SpeechToText getInstance(Context appContext, OnTextReceivedListener onTextReceivedListener,
                                           OnErrorListener onErrorListener) {
        if (instance == null) {
            instance = new SpeechToText(appContext, onTextReceivedListener, onErrorListener);
        }
        return instance;
    }

    private SpeechToText(Context appContext, OnTextReceivedListener onTextReceivedListener,
                        OnErrorListener onErrorListener) {
        this.appContext = appContext;
        this.onTextReceivedListener = onTextReceivedListener;
        this.onErrorListener = onErrorListener;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.appContext);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
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
            public void onError(int error) {
                String errorStr = "";
                if (error == SpeechRecognizer.ERROR_AUDIO) {
                    errorStr = "ERROR_AUDIO";
                    SpeechToText.this.onErrorListener.onError(SpeechRecognizer.ERROR_AUDIO);
                } else if (error == SpeechRecognizer.ERROR_CLIENT) {
                    errorStr = "ERROR_CLIENT";
                    SpeechToText.this.onErrorListener.onError(SpeechRecognizer.ERROR_CLIENT);
                } else if (error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS) {
                    errorStr = "ERROR_INSUFFICIENT_PERMISSIONS";
                    SpeechToText.this.onErrorListener.onError(SpeechRecognizer
                            .ERROR_INSUFFICIENT_PERMISSIONS);
                } else if (error == SpeechRecognizer.ERROR_NETWORK) {
                    errorStr = "ERROR_NETWORK";
                    SpeechToText.this.onErrorListener.onError(SpeechRecognizer.ERROR_NETWORK);
                } else if (error == SpeechRecognizer.ERROR_NETWORK_TIMEOUT) {
                    errorStr = "ERROR_NETWORK_TIMEOUT";
                    SpeechToText.this.onErrorListener.onError(SpeechRecognizer
                            .ERROR_NETWORK_TIMEOUT);
                } else if (error == SpeechRecognizer.ERROR_NO_MATCH) {
                    errorStr = "ERROR_NO_MATCH";
                    SpeechToText.this.onErrorListener.onError(SpeechRecognizer.ERROR_NO_MATCH);
                } else if (error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
                    errorStr = "ERROR_RECOGNIZER_BUSY";
                    SpeechToText.this.onErrorListener.onError(SpeechRecognizer
                            .ERROR_RECOGNIZER_BUSY);
                } else if (error == SpeechRecognizer.ERROR_SERVER) {
                    errorStr = "ERROR_SERVER";
                    SpeechToText.this.onErrorListener.onError(SpeechRecognizer.ERROR_SERVER);
                } else if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
                    errorStr = "ERROR_SPEECH_TIMEOUT";
                    SpeechToText.this.onErrorListener.onError(SpeechRecognizer
                            .ERROR_SPEECH_TIMEOUT);
                }
                Log.e(TAG, "onError: " + errorStr);
                speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
            }

            @Override
            public void onResults(Bundle results) {
                List<String> resultList = results.getStringArrayList(SpeechRecognizer
                        .RESULTS_RECOGNITION);
                String dbgResult = "";
                if (resultList != null && resultList.size() >= 1) {
                    onResult(resultList.get(0), false);
                    dbgResult = resultList.get(0);
                }
                Log.e(TAG, "onResults: " + dbgResult);
                speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                List<String> resultList = partialResults.getStringArrayList(SpeechRecognizer
                        .RESULTS_RECOGNITION);
                String dbgResult = "";
                if (resultList != null && resultList.size() >= 1) {
                    onResult(resultList.get(0), true);
                    dbgResult = resultList.get(0);
                }
                Log.e(TAG, "onPartialResults: " + dbgResult);
                speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
            }

            private void onResult(String text, boolean partial) {
                if (text != null) {
                    SpeechToText.this.onTextReceivedListener.onTextReceived(text);
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.e(TAG, "onEvent");
            }
        });
    }

    public void start() {
        if (SpeechRecognizer.isRecognitionAvailable(appContext)) {
            speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        } else {
            onErrorListener.onError(ERROR_RECOGNITION_NOT_AVAILABLE);
        }
    }

    public void stop() {
        speechRecognizer.stopListening();
    }

    public void destroy() {
        speechRecognizer.destroy();
    }


    public static interface OnTextReceivedListener {
        public void onTextReceived(String text);
    }
}
