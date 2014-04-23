package org.idr.notouch.app.analyzer;


import org.idr.notouch.app.R;
import org.idr.notouch.app.engine.SendMessageCommand;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by ismail ARILIK on 6.04.2014.
 */
public class AnalyzerEngine {

    private SpeechActivity mActivity;


    public AnalyzerEngine(SpeechActivity activity) {
        this.mActivity = activity;
    }


    public Request analyze(String action) {
        Locale currentLocale = Locale.getDefault();
        String actionLower = action.toLowerCase(currentLocale);
        Request request = null;

        // if action is a 'Send Message' command
        String sendMsgCommand = mActivity.getString(SendMessageCommand.REQUEST_SEND_MESSAGE);
        String sendMsgCommandLower = sendMsgCommand.toLowerCase(currentLocale);
        if (actionLower.startsWith(sendMsgCommandLower)) {
            // tokenize, and generate the Request
            String paramPerson = mActivity.getString(SendMessageCommand.REQUEST_PARAM_PERSON);
            String paramMessage = mActivity.getString(SendMessageCommand.REQUEST_PARAM_MESSAGE);
            int paramPersonIndex = actionLower.indexOf(paramPerson.toLowerCase(currentLocale),
                    sendMsgCommand.length() - 1);
            int paramMessageIndex = actionLower.indexOf(paramMessage.toLowerCase(currentLocale),
                    sendMsgCommand.length() - 1);
            try {
                String personName = action.substring(paramPersonIndex + paramPerson.length() + 1,
                        paramMessageIndex - 1);
                String message = action.substring(paramMessageIndex + paramMessage.length() + 1);
                Map<String, String> params = new HashMap<String, String>(2);
                params.put(SendMessageCommand.PARAM_NAME, personName);
                params.put(SendMessageCommand.PARAM_MESSAGE, message);
                request = new Request(SendMessageCommand.REQUEST_SEND_MESSAGE, params);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return request;
    }

    private boolean stringArrayContainsIgnoreCase(String[] array, String value) {
        for (String str : array) {
            if (str.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean stringStartsWithAnyStringArrayElementIgnoreCase(String value, String[] array) {
        Locale locale = Locale.getDefault();
        for (String str : array) {
            if (value.toLowerCase(locale).startsWith(str.toLowerCase(locale))) {
                return true;
            }
        }
        return false;
    }
}
