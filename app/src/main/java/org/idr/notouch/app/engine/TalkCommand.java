package org.idr.notouch.app.engine;

import org.idr.notouch.app.R;
import org.idr.notouch.app.analyzer.SpeechActivity;
import org.idr.notouch.app.speech.MyTextToSpeech;

import java.util.Map;

/**
 * Created by Derya on 3.5.2014.
 */
public class TalkCommand implements Command{


    public static final int REQUEST_SAY_HELLO = R.string.say_hello;
    public static final String PARAM_HELLO = "hello";

    public static final int REQUEST_SAY_HI1= R.string.say_hi1;
    public static final String PARAM_HI = "hi";

    public static final int REQUEST_SAY_IDIOT= R.string.idiot;
    public static final String PARAM_IDIOT = "idiot";

    public static final int REQUEST_SAY_HOW= R.string.how;
    public static final String PARAM_HOW = "how";

    public static final int REQUEST_SAY_WHAT= R.string.what;
    public static final String PARAM_WHAT = "what";




    private MyTextToSpeech textToSpeech;
    private SpeechActivity mActivity;
    private Map<String, String> mParams;
    private MyTextToSpeech mTts;
    public TalkCommand(SpeechActivity mActivity, Map<String, String> mParams) {
        this.mActivity = mActivity;
        this.mParams = mParams;
        mTts = mActivity.getTextToSpeech();
    }
    @Override
    public void execute() {

    }
}
