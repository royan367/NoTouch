package org.idr.notouch.app.analyzer;


import org.idr.notouch.app.R;
import org.idr.notouch.app.engine.AlarmCommand;
import org.idr.notouch.app.engine.CallCommand;
import org.idr.notouch.app.engine.MusicPlayerCommand;
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

        // define commands as string
        String sendMsgCommand = mActivity.getString(SendMessageCommand.REQUEST_SEND_MESSAGE);
        String sendMsgCommandLower = sendMsgCommand.toLowerCase(currentLocale);

        String callCommand = mActivity.getString(CallCommand.REQUEST_CALL);
        String callCommandLower = callCommand.toLowerCase(currentLocale);

        String play_music = mActivity.getString(MusicPlayerCommand.REQUEST_PLAY_MUSIC);
        String play_musicLower = play_music.toLowerCase(currentLocale);

        String set_alarm = mActivity.getString(AlarmCommand.REQUEST_SET_ALARM);
        String set_alarmLower = set_alarm.toLowerCase(currentLocale);


        // if action is a 'Send Message' command
        if (actionLower.startsWith(sendMsgCommandLower)) {
            // if the action is only 'Send Message' command
            if (actionLower.trim().equals(sendMsgCommandLower)) {
                // generate the request directly
                request = new Request(SendMessageCommand.REQUEST_SEND_MESSAGE, null);
            } else {
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
        } else if (actionLower.startsWith(callCommandLower)) {    // else if action is a 'Call' command
            // if the action is only 'Call' command
            if (actionLower.trim().equals(callCommandLower)) {
                // generate the request directly
                request = new Request(CallCommand.REQUEST_CALL, null);
            } else {
                // tokenize and generate the Request
                String paramPerson = mActivity.getString(CallCommand.REQUEST_PARAM_PERSON);
                int paramPersonIndex = actionLower.indexOf(paramPerson.toLowerCase(currentLocale),
                        callCommand.length() - 1);
                try {
                    String personName = action.substring(paramPersonIndex + paramPerson.length() + 1);
                    Map<String, String> params = new HashMap<String, String>(1);
                    params.put(CallCommand.PARAM_NAME, personName);
                    request = new Request(CallCommand.REQUEST_CALL, params);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }  else if (actionLower.startsWith(play_musicLower)) {    // if action is a 'Play Music' command
            String paramMusic = mActivity.getString(MusicPlayerCommand.REQUEST_PARAM_MUSIC);
            int paramMusicIndex = actionLower.indexOf(paramMusic.toLowerCase(currentLocale),
                    play_music.length() - 1);

            try {
                String musicName = action.substring(paramMusicIndex + paramMusic.length() + 1);
                Map<String, String> params = new HashMap<String, String>(2);
                params.put(MusicPlayerCommand.PARAM_NAME, musicName);
                request = new Request(MusicPlayerCommand.REQUEST_PLAY_MUSIC, params);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        } else if (actionLower.startsWith(set_alarmLower)) {    // if action is a 'Set Alarm' command
            // tokenize and generate the Request
            String paramAlarm = mActivity.getString(AlarmCommand.REQUEST_SET_ALARM);
            String paramHour= mActivity.getString(AlarmCommand.REQUEST_PARAM_HOUR);
            String paramMinute= mActivity.getString(AlarmCommand.REQUEST_PARAM_MINUTE);
            int paramHourIndex = actionLower.indexOf(paramHour.toLowerCase(currentLocale),
                    set_alarm.length() - 1);
            int paramMinuteIndex = actionLower.indexOf(paramMinute.toLowerCase(currentLocale),
                    set_alarm.length() - 1);

            try {
                String alarmHour = action.substring(paramHourIndex + paramHour.length() + 1,
                        paramMinuteIndex - 1);
                String alarmMinute = action.substring(paramMinuteIndex + paramMinute.length() + 1);
                Map<String, String> params = new HashMap<String, String>(1);
                params.put(AlarmCommand.PARAM_HOUR, alarmHour);
                params.put(AlarmCommand.PARAM_MINUTE, alarmMinute);
                request = new Request(AlarmCommand.REQUEST_SET_ALARM, params);
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
