package com.example.clabuyakchai.brushtrainingsimulator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.clabuyakchai.brushtrainingsimulator.sharedpreferences.Preferences;

/**
 * Created by Clabuyakchai on 13.05.2018.
 */

public class SettingsFragment extends Fragment{

    private Switch mSwitch;
    private CircularSeekBar mCircularSeekBar;
    private TextView mDisplayCounter;
    private TextView mInscription;
    private int maxCounter = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mSwitch = view.findViewById(R.id.settingSwitch);
        mDisplayCounter = view.findViewById(R.id.displayCounter);
        mInscription = view.findViewById(R.id.setting_inscription);
        mCircularSeekBar = view.findViewById(R.id.seekBar);

        mCircularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                mDisplayCounter.setText(String.valueOf(progress));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                if(seekBar.getProgress() == 0) {
                    Preferences.setSettingCounterSharedPreferences(getActivity(), 1);
                } else {
                    Preferences.setSettingCounterSharedPreferences(getActivity(), seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Preferences.setSettingInfinityOrNoSharedPreferences(getActivity(), true);
                } else {
                    Preferences.setSettingInfinityOrNoSharedPreferences(getActivity(), false);
                }
                setSettings();
            }
        });

        if(setSettings()){
            mSwitch.setChecked(true);
        } else {
            mSwitch.setChecked(false);
        }

        return view;
    }

    private boolean setSettings(){
        if(Preferences.getSettingInfinityOrNoSharedPreferences(getActivity())){
            maxCounter = Preferences.getSettingCounterSharedPreferences(getActivity());
            mCircularSeekBar.setIsTouchEnabled(true);
            mInscription.setText(R.string.settings_max_counter);
            mDisplayCounter.setText(String.valueOf(maxCounter));

            return true;
        } else {
            mCircularSeekBar.setIsTouchEnabled(false);
            mInscription.setText(R.string.settings_infinity_rotation);
            mDisplayCounter.setText(R.string.infinity);

            return false;
        }
    }

    public SettingsFragment newInstance(){
        return new SettingsFragment();
    }
}
