package com.example.clabuyakchai.brushtrainingsimulator.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import com.example.clabuyakchai.brushtrainingsimulator.api.ApiService;
import com.example.clabuyakchai.brushtrainingsimulator.api.Client;
import com.example.clabuyakchai.brushtrainingsimulator.database.DBQuery;
import com.example.clabuyakchai.brushtrainingsimulator.model.ResponseStatistics;
import com.example.clabuyakchai.brushtrainingsimulator.model.UserStatistics;
import com.example.clabuyakchai.brushtrainingsimulator.sharedpreferences.Preferences;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyIntentService extends IntentService {

    private static final String ACTION_DOWNLOAD_STATISTICS = "com.example.clabuyakchai.brushtrainingsimulator.service.action.DOWNLOAD_STATISTICS";
    private static final String ACTION_UPLOAD_STATISTICS = "com.example.clabuyakchai.brushtrainingsimulator.service.action.UPLOAD_STATISTICS";

    public MyIntentService() {
        super("MyIntentService");
    }

    // Сервис для скачивания статистики из сервера на телефон
    public static void startActionDownloadStatistics(Context context) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_DOWNLOAD_STATISTICS);
        context.startService(intent);
    }

    // Сервис для загрузки статистики из телефона на сервер
    public static void startActionUploadStatistics(Context context) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_UPLOAD_STATISTICS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD_STATISTICS.equals(action)) {
                handleActionDownloadStatistics();
            } else if (ACTION_UPLOAD_STATISTICS.equals(action)) {
                handleActionUploadStatistics();
            }
        }
    }

    private void handleActionDownloadStatistics() {
        String username = Preferences.getUsernameSharedPreferences(MyIntentService.this);
        String token = Preferences.getTokenSharedPreferences(MyIntentService.this);

        if(username == null){
            return;
        }

        DBQuery dbQuery = new DBQuery(MyIntentService.this);

        List<UserStatistics> list = null;

        list = dbQuery.getStatisticsUsername(DBQuery.QUERY_USERNAME);
        if(list == null) {
            ApiService service = Client.getApiService();
            Call<List<ResponseStatistics>> call = service.getStatisticsUsername(token, username);

            call.enqueue(new Callback<List<ResponseStatistics>>() {
                @Override
                public void onResponse(Call<List<ResponseStatistics>> call, Response<List<ResponseStatistics>> response) {
                    if(response.isSuccessful()){
                        if(response.body().size() != 0){
                            List<UserStatistics> list = new ArrayList<>();

                            for (int i = 0; i < response.body().size(); i++) {
                                ResponseStatistics responseStatistics = response.body().get(i);

                                UserStatistics userStatistics = new UserStatistics();
                                userStatistics.setCounter(responseStatistics.getCounter());
                                userStatistics.setData(responseStatistics.getData());
                                userStatistics.setUsername(username);

                                list.add(userStatistics);
                            }

                            dbQuery.addAllStatistics(list);

                            Toast.makeText(MyIntentService.this, "Download", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MyIntentService.this, "Error Download", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ResponseStatistics>> call, Throwable t) {
                    Toast.makeText(MyIntentService.this, "onFailure Download", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleActionUploadStatistics() {
        Long data = Preferences.getDataSharedPreferences(MyIntentService.this);
        String token = Preferences.getTokenSharedPreferences(MyIntentService.this);

        if(data == 0){
            Preferences.setDataSharedPreferences(MyIntentService.this, new Date().getTime());
        }

        DBQuery dbQuery = new DBQuery(MyIntentService.this);

        List<UserStatistics> list = dbQuery.getStatisticsUsername(DBQuery.QUERY_DATA);

        if(list != null){
            ApiService service = Client.getApiService();
            Call<Void> call = service.addAllStatistics(token, list);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Preferences.setDataSharedPreferences(MyIntentService.this, new Date().getTime());
                        Toast.makeText(MyIntentService.this, "Upload", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyIntentService.this, "Error Upload", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MyIntentService.this, "onFailure Upload", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
