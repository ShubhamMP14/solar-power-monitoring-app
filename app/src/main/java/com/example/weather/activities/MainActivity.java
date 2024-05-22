package com.example.weather.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.weather.fragments.DashFragment;
import com.example.weather.fragments.HomeFragment;
import com.example.weather.fragments.ProfileFragment;
import com.example.weather.R;
import com.example.weather.fragments.TestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.dash) {
                selectedFragment = new DashFragment();
            } else if (item.getItemId() == R.id.test) {
                selectedFragment = new TestFragment();
            } else if (item.getItemId() == R.id.profile) {
                selectedFragment = new ProfileFragment();
            }

            loadFragment(selectedFragment);
            return true;
        });


        // Set the default selected item
        bottomNavigation.setSelectedItemId(R.id.home);

    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    @Override
    public void onBackPressed() {
        // Remove the onBackPressed() method from MainActivity to use the default behavior.
        // Alternatively, you can add your custom logic here if needed.
        super.onBackPressed();
    }
}