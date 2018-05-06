package com.example.clabuyakchai.brushtrainingsimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.clabuyakchai.brushtrainingsimulator.sharedpreferences.Preferences;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Preferences.setTokenSharedPreferences(LoginActivity.this, "jvnfbniudv");
        String token = Preferences.getTokenSharedPreferences(LoginActivity.this);

        if(token == null || token.isEmpty()) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

            if (fragment == null) {
                fragment = new SignInFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
        } else {
            startActivity(MainActivity.newIntent(LoginActivity.this));
        }
    }
}
