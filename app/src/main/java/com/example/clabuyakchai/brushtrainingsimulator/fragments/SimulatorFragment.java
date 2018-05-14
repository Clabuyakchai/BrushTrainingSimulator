package com.example.clabuyakchai.brushtrainingsimulator.fragments;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clabuyakchai.brushtrainingsimulator.R;
import com.example.clabuyakchai.brushtrainingsimulator.database.DBQuery;
import com.example.clabuyakchai.brushtrainingsimulator.model.UserStatistics;
import com.example.clabuyakchai.brushtrainingsimulator.service.MyIntentService;
import com.example.clabuyakchai.brushtrainingsimulator.sharedpreferences.Preferences;
import com.example.clabuyakchai.brushtrainingsimulator.stateinternet.StateInternet;

import java.util.Date;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by Clabuyakchai on 05.05.2018.
 */

public class SimulatorFragment extends Fragment {

    private static final int REQUEST_COMMENT = 123;
    private static final String DIALOG_COMMENT = "DialogComment";

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private boolean isPresent = false;

    private boolean state1 = false;
    private boolean state2 = false;
    private boolean state3 = false;
    private boolean state4 = false;

    private TextView mTextViewProgress;
    private ProgressBar mProgressBar;
    private Button mFinish;

    private int mProgress = 0;
    private int MAX_VALUE = 0;
    private String value = null;

    private UserStatistics statistics = null;
    private FragmentManager fragmentManager = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simulator, container, false);

        mTextViewProgress = view.findViewById(R.id.progress);
        mProgressBar = view.findViewById(R.id.progressBar);
        mFinish = view.findViewById(R.id.finishTrening);

        if(Preferences.getSettingInfinityOrNoSharedPreferences(getActivity())) {
            MAX_VALUE = Preferences.getSettingCounterSharedPreferences(getActivity());
            value = String.valueOf(MAX_VALUE);
            mProgressBar.setMax(MAX_VALUE);
        } else {
            MAX_VALUE = -1;
            value = getResources().getString(R.string.infinity);
            mProgressBar.setMax(Integer.MAX_VALUE);
        }

        mTextViewProgress.setText("0/" + value);

        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

        if(sensors.size() > 0){
            isPresent = true;
            mAccelerometerSensor = sensors.get(0);
        }

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autofinish();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPresent){
            mSensorManager.registerListener(listn, mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isPresent){
            stopListenerAccelerometr();
        }
    }

    SensorEventListener listn = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            if(9 <= y && y <= 10 && -3 <= z && z <= 5 && !state1){
                state1 = true;
            }else if(8.5 <= x && x <= 10 && -2 <= y && y <= 2 && state1 && !state2){
                state2 = true;
            } else if(-10 <= y && y <= -2 && -9 <= z && z <= 0 && state1 && state2 && !state3){
                state3 = true;
            } else if(-3 <= z && z <= 2 && state1 && state2 && state3){
                state4 = true;
            } else if(9 <= y && y <= 10 && -3 <= z && z <= 3 && state1 && state2 && state3 && state4){
                mProgress++;
                if (mProgress < MAX_VALUE) {
                    mProgressBar.setProgress(mProgress);
                    mTextViewProgress.setText(mProgress + " /" + value);
                } else if (MAX_VALUE == -1){
                    mProgressBar.setProgress(mProgress);
                    mTextViewProgress.setText(mProgress + " /" + value);
                } else if (mProgress == MAX_VALUE){
                    mTextViewProgress.setText(mProgress + " /" + value);
                    autofinish();
                }
                state1 = false;
                state2 = false;
                state3 = false;
                state4 = false;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public void autofinish(){
        stopListenerAccelerometr();

        mProgressBar.setProgress(0);

        statistics = new UserStatistics(mProgress, new Date().getTime(),
                Preferences.getUsernameSharedPreferences(getActivity()));

        fragmentManager = getActivity().getSupportFragmentManager();

        CommentFragment commentFragment = CommentFragment.newInstance();
        commentFragment.setTargetFragment(SimulatorFragment.this, REQUEST_COMMENT);
        commentFragment.show(fragmentManager, DIALOG_COMMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_COMMENT){
            String comment = data.getStringExtra(CommentFragment.EXTRA_COMMENT);
            if(comment != null){
                statistics.setDescription(comment);
                afterComment();
            }
        }
    }

    private void afterComment(){
        DBQuery dbQuery = new DBQuery(getActivity());
        dbQuery.addStatistics(statistics);

        if(StateInternet.hasConnection(getActivity())){
            MyIntentService.startActionUploadStatistics(getActivity());
        } else {
            Toast.makeText(getActivity(), R.string.error_internet_connection, Toast.LENGTH_SHORT).show();
        }

        fragmentManager.popBackStack();
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container_main, new InstructionFragment()).commit();
    }

    public void stopListenerAccelerometr(){
        mSensorManager.unregisterListener(listn);
    }

    public static SimulatorFragment newInstance(){
        return new SimulatorFragment();
    }
}
