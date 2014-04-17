package org.idr.notouch.app.analyzer;

/**
 * Created by ms on 16.04.2014.
 */
public class SpeechContextManager implements SpeechContextManagerImpl {

    private SpeechContextImpl mGlobalSpeechContext;
    private SpeechContextImpl mMainSpeechContext;
    private SpeechContextImpl mCurrentSpeechContext;
    private SpeechContextImpl mPrevSpeechContext;

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
        mPrevSpeechContext = null;
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

    @Override
    public SpeechContextImpl getPrevContext() {
        return mPrevSpeechContext;
    }

    @Override
    public void changeLocalContext(SpeechContextImpl speechContext) {
        mPrevSpeechContext = mCurrentSpeechContext;
        mCurrentSpeechContext = speechContext;
    }
}
