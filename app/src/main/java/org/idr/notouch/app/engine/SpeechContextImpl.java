package org.idr.notouch.app.engine;

import java.util.List;

/**
 * Created by ms on 16.04.2014.
 */
interface SpeechContextImpl {
    public List<Action> getActions();
    public void setActions(List<Action> actions);
    public void addAction(Action action);
    public void removeAction(Action action);
    public Action findActionByName(int nameId);
}
