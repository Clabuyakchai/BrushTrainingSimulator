package com.example.clabuyakchai.brushtrainingsimulator;

import android.content.Context;
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

        //TODO
//        Preferences.setTokenSharedPreferences(LoginActivity.this, "jvnfbniudv");
//        Preferences.setUsernameSharedPreferences(LoginActivity.this, "client");

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

    public static Intent newIntent(Context context){
        return new Intent(context, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
