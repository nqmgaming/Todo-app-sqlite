package com.nqmgaming.assignment_minhnqph31902.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nqmgaming.assignment_minhnqph31902.Adapter.SlideAdapter;
import com.nqmgaming.assignment_minhnqph31902.Preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;

import me.relex.circleindicator.CircleIndicator;

public class WelcomeActivity extends AppCompatActivity {
    ViewPager viewPager;
    CircleIndicator dotsLayout;
    Button btnNext, btnSkip;
    SlideAdapter slideAdapter;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreferences = new UserPreferences(this);
        if (userPreferences.isLogin()) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);


        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.indicator);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);

        slideAdapter = new SlideAdapter(this);
        viewPager.setAdapter(slideAdapter);

        dotsLayout.setViewPager(viewPager);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(0) < slideAdapter.getCount() - 1) {
                    viewPager.setCurrentItem(getItem(1), true);
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, GetStartActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, GetStartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        viewPager.addOnPageChangeListener(viewListener);
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position >= slideAdapter.getCount() - 1) {
                btnNext.setText("Get Started");
            } else {
                btnNext.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}