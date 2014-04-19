package org.idr.notouch.app.engine;

/**
 * Created by ms on 16.04.2014.
 */
public class SpeechContextManager implements SpeechContextManagerImpl {

    public SpeechContextImpl mGlobalSpeechContext;
    public SpeechContextImpl mMainSpeechContext;
    public SpeechContextImpl mCurrentSpeechContext;

    /**
     *
     * @param globalSpeechContext global context for the speech commands
     * @param mainSpeechContext local context for the speech commands
     */
    public SpeechContextManager(SpeechContextImpl globalSpeechContext,
                                SpeechContextImpl mainSpeechContext) {
        mGlobalSpeechContext = globalSpeechContext;
        mMainSpeechContext = mainSpeechContext;
        mCurrentSpeechContext = mMainSpeechContext;
    }

    @Override
    public SpeechContextImpl getGlobalSpeechContext() {
        return mGlobalSpeechContext;
    }

    @Override
    public SpeechContextImpl getMainContext() {
        return mMainSpeechContext;
    }

    @Override
    public SpeechContextImpl getCurrentContext() {
        return mCurrentSpeechContext;
    }

    // TODO implement et

    /**
     * NOT IMPLEMENTED YET
     * @return an error
     */
    @Override
    public SpeechContextImpl getPrevContext() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void changeLocalContext(SpeechContextImpl speechContext) {
        mCurrentSpeechContext = speechContext;
    }
}
