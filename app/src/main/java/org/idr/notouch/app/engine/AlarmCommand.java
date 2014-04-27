package org.idr.notouch.app.engine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;

import org.idr.notouch.app.R;
import org.idr.notouch.app.analyzer.SpeechActivity;
import org.idr.notouch.app.speech.MyTextToSpeech;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by Derya on 26.04.2014.
 */
public class AlarmCommand implements Command {


    // REQUEST NAME IDs
    public static final int REQUEST_SET_ALARM = R.string.set_alarm;
    // REQUEST PARAM NAME IDs
    public static final int REQUEST_PARAM_ALARM = R.string.alarm;
    // PARAMS NAMES
    public static final String PARAM_NAME = "alarm_name";


    private SpeechActivity mActivity;
    private Map<String, String> mParams;
    private MyTextToSpeech mTts;


    public AlarmCommand(SpeechActivity mActivity, Map<String, String> mParams) {
        this.mActivity = mActivity;
        this.mParams = mParams;
        mTts = mActivity.getTextToSpeech();
    }

    @Override
    public void execute() {
        /*
        setup the alarm
         */
        String alarmName = mParams.get(PARAM_NAME);


    }


}
