package org.idr.notouch.app.engine;

import android.content.Intent;
import android.provider.AlarmClock;
import android.speech.tts.TextToSpeech;

import org.idr.notouch.app.R;
import org.idr.notouch.app.analyzer.MainActivity;
import org.idr.notouch.app.analyzer.SpeechActivity;

import java.util.Map;

/**
 * Created by Derya on 26.04.2014. alarm kur saat 8 dakika 20
 */
public class AlarmCommand implements Command, TextToSpeech.OnInitListener {


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
    private TextToSpeech mTts;

    public AlarmCommand(SpeechActivity mActivity, Map<String, String> mParams) {
        this.mActivity = mActivity;
        this.mParams = mParams;
    }

    @Override
    public void execute() {
        mTts = new TextToSpeech(mActivity, this);
    }

    @Override
    public void onInit(int status) {
        /*
        setup the alarm
         */
        String alarmHour = mParams.get(PARAM_HOUR);
        String alarmMinute = mParams.get(PARAM_MINUTE);
        try {
            int alarm_Hour = Integer.parseInt(alarmHour);
            int alarm_Minute = Integer.parseInt(alarmMinute);
            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            // TODO gereksizse sil
            //i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
            i.putExtra(AlarmClock.EXTRA_HOUR, alarm_Hour);
            i.putExtra(AlarmClock.EXTRA_MINUTES, alarm_Minute);
            mActivity.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            // say that the alarm could not be sent, to user
            mTts.speak(mActivity.getString(R.string.alarm_could_not_be_setup),
                    TextToSpeech.QUEUE_FLUSH, null);
            ((MainActivity) mActivity).writeToListView(R.string.alarm_could_not_be_setup, false);
        }
    }
}
