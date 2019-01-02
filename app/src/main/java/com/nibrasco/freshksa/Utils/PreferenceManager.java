package com.nibrasco.freshksa.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "intro_slider-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String KEY_PHONE = "phone";

    public PreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }

    public String getUserPhone(){
        return pref.getString(KEY_PHONE, null);
    }
    public void setUserPhone(String phone){
        pref.edit()
            .putString(KEY_PHONE, phone)
            .apply();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        pref.edit()
            .putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            .apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}

