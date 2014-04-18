package org.idr.notouch.app.engine;

/**
 * Created by ms on 16.04.2014.
 */
public interface SpeechContextManagerImpl {
    public SpeechContextImpl getGlobalSpeechContext();
    public SpeechContextImpl getMainContext();
    public SpeechContextImpl getCurrentContext();
    public SpeechContextImpl getPrevContext();
    public void changeLocalContext(SpeechContextImpl speechContext);
}
