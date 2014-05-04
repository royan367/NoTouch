package org.idr.notouch.app.engine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.util.Log;

import org.idr.notouch.app.R;
import org.idr.notouch.app.analyzer.SpeechActivity;
import org.idr.notouch.app.speech.MyTextToSpeech;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by Derya on 26.04.2014. alarm kur saat 8 dakika 20
 */
public class AlarmCommand implements Command {


    // REQUEST NAME IDs
    public static final int REQUEST_SET_ALARM = R.string.set_alarm;
    // REQUEST PARAM NAME IDs
    public static final int REQUEST_PARAM_HOUR = R.string.hour;
    public static final int REQUEST_PARAM_MINUTE= R.string.minute;

    // PARAMS NAMES
    public static final String PARAM_NAME = "alarm_name";
    public static final String PARAM_HOUR = "alarm_hour";
    public static final String PARAM_MINUTE = "alarm_minute";


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
        String alarmHour = mParams.get(PARAM_HOUR);
        String alarmMinute = mParams.get(PARAM_MINUTE);
        try {
            int alarm_Hour = Integer.parseInt(alarmHour);
            int alarm_Minute = Integer.parseInt(alarmMinute);
            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            //i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
            i.putExtra(AlarmClock.EXTRA_HOUR, alarm_Hour);
            i.putExtra(AlarmClock.EXTRA_MINUTES, alarm_Minute);
            mActivity.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
