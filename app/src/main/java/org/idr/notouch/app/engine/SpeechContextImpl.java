package org.idr.notouch.app.engine;

import java.util.List;

/**
 * Created by ms on 16.04.2014.
 */
public interface SpeechContextImpl {
    List<Action> getActions();
    void setActions(List<Action> actions);
    void addAction(Action action);
    void removeAction(Action action);
    Action findActionByName(int nameId);
}
