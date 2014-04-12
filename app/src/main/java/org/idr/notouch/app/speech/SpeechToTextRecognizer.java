package org.idr.notouch.app.speech;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import org.idr.notouch.app.R;
import org.idr.notouch.app.analyzer.AnalyzerEngine;

public class SpeechToTextRecognizer extends AsyncTask<Void, Void, Void> {

    private Context appContext;
    private Activity activity;
    private SpeechRecognizer speechRecognizer;
    private AnalyzerEngine analyzerEngine;

    public SpeechToTextRecognizer(Context appContext, Activity activity) {
        this.appContext = appContext;
        this.activity = activity;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.appContext);
        analyzerEngine = new AnalyzerEngine(this.activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (SpeechRecognizer.isRecognitionAvailable(appContext)) {
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
                    StringBuilder sb = new StringBuilder(resultArray.length);
                    for (String result : resultArray) {
                        sb.append(result + " ");
                    }
                    analyzerEngine.analyze(sb.toString());
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
            speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        } else {

        }
    }

    @Override
    protected Void doInBackground(Void... params) {


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
