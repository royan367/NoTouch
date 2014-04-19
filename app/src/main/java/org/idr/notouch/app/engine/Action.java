package org.idr.notouch.app.engine;

/**
 * Created by ms on 14.04.2014.
 */
public class Action {

    private int mNameId;
    private String mParam;
    private boolean mRequireParam;
    private ActionCallback mActionCallback;


    public Action(int nameId, String param, boolean requireParam, ActionCallback actionCallback) {
        mNameId = nameId;
        mParam = param;
        mRequireParam = requireParam;
        mActionCallback = actionCallback;
    }


    public int getName() {
        return mNameId;
    }

    public String getParam() {
        return mParam;
    }

    public ActionCallback getActionCallback() {
        return mActionCallback;
    }

    public void run() {
        mActionCallback.onActionBegin();
        mActionCallback.onAction();
        mActionCallback.onActionEnd();
    }


    public static interface ActionCallback {
        public void onAction();
        public void onActionBegin();
        public void onActionEnd();
        public void onParamNotSet();
        public void onError();
    }
}
