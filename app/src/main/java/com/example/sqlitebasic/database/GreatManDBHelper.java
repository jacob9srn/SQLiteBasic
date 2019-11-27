package com.example.sqlitebasic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

//import com.example.sqlitebasic.database.GreatManContract.*; 이건 나중에 쓰자.


import androidx.annotation.Nullable;

public class GreatManDBHelper extends SQLiteOpenHelper {

    private static GreatManDBHelper sInstance;
    public static  final  String DATABASE_NAME = "greatMan.db";
    public static  final  int DATABASE_VERSION = 3;

    public GreatManDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    public static synchronized GreatManDBHelper getInstance(Context context){
        // 액티비티의 context가 메모리 릭을 발생할 수 있으므로
        // application context를 사용하는 것이 좋다.
        if(sInstance==null){
            sInstance = new GreatManDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_GREATMAN_TABLE = "CREATE TABLE "+
                GreatManContract.GreatManEntry.TABLE_NAME+" ("+
                BaseColumns._ID +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , "+
                GreatManContract.GreatManEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                GreatManContract.GreatManEntry.COLUMN_NAME_IMAGE + " BLOB, "+
                GreatManContract.GreatManEntry.COLUMN_BIRTHYEAR + " TEXT, " +
                GreatManContract.GreatManEntry.COLUMN_CONTENT +" TEXT, " +
                GreatManContract.GreatManEntry.COLUMN_MOT +" TEXT, " +
                GreatManContract.GreatManEntry.COLUMN_DATE +" TEXT, " +
                GreatManContract.GreatManEntry.COLUMN_NATIONALITY + " TEXT);";

        db.execSQL(SQL_CREATE_GREATMAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GreatManContract.GreatManEntry.TABLE_NAME);
        onCreate(db);

    }

    public void delete(int read_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+GreatManContract.GreatManEntry.TABLE_NAME+" where _id = "+read_id);

    }
}
