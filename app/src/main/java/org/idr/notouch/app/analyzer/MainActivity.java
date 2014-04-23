package org.idr.notouch.app.analyzer;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import org.idr.notouch.app.R;
import org.idr.notouch.app.engine.Action;
import org.idr.notouch.app.engine.Command;
import org.idr.notouch.app.engine.SendMessageCommand;
import org.idr.notouch.app.engine.SpeechContext;
import org.idr.notouch.app.engine.SpeechContextImpl;
import org.idr.notouch.app.engine.SpeechContextManager;
import org.idr.notouch.app.engine.SpeechContextManagerImpl;
import org.idr.notouch.app.speech.MyTextToSpeech;
import org.idr.notouch.app.speech.OnErrorListener;
import org.idr.notouch.app.speech.SpeechToText;

import java.util.List;

import java.util.ArrayList;


public class MainActivity extends SpeechActivity {

    private static final int TEXT_TO_SPEECH_NOT_INITIALIZED = 100;

    private SpeechToText speechToText;
    private MyTextToSpeech textToSpeech;
    private SpeechContextManagerImpl speechContextManager;
    private AnalyzerEngine mAnalyzer;
    // TEXT_TO_SPEECH_NOT_INITIALIZED or MyTextToSpeech.SUCCESS or MyTextToSpeech.ERROR
    private int ttsStatus = TEXT_TO_SPEECH_NOT_INITIALIZED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializations
        speechToText = getSpeechToText();
        textToSpeech = getTextToSpeech();
        speechContextManager = getSpeechContextManager();
        mAnalyzer = new AnalyzerEngine(this);

        // start listening
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
        Request userRequest = mAnalyzer.analyze(text);

        if (userRequest != null) {
            // if 'userRequest' is a 'Send Message' command
            if (userRequest.getNameId() == SendMessageCommand.REQUEST_SEND_MESSAGE) {
                // generate the command and run it!
                Command sendMsgCmd = new SendMessageCommand(this, userRequest.getParams());
                sendMsgCmd.execute();
            }
        } else {
            // TODO işle
        }

        // TODO gereksizse sil
        /*// get global and CURRENT local speech context
        SpeechContextImpl globalSpeechContext = speechContextManager.getGlobalSpeechContext();
        SpeechContextImpl localSpeechContext = speechContextManager.getCurrentContext();
        // get actions of global and CURRENT local speech contexts
        List<Action> globalActions = globalSpeechContext.getActions();
        List<Action> localActions = localSpeechContext.getActions();
        // find the action related with the text 'text' if the action exists in GLOBAL ACTIONS
        Action actionRun = null;
        for (Action action : globalActions) {
            if (text.equalsIgnoreCase(getString(action.getName()))) {
                actionRun = action;
            }
        }
        // find the action related with the text 'text' if the action exists in LOCAL ACTIONS
        if (actionRun == null) {
            for (Action action : localActions) {
                if (text.equalsIgnoreCase(getString(action.getName()))) {
                    actionRun = action;
                }
            }
        }
        // if it exists in either global or local actions, run it!
        if (actionRun != null) {
            actionRun.run();
        } else {
        }*/
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
            case MyTextToSpeech.ERROR:
                // TODO yazıdan sese dönüşüm hatalarını işle
                break;
            case TEXT_TO_SPEECH_NOT_INITIALIZED:
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
        ttsStatus = status;
    }


    @Override
    protected SpeechContextManagerImpl onGenerateSpeechContextManager() {
        // TODO @derya burada SpeechContextManager ı oluştur
        // TODO SpeechContextManager içine SpeechContext leri koyman gerek (global ve local olarak)
        // TODO SpeechContext lerin içine de Action lar koyman gerek

        // GLOBALS
        final List<Action> globalActions=new ArrayList<Action>();
        globalActions.add(new Action(R.string.bye_buddy, null, false, new Action.ActionCallback() {
            @Override
            public void onAction(Action action) {
                finish();
            }

            @Override
            public void onActionBegin(Action action) {

            }

            @Override
            public void onActionEnd(Action action) {

            }

            @Override
            public void onParamNotSet(Action action) {

            }

            @Override
            public void onError(Action action) {

            }
        }));
        globalActions.add(new Action(R.string.back, null, false, new Action.ActionCallback() {
            @Override
            public void onAction(Action action) {

                SpeechContextImpl context = getSpeechContextManager().getPrevContext();
                getSpeechContextManager().changeLocalContext(context);
            }

            @Override
            public void onActionBegin(Action action) {

            }

            @Override
            public void onActionEnd(Action action) {

            }

            @Override
            public void onParamNotSet(Action action) {

            }

            @Override
            public void onError(Action action) {

            }
        }));
        globalActions.add(new Action(R.string.main_menu, null, false, new Action.ActionCallback() {
            @Override
            public void onAction(Action action) {
                SpeechContextImpl context = getSpeechContextManager().getMainContext();
                getSpeechContextManager().changeLocalContext(context);
            }

            @Override
            public void onActionBegin(Action action) {

            }

            @Override
            public void onActionEnd(Action action) {

            }

            @Override
            public void onParamNotSet(Action action) {

            }

            @Override
            public void onError(Action action) {

            }
        }));
        globalActions.add(new Action(R.string.where, null, false, new Action.ActionCallback() {
            @Override
            public void onAction(Action action) {
                SpeechContextImpl currentContext = getSpeechContextManager().getCurrentContext();
                textToSpeech.speak(action.getName(), TextToSpeech.QUEUE_FLUSH, null);
            }

            @Override
            public void onActionBegin(Action action) {

            }

            @Override
            public void onActionEnd(Action action) {

            }

            @Override
            public void onParamNotSet(Action action) {

            }

            @Override
            public void onError(Action action) {

            }
        }));
        globalActions.add(new Action(R.string.settings, null, false, new Action.ActionCallback() {
            @Override
            public void onAction(Action action) {
                //TODO open settings screen
            }

            @Override
            public void onActionBegin(Action action) {

            }

            @Override
            public void onActionEnd(Action action) {

            }

            @Override
            public void onParamNotSet(Action action) {

            }

            @Override
            public void onError(Action action) {

            }
        }));

        SpeechContextImpl globalSpeechContext=new SpeechContext(globalActions);


        // LOCALS
        List<Action> mainLocalActions = new ArrayList<Action>();
        mainLocalActions.add(new Action(R.string.messages, null, false, new Action.ActionCallback() {
            @Override
            public void onAction(Action action) {

            }

            @Override
            public void onActionBegin(Action action) {

            }

            @Override
            public void onActionEnd(Action action) {

            }

            @Override
            public void onParamNotSet(Action action) {

            }

            @Override
            public void onError(Action action) {

            }
        }));
        SpeechContextImpl mainSpeechContext=new SpeechContext(mainLocalActions);

        List<SpeechContextImpl> localSpeechContexts = new ArrayList<SpeechContextImpl>();
        localSpeechContexts.add(mainSpeechContext);
        SpeechContextManager context=new SpeechContextManager(globalSpeechContext,
                localSpeechContexts);


        return context;
    }
}
