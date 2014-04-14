package org.idr.notouch.app.analyzer;

/**
 * Created by ms on 14.04.2014.
 */
public class Action {

    private int mNameId;

    public Action() {
    }

    public Action(int nameId) {
        mNameId = nameId;
    }

    public int getName() {
        return mNameId;
    }

    public void setName(int nameId) {
        mNameId = nameId;
    }
}
