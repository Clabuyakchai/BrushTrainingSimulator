package com.example.clabuyakchai.brushtrainingsimulator;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clabuyakchai.brushtrainingsimulator.database.DBQuery;
import com.example.clabuyakchai.brushtrainingsimulator.model.UserStatistics;
import com.example.clabuyakchai.brushtrainingsimulator.service.MyIntentService;
import com.example.clabuyakchai.brushtrainingsimulator.sharedpreferences.Preferences;
import com.example.clabuyakchai.brushtrainingsimulator.stateinternet.StateInternet;

import java.util.Date;
import java.util.List;

/**
 * Created by Clabuyakchai on 05.05.2018.
 */

public class SimulatorFragment extends Fragment implements SensorEventListener {

    /** Объект типа сенсор менеджер */
    private SensorManager mSensorManager;
    /** Создали объект типа сенсор для получения данных угла наклона телефона */
    private Sensor mAccelerometerSensor;

    private TextView mCounter;

    private Button mFinish;

    private int countBefore = 0;
    private int countAfter = 0;
    private boolean xRotate = false;
    private boolean yRotate = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simulator, container, false);

        mCounter = view.findViewById(R.id.counter);

        mFinish = view.findViewById(R.id.finishTrening);

        // присвоили менеджеру работу с серсором
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        // создали список сенсоров для записи и сортировки
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        // делаем проверку если больше нуля значит все хорошо и начинаем обрабатывать работу датчика
        if (sensors.size() > 0) {
            // форич для зацикливания работы, что бы не единожды выполнялось, а постоянно
            for (Sensor sensor : sensors) {
                // берем данные с акселерометра
                switch (sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        // если пусто значит возвращаем значения сенсора
                        if (mAccelerometerSensor == null)
                            mAccelerometerSensor = sensor;
                        break;
                    default:
                        break;
                }
            }
        }

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopListenerAccelerometr();

                UserStatistics statistics = new UserStatistics(100, new Date().getTime(), "Nice",
                        Preferences.getUsernameSharedPreferences(getActivity()));

                DBQuery dbQuery = new DBQuery(getActivity());
                dbQuery.addStatistics(statistics);

                //TODO
                if(StateInternet.hasConnection(getActivity())){
                    MyIntentService.startActionUploadStatistics(getActivity());
                } else {
                    Toast.makeText(getActivity(), R.string.error_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        stopListenerAccelerometr();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //регистрируем сенсоры в объекты сенсора
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
//        mSensorManager.registerListener(this, mMagneticFieldSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //создали массив в которые будем записывать наши данные полученые с датчиков
        float[] values = sensorEvent.values;
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                //собственно выводим все полученые параметры в текствьюшки наши
                countRotate(sensorEvent);
            }
            break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void countRotate(SensorEvent event){
        if(9 <= event.values[0] && event.values[0] <= 10){
            xRotate = true;

        }
        if (-10 <= event.values[1] && event.values[1] <= -3.5 && xRotate){
            countAfter = countBefore;
            countAfter++;
            xRotate = false;
            yRotate = true;
        }
        if(8.5 <= event.values[1] && event.values[1] <= 10 && !xRotate && yRotate){
            setCounterText();
            yRotate = false;
        }
    }

    private void setCounterText(){
        if(countAfter - countBefore == 1){
            countBefore++;
            mCounter.setText(String.valueOf(countBefore));
        }
    }

    public void stopListenerAccelerometr(){
        mSensorManager.unregisterListener(this);
    }

    public static SimulatorFragment newInstance(){
        return new SimulatorFragment();
    }
}
