package org.idr.notouch.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ismail ARILIK on 4.05.2014.
 */
public class SharedPreferencesUtils {

    public static final String KEY_LANGUAGE = "lang";


    /**
     *
     * @param appContext the application's context
     * @return default SharedPreferences for this application with private access
     */
    public static SharedPreferences getDefaultSharedPreferences(Context appContext) {
        return appContext.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }
}
