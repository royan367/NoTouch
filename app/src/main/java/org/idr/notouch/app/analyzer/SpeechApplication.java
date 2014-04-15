package org.idr.notouch.app.analyzer;

import android.app.Application;

import org.idr.notouch.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ms on 14.04.2014.
 */
public class SpeechApplication extends Application {

    private List<Action> globalActions;

    @Override
    public void onCreate() {
        super.onCreate();

        // enter global actions here
        globalActions = new ArrayList<Action>();
        globalActions.add(new Action(R.string.bye_buddy, new Action.ActionCallback() {
            @Override
            public void onAction() {
                // terminate the application
            }
        }));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public List<Action> getGlobalActions() {
        return globalActions;
    }
}
