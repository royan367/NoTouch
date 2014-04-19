package org.idr.notouch.app.engine;

import java.util.List;

/**
 * Created by ms on 16.04.2014.
 */
public interface SpeechContextImpl {
    public List<Action> getActions();
    public void addAction(Action action);
    public void removeAction(Action action);
    public Action findActionByName(int nameId);
    public String getTag();
    public void setTag(String tag);
}
