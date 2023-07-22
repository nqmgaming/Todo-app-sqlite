package com.nqmgaming.assignment_minhnqph31902.UI;

import android.os.Bundle;
import android.view.Menu;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.nqmgaming.assignment_minhnqph31902.Fragment.CalendarFragment;
import com.nqmgaming.assignment_minhnqph31902.Fragment.HomeFragment;
import com.nqmgaming.assignment_minhnqph31902.Fragment.SearchFragment;
import com.nqmgaming.assignment_minhnqph31902.Fragment.UserFragment;
import com.nqmgaming.assignment_minhnqph31902.Preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    UserPreferences userPreferences;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigation.setBackground(null);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.user) {
                replaceFragment(new UserFragment());
            } else if (item.getItemId() == R.id.search) {
                replaceFragment(new SearchFragment());

            } else if (item.getItemId() == R.id.calendar) {
                replaceFragment(new CalendarFragment());
            }
            return true;
        });


        userPreferences = new UserPreferences(this);

    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment instanceof UserFragment) {
            Bundle args = new Bundle();
            args.putInt("id", userPreferences.getIdUser());
            fragment.setArguments(args);
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
