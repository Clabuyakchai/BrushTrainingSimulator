package com.example.clabuyakchai.brushtrainingsimulator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.clabuyakchai.brushtrainingsimulator.model.UserStatistics;
import com.example.clabuyakchai.brushtrainingsimulator.sharedpreferences.Preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clabuyakchai on 06.05.2018.
 */

public class DBQuery {
    public static final String QUERY_DATA = "data";
    public static final String QUERY_USERNAME = "username";

    private DBHelper mDBHelper;
    private Context mContext;

    public DBQuery(Context context) {
        this.mContext = context;
        mDBHelper = new DBHelper(context);
    }

    //1. Статистика для выгрузки на сервер WHERE data
    //2. Статистика для вывода на экран WHERE username
    public List<UserStatistics> getStatisticsUsername(String argument){

        List<UserStatistics> list = new ArrayList<>();

        String selection = null;
        String[] selectionArgs = null;

        if(argument.equals(QUERY_DATA)){
            selection = DBShema.Table.Cols.data + ">?";
            selectionArgs = new String[]{String.valueOf(Preferences.getDataSharedPreferences(mContext))};
        } else if (argument.equals(QUERY_USERNAME)){
            selection = DBShema.Table.Cols.username + "=?";
            selectionArgs = new String[]{Preferences.getUsernameSharedPreferences(mContext)};
        }

        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        Cursor cursor = db.query(DBShema.Table.NAME,
                new String[]{DBShema.Table.Cols.counter, DBShema.Table.Cols.data, DBShema.Table.Cols.username},
                selection,
                selectionArgs,
                null,null, null);

        if(cursor == null || cursor.getCount() == 0){
            return null;
        }

        cursor.moveToFirst();
        do {
            UserStatistics statistics = new UserStatistics();
            statistics.setCounter(cursor.getInt(0));
            statistics.setData(cursor.getLong(1));
            statistics.setUsername(cursor.getString(2));

            list.add(statistics);

            cursor.moveToNext();
        } while (!cursor.isAfterLast());

        cursor.close();

        return list;
    }

    //добавление одной статистики
    public void addStatistics(UserStatistics statistics){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBShema.Table.Cols.counter, statistics.getCounter());
        contentValues.put(DBShema.Table.Cols.data, statistics.getData());
        contentValues.put(DBShema.Table.Cols.username, statistics.getUsername());

        db.insert(DBShema.Table.NAME, null, contentValues);
    }

    //для выгрузки с сервера
    public void addAllStatistics(List<UserStatistics> listStatistics){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        //TODO
        //DELETE
//        listStatistics = addTest();

        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < listStatistics.size(); i++){
            contentValues.put(DBShema.Table.Cols.counter, listStatistics.get(i).getCounter());
            contentValues.put(DBShema.Table.Cols.data, listStatistics.get(i).getData());
            contentValues.put(DBShema.Table.Cols.username, listStatistics.get(i).getUsername());

            db.insert(DBShema.Table.NAME, null, contentValues);

            contentValues.clear();
        }
    }

    //TODO
//    public List<UserStatistics> addTest(){
//        List<UserStatistics> list = new ArrayList<>();
//        list.add(new UserStatistics(20, 1525610353L, "client"));
//        list.add(new UserStatistics(600, 1525610353L, "client"));
//        list.add(new UserStatistics(300, 1525610353L, "client"));
//        list.add(new UserStatistics(400, 1525610353L, "client"));
//        list.add(new UserStatistics(30, 1525610353L, "client"));
//        list.add(new UserStatistics(600, 1525610353L, "client"));
//        list.add(new UserStatistics(70, 1525610353L, "client"));
//        list.add(new UserStatistics(800, 1525610353L, "client"));
//        list.add(new UserStatistics(10, 1525610353L, "client"));
//
//        return list;
//    }
}
