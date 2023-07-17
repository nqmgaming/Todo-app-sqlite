package com.nqmgaming.assignment_minhnqph31902.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import com.nqmgaming.assignment_minhnqph31902.Preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;

public class GetStartActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    CheckBox cbAgree;
    private UserPreferences userPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreferences = new UserPreferences(this);
        if (userPreferences.isLogin()) {
            startActivity(new Intent(GetStartActivity.this, MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_get_start);
        btnLogin = findViewById(R.id.btnLoginGetStart);
        btnRegister = findViewById(R.id.btnSignUpGetStart);
        cbAgree = findViewById(R.id.checkBoxAgree);

        btnLogin.setOnClickListener(v -> {
            cbAgree.setChecked(true);
            Intent intent = new Intent(GetStartActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(v -> {
            cbAgree.setChecked(true);
            Intent intent = new Intent(GetStartActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }
}