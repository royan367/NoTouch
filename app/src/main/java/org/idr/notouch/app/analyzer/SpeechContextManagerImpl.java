package org.idr.notouch.app.analyzer;

/**
 * Created by ms on 16.04.2014.
 */
public interface SpeechContextManagerImpl {
    SpeechContextImpl getGlobalSpeechContext();
    SpeechContextImpl getMainContext();
    SpeechContextImpl getCurrentContext();
    SpeechContextImpl getPrevContext();
    void changeLocalContext(SpeechContextImpl speechContext);
}
