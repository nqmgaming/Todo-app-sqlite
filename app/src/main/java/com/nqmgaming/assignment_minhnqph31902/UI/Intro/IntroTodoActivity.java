package com.nqmgaming.assignment_minhnqph31902.UI.Intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nqmgaming.assignment_minhnqph31902.R;

public class IntroTodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_todo);

        // Set delay 3s
        new Handler().postDelayed(() -> {
            startActivity(new Intent(IntroTodoActivity.this, WelcomeActivity.class));
            finish();
        }, 3500);
    }
}
