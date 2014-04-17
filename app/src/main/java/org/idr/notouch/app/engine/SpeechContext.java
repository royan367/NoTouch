package org.idr.notouch.app.engine;

import java.util.List;

/**
 * Created by ms on 16.04.2014.
 */
public class SpeechContext implements SpeechContextImpl {

    private List<Action> mActions;

    public SpeechContext(List<Action> actions) {
        mActions = actions;
    }

    @Override
    public List<Action> getActions() {
        return mActions;
    }

    @Override
    public void setActions(List<Action> actions) {
        mActions = actions;
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
}
