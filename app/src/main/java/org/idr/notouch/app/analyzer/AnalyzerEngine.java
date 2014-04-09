package org.idr.notouch.app.analyzer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by ismail ARILIK on 6.04.2014.
 */
public class AnalyzerEngine {

    private Activity activity;

    public AnalyzerEngine(Activity activity) {
        this.activity = activity;
    }

    public void analyze(String action) {
        String output;
        if (action.equals("do")) {
            output = "done";
        } else {
            output = "Could not be found!";
        }

        new AlertDialog.Builder(activity)
                .setMessage(output)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
