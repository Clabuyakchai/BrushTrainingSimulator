package com.example.clabuyakchai.brushtrainingsimulator.sharedpreferences;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Clabuyakchai on 05.05.2018.
 */

public class Preferences {

    private static final String PREF_USERNAME = "username";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_DATA = "data";

    public static String getUsernameSharedPreferences(Context context){
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREF_USERNAME, null);
    }

    public static void setUsernameSharedPreferences(Context context, String username){
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USERNAME, username)
                .apply();

    }

    public static String getTokenSharedPreferences(Context context){
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREF_TOKEN, null);
    }

    public static void setTokenSharedPreferences(Context context, String token){
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_TOKEN, "Bearer " + token)
                .apply();
    }

    public static Long getDataSharedPreferences(Context context){
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getLong(PREF_DATA, 0);
    }

    public static void setDataSharedPreferences(Context context, Long data){
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putLong(PREF_DATA, data)
                .apply();
    }
}
