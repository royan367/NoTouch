package org.idr.notouch.app.analyzer;

import android.app.Activity;

import java.util.List;

/**
 * Created by ms on 14.04.2014.
 */
public abstract class SpeechActivity extends Activity {

    private SpeechContextManagerImpl speechContextManager;

    protected SpeechActivity() {
        speechContextManager = new SpeechContextManager();
    }

    protected SpeechContextManagerImpl getSpeechContextManager() {
        return speechContextManager;
    }
}
