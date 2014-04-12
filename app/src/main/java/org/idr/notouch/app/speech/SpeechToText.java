package org.idr.notouch.app.speech;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import org.idr.notouch.app.R;

/**
 * Created by ms on 12.04.2014.
 */
public class SpeechToText {

    // error codes
    public static final int ERROR_RECOGNITION_NOT_AVAILABLE = 0;

    private Context appContext;
    private OnTextReceivedListener onTextReceivedListener;
    private OnErrorListener onErrorListener;
    private SpeechRecognizer speechRecognizer;

    public SpeechToText(Context appContext, OnTextReceivedListener onTextReceivedListener,
                        OnErrorListener onErrorListener) {
        this.appContext = appContext;
        this.onTextReceivedListener = onTextReceivedListener;
        this.onErrorListener = onErrorListener;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.appContext);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
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
                String[] resultArray = results.getStringArray(SpeechRecognizer.RESULTS_RECOGNITION);
                // TODO gereksizse sil
                /*StringBuilder sb = new StringBuilder(resultArray.length);
                for (String result : resultArray) {
                    sb.append(result + " ");
                }*/
                if (resultArray != null && resultArray.length >= 1) {
                    onResult(resultArray[0], false);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                String[] resultArray = partialResults.getStringArray(SpeechRecognizer
                        .RESULTS_RECOGNITION);
                // TODO gereksizse sil
                /*StringBuilder sb = new StringBuilder(resultArray.length);
                for (String result : resultArray) {
                    sb.append(result + " ");
                }*/
                if (resultArray != null && resultArray.length >= 1) {
                    onResult(resultArray[0], true);
                }
            }

            private void onResult(String text, boolean partial) {
                if (text != null) {
                    SpeechToText.this.onTextReceivedListener.onTextReceived(text);
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    public void start() {
        if (SpeechRecognizer.isRecognitionAvailable(appContext)) {
            speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        } else {
            onErrorListener.onError(ERROR_RECOGNITION_NOT_AVAILABLE);
            // TODO gereksizse sil
            /*new AlertDialog.Builder(activity)
                    .setMessage(R.string.ses_tanima_cihazinizda_kullanilamiyor)
                    .setNeutralButton(R.string.kapat, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();*/
        }
    }

    public void stop() {
        speechRecognizer.stopListening();
    }


    public static interface OnTextReceivedListener {
        void onTextReceived(String text);
    }

    public static interface OnErrorListener {
        void onError(int errorCode);
    }
}
