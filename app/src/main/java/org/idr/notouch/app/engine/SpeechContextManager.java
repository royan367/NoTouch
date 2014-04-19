package org.idr.notouch.app.engine;

import java.util.List;
import java.util.Stack;

/**
 * Created by ms on 16.04.2014.
 */
public class SpeechContextManager implements SpeechContextManagerImpl {

    private SpeechContextImpl mGlobalSpeechContext;
    private List<SpeechContextImpl> mLocalSpeechContexts;
    private SpeechContextImpl mMainSpeechContext;
    private SpeechContextImpl mCurrentSpeechContext;
    private Stack<SpeechContextImpl> mLocalsBackStack;


    /**
     * assume the param localSpeechContexts contains at least one element
     * @param globalSpeechContext global context for the speech commands
     * @param localSpeechContexts local contexts for the speech commands
     */
    public SpeechContextManager(SpeechContextImpl globalSpeechContext,
                                List<SpeechContextImpl> localSpeechContexts) {
        mGlobalSpeechContext = globalSpeechContext;
        mLocalSpeechContexts = localSpeechContexts;
        mMainSpeechContext = mLocalSpeechContexts.get(0);
        mCurrentSpeechContext = mMainSpeechContext;
        mLocalsBackStack = new Stack<SpeechContextImpl>();
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
        if (mLocalsBackStack.size() >= 1) {
            return mLocalsBackStack.pop();
        }
        return null;
    }

    @Override
    public void addSpeechContext(SpeechContextImpl speechContext) {
        mLocalSpeechContexts.add(speechContext);
    }

    @Override
    public void removeSpeechContext(SpeechContextImpl speechContext) {
        mLocalSpeechContexts.remove(speechContext);
    }

    @Override
    public void changeLocalContext(SpeechContextImpl speechContext) {
        mLocalsBackStack.push(speechContext);
        mCurrentSpeechContext = speechContext;
    }

    @Override
    public SpeechContextImpl findContextByTag(String tag) {
        for (SpeechContextImpl speechContext : mLocalSpeechContexts) {
            if (speechContext.getTag().equals(tag)) {
                return speechContext;
            }
        }
        return null;
    }
}
