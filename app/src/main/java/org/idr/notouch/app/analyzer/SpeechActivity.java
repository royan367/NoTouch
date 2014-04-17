package org.idr.notouch.app.analyzer;

import android.app.Activity;

import org.idr.notouch.app.engine.SpeechContextManagerImpl;

/**
 * Created by ms on 14.04.2014.
 */
public abstract class SpeechActivity extends Activity {

    private SpeechContextManagerImpl speechContextManager;

    protected SpeechActivity() {
        speechContextManager = onGenerateSpeechContextManager();
    }

    protected SpeechContextManagerImpl getSpeechContextManager() {
        return speechContextManager;
    }

    abstract protected SpeechContextManagerImpl onGenerateSpeechContextManager();
}
