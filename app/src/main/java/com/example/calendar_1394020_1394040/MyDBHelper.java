package com.example.calendar_1394020_1394040;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL("CREATE TABLE todaydb(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT, " + "date TEXT , " + "time TEXT, " + "locate TEXT, "
                + "memo TEXT, " + "photo TEXT , " + "video TEXT , " + "voice TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXIST todaydb;");
        onCreate(db);
    }

}
