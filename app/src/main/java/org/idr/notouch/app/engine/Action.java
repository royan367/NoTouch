package org.idr.notouch.app.engine;

/**
 * Created by ms on 14.04.2014.
 */
public class Action {

    private int mNameId;
    private ActionCallback mActionCallback;

    public Action(int nameId, ActionCallback actionCallback) {
        mNameId = nameId;
        mActionCallback = actionCallback;
    }

    public int getName() {
        return mNameId;
    }

    public ActionCallback getActionCallback() {
        return mActionCallback;
    }

    public void run() {
        mActionCallback.onAction();
    }


    public static interface ActionCallback {
        void onAction();
    }
}
