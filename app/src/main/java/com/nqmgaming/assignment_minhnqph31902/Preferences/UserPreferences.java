package com.nqmgaming.assignment_minhnqph31902.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    //create variables
    private static final String KEY_LOGIN_STATE = "key_login_state";
    private final SharedPreferences sharedPreferences;

    //create constructor
    public UserPreferences(Context context) {

        //call shared preferences
        sharedPreferences = context.getSharedPreferences("user_preferences",
                Context.MODE_PRIVATE);

    }


    //is login if true and not login if false
    public boolean isLogin() {

        return sharedPreferences.getBoolean(KEY_LOGIN_STATE, false);

    }

    //set login state
    public void setLogin(boolean state) {

        sharedPreferences.edit()
                .putBoolean(KEY_LOGIN_STATE, state)
                .apply();

    }

    //logout
    public void logout() {

        sharedPreferences.edit()
                .clear()
                .apply();

    }
}
