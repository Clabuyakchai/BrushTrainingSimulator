package com.example.clabuyakchai.brushtrainingsimulator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.clabuyakchai.brushtrainingsimulator.database.DBShema.Table;

/**
 * Created by Clabuyakchai on 04.05.2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "courseproject.db";
    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table.NAME + "("
                + Table.Cols.id +" integer primary key autoincrement" + ", "
                + Table.Cols.counter + " integer, "
                + Table.Cols.data + " integer, "
                + Table.Cols.description + ", "
                + Table.Cols.username
                + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Table.NAME);
        onCreate(db);
    }
}
