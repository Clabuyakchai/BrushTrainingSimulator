package com.example.clabuyakchai.brushtrainingsimulator.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clabuyakchai.brushtrainingsimulator.fragments.InstructionFragment;
import com.example.clabuyakchai.brushtrainingsimulator.R;
import com.example.clabuyakchai.brushtrainingsimulator.fragments.SettingsFragment;
import com.example.clabuyakchai.brushtrainingsimulator.fragments.StatisticsFragment;
import com.example.clabuyakchai.brushtrainingsimulator.service.MyIntentService;
import com.example.clabuyakchai.brushtrainingsimulator.sharedpreferences.Preferences;
import com.example.clabuyakchai.brushtrainingsimulator.soap.SOAPRequest;
import com.example.clabuyakchai.brushtrainingsimulator.stateinternet.StateInternet;

/**
 * Created by Clabuyakchai on 05.05.2018.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //TODO службы временно отключены
        if (StateInternet.hasConnection(MainActivity.this)) {
            MyIntentService.startActionDownloadStatistics(MainActivity.this);
            MyIntentService.startActionUploadStatistics(MainActivity.this);
        }

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,
                drawer, toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView mUsername = header.findViewById(R.id.headerUsername);
        mUsername.setText(Preferences.getUsernameSharedPreferences(MainActivity.this));

        startFragment(InstructionFragment.class);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_training:
                startFragment(InstructionFragment.class);
                break;
            case R.id.nav_statistics:
                startFragment(StatisticsFragment.class);
                break;
            case R.id.nav_settings:
                startFragment(SettingsFragment.class);
                break;
            case R.id.nav_information:
                if(StateInternet.hasConnection(MainActivity.this)){
                    new InformationAsyncTask().execute();
                } else {
                    Toast.makeText(MainActivity.this, R.string.error_internet_connection, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_logout:
                Preferences.setUsernameSharedPreferences(MainActivity.this, null);
                Preferences.setTokenSharedPreferences(MainActivity.this, null);
                startActivity(LoginActivity.newIntent(MainActivity.this));
                break;
        }

        item.setChecked(true);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startFragment(Class fragmentClass){
        Fragment fragment = null;

        try{
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container_main, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static Intent newIntent(Context context){
        return new Intent(context, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public class InformationAsyncTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            String response = SOAPRequest.requestSoap();

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}
