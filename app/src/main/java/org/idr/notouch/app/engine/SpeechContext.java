package org.idr.notouch.app.engine;

import java.util.List;

/**
 * Created by ms on 16.04.2014.
 */
public class SpeechContext implements SpeechContextImpl {

    private List<Action> mActions;
    private String mTag;

    public SpeechContext(List<Action> actions) {
        mActions = actions;
    }

    public SpeechContext(List<Action> actions, String tag) {
        mActions = actions;
        mTag = tag;
    }

    @Override
    public List<Action> getActions() {
        return mActions;
    }

    @Override
    public void addAction(Action action) {
        mActions.add(action);
    }

    @Override
    public void removeAction(Action action) {
        mActions.remove(action);
    }

    @Override
    public Action findActionByName(int nameId) {
        for (Action action : mActions) {
            if (action.getName() == nameId) {
                return action;
            }
        }
        return null;
    }

    @Override
    public String getTag() {
        return mTag;
    }

    @Override
    public void setTag(String tag) {
        mTag = tag;
    }
}
