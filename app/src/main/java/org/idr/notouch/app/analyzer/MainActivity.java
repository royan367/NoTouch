package org.idr.notouch.app.analyzer;

import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.MenuItem;

import org.idr.notouch.app.R;
import org.idr.notouch.app.engine.Action;
import org.idr.notouch.app.engine.SpeechContext;
import org.idr.notouch.app.engine.SpeechContextImpl;
import org.idr.notouch.app.engine.SpeechContextManager;
import org.idr.notouch.app.engine.SpeechContextManagerImpl;
import org.idr.notouch.app.speech.SpeechToText;
import org.idr.notouch.app.speech.TextToSpeech;

import java.util.ArrayList;
import java.util.List;


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
    protected SpeechContextManagerImpl onGenerateSpeechContextManager() {
        // TODO @derya burada SpeechContextManager ı oluştur
        // TODO SpeechContextManager içine SpeechContext leri koyman gerek (global ve local olarak)
        // TODO SpeechContext lerin içine de Action lar koyman gerek

        final List<Action> actions=new ArrayList<Action>();
        actions.add(new Action(R.string.bye_buddy,new Action.ActionCallback() {
            @Override
            public void onAction() {
                finish();
            }
        }));
        actions.add(new Action(R.string.back,new Action.ActionCallback() {
            @Override
            public void onAction() {

                SpeechContextImpl context=getSpeechContextManager().getPrevContext();
                getSpeechContextManager().changeLocalContext(context);
            }
        }));

        actions.add(new Action(R.string.main_menu,new Action.ActionCallback() {
            @Override
            public void onAction() {
                SpeechContextImpl context=getSpeechContextManager().getMainContext();
                getSpeechContextManager().changeLocalContext(context);
            }
        }));

        actions.add(new Action(R.string.where,new Action.ActionCallback() {
            @Override
            public void onAction() {

                // TODO konuş
            }
        }));

        actions.add(new Action(R.string.settings,new Action.ActionCallback() {
            @Override
            public void onAction() {
                //TODO settings screen

            }
        }));

        SpeechContextImpl mainSpeechContext=new SpeechContext(null);
        SpeechContextImpl globalSpeechContext=new SpeechContext(actions);
        SpeechContextManager context=new SpeechContextManager(globalSpeechContext, mainSpeechContext);


        return context;
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
