package com.nqmgaming.assignment_minhnqph31902.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static final String KEY_LOGIN_STATE = "key_login_state";
    private final SharedPreferences sharedPreferences;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("user_preferences",
                Context.MODE_PRIVATE);
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(KEY_LOGIN_STATE, false);
    }

    public void setLogin(boolean state) {
        sharedPreferences.edit()
                .putBoolean(KEY_LOGIN_STATE, state)
                .apply();
    }

    public void logout() {
        sharedPreferences.edit()
                .clear()
                .apply();
    }
}
