package org.idr.notouch.app.analyzer;

import java.util.Map;

/**
 * Created by ismail ARILIK on 23.04.2014.
 * This class made for keeping the user request and returned from the AnalyzerEngine
 */
public class Request {

    private int mNameId;
    private Map<String, String> mParams;

    public Request(int nameId, Map<String, String> params) {
        this.mNameId = nameId;
        this.mParams = params;
    }

    public int getNameId() {
        return mNameId;
    }

    public Map<String, String> getParams() {
        return mParams;
    }
}
