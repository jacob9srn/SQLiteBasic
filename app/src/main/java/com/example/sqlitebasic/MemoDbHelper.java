package com.example.sqlitebasic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

public class MemoDbHelper extends SQLiteOpenHelper {

    static final String TAG = "메모 디비 핼퍼 ";

    private static MemoDbHelper sInstance;
    private static final int DB_VERSION = 1; // 스키마 변경시 숫자를 올린다.
    private static final String DB_NAME = "Memo.db";// db이름

    // 테이블 생성 SQL문
    private static final String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
            Contract.MemoEntry.TABLE_NAME,
    Contract.MemoEntry._ID,
    Contract.MemoEntry.COLUMN_NAME_TITLE,
    Contract.MemoEntry.COLUMN_NAME_CONTENTS);
//
    // 테이블 삭제 SQL문

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Contract.MemoEntry.TABLE_NAME;

    // 테이블 셀렉트 문



    // 기본 생성자인가? // 생성자를 private로 직접 인스턴스화를 방지하고 getInstance()를통해 인스턴스를 얻어야 함.
    //public MemoDbHelper(Context context){
   //     super(context,DB_NAME,null,DB_VERSION);
   // }
    private MemoDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        Log.i(TAG,"생성자 안");
    }


    // 팩토리 메서드 ??

     public static synchronized MemoDbHelper getInstance(Context context){
        // 액티비티의 context가 메모리 릭을 발생할 수 있으므로
         // application context를 사용하는 것이 좋다.
         if(sInstance==null){

             sInstance = new MemoDbHelper(context.getApplicationContext());
         }
         return sInstance;
     }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,"START onCreate and Before db.exesSQL(db.execSQL(\"DROP TABLE IF EXISTS \" + Contract.MemoEntry.TABLE_NAME);)");
        db.execSQL("DROP TABLE IF EXISTS " + Contract.MemoEntry.TABLE_NAME);
        Log.i(TAG,"in onCreate and Before db.exesSQL(String.format(\"CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)\",\n" +
                "            Contract.MemoEntry.TABLE_NAME,\n" +
                "    Contract.MemoEntry._ID,\n" +
                "    Contract.MemoEntry.COLUMN_NAME_TITLE,\n" +
                "    Contract.MemoEntry.COLUMN_NAME_CONTENTS);)");
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DB스키마가 변경될 때 여기서 데이터르 백업하고, 테이블 삭제 후 재생성 및 데이터 복원등을 한다.
        Log.i(TAG,"START onUpgrage and Before db.exesSQL(\"DROP TABLE IF EXISTS \" + Contract.MemoEntry.TABLE_NAME;)");
        db.execSQL(SQL_DELETE_ENTRIES);
        Log.i(TAG,"In onUpgrage and Before onCreate(db)");
        onCreate(db);
    }
}
