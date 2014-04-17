package org.idr.notouch.app.engine;

/**
 * Created by ms on 14.04.2014.
 */
public class Action {

    private int mNameId;
    private ActionCallback mActionCallback;

    public Action() {
    }

    public Action(int nameId, ActionCallback actionCallback) {
        mNameId = nameId;
        mActionCallback = actionCallback;
    }

    public int getName() {
        return mNameId;
    }

    public void setName(int nameId) {
        mNameId = nameId;
    }

    public ActionCallback getActionCallback() {
        return mActionCallback;
    }

    public void setActionCallback(ActionCallback actionCallback) {
        mActionCallback = actionCallback;
    }

    public void run() {
        mActionCallback.onAction();
    }


    static interface ActionCallback {
        void onAction();
    }
}
