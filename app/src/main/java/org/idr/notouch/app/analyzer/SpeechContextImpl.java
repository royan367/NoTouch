package org.idr.notouch.app.analyzer;

import java.util.List;

/**
 * Created by ms on 16.04.2014.
 */
interface SpeechContextImpl {
    List<Action> getActions();
    void setActions(List<Action> actions);
    void addAction(Action action);
    void removeAction(Action action);
    Action findActionByName(int nameId);
}
