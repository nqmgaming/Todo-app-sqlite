package com.nqmgaming.assignment_minhnqph31902.UI;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.nqmgaming.assignment_minhnqph31902.Preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;

import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {
    UserPreferences userPreferences;
    SmoothBottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPreferences = new UserPreferences(this);
        bottomBar = findViewById(R.id.bottomBar);

    }

}