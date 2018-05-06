package com.example.clabuyakchai.brushtrainingsimulator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Clabuyakchai on 05.05.2018.
 */

public class InstructionFragment extends Fragment {

    private Button mStartTrening;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instruction, container, false);

        mStartTrening = view.findViewById(R.id.startTrening);
        mStartTrening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_main, SimulatorFragment.newInstance())
                        .addToBackStack("Instruction")
                        .commit();
            }
        });

        return view;
    }

    public static InstructionFragment newInstance(){
        return new InstructionFragment();
    }
}
