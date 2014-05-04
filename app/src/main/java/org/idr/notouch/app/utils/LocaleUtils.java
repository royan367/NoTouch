package org.idr.notouch.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by ismail ARILIK on 4.05.2014.
 * utils for localization settings
 */
public final class LocaleUtils {

    /**
     * change language of the app (settings will not be written to SharedPreferences)
     * @param appContext context of the application (try getApplicationContext method in activity)
     * @param lang string representation of the language (forex; "tr", "en_US", ...)
     */
    public static void setLocale(Context appContext, String lang) {
        Locale newLocale = new Locale(lang);
        Resources res = appContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration localConf = res.getConfiguration();
        localConf.locale = newLocale;
        res.updateConfiguration(localConf, dm);
    }

    /**
     * change language of the app statically (this settings will be written to SharedPreferences)
     * @param appContext context of the application (try getApplicationContext method in activity)
     * @param lang string representation of the language (forex; "tr", "en_US", ...)
     */
    public static void setAndSaveLocale(Context appContext, String lang) {
        setLocale(appContext, lang);
        SharedPreferences prefs = SharedPreferencesUtils.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesUtils.KEY_LANGUAGE, lang);
        editor.commit();
    }

    public static String getLocaleAsString(Context appContext) {
        Resources res = appContext.getResources();
        Configuration conf = res.getConfiguration();
        assert conf.locale != null;
        return conf.locale.getLanguage();
    }
}
