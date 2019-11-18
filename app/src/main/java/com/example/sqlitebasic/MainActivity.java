package com.example.sqlitebasic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //깃허브 반영
    static final String TAG = "메인 액티비티";

    private ArrayList<MemoData> arrayList;
    private MemoAdapter memoAdapter;

    private MemoDbHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;


    private static final int REQUEST_CODE_INSERT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,WriteActivity.class),REQUEST_CODE_INSERT); // WriteAcitvity에서 setResult
            }
        });



//리사이클러뷰를 리니어 레이아웃으로 설정

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // DB
        Log.i(TAG,"Before MemoDbHelper dbHelper = MemoDbHelper.getInstance(this)");
        dbHelper = MemoDbHelper.getInstance(this);
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+Contract.MemoEntry.TABLE_NAME,null);

        // 어뎁터뷰 생성
        arrayList = new ArrayList<>();
        Log.i(TAG,"Before memoAdapter = new MemoAdapter(this,arrayList;  arrayList.size : "  +arrayList.size());
        memoAdapter = new MemoAdapter(this,arrayList,db);
        Log.i(TAG,"Before recyclerView.setAdapter(memoAdapter)");
        recyclerView.setAdapter(memoAdapter);



        Log.i(TAG,"Before if(cursor.moveToFirst())");
        if(cursor.moveToFirst()){

            String item_title = cursor.getString(1);
            String item_contents = cursor.getString(2);
            MemoData memoData = new MemoData(item_title,item_contents);
            arrayList.add(memoData);
        }
        while(cursor.moveToNext()){
            Log.i(TAG,"In cursor.moveToNext()");
            String item_title = cursor.getString(1);
            String item_contents = cursor.getString(2);
            MemoData memoData = new MemoData(item_title,item_contents);
            arrayList.add(memoData);

        }
        Log.i(TAG,"Before memoAdapter.notifyDataSetChanged()");


        memoAdapter.setOnItemClickListener(new MemoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.i(TAG,"in memoAdapter.setOnItemClickListener");
               Intent intent = new Intent(getBaseContext(),ReadItemActivity.class);

               intent.putExtra("title" ,arrayList.get(position).getItem_title() );
                intent.putExtra("contents" ,arrayList.get(position).getItem_contens() );

               startActivity(intent);


            }
        });

        memoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "in onActivityResult");
        if(requestCode==REQUEST_CODE_INSERT && resultCode==RESULT_OK){
            cursor = db.rawQuery("SELECT * FROM "+Contract.MemoEntry.TABLE_NAME,null);
            cursor.moveToLast();
                String item_title = cursor.getString(1);
                String item_contents = cursor.getString(2);
                MemoData memoData = new MemoData(item_title,item_contents);
                arrayList.add(memoData);
            Log.i(TAG,"Before memoAdapter.notifyDataSetChanged()");
            memoAdapter.notifyDataSetChanged();
        }
        if(requestCode==REQUEST_CODE_INSERT && resultCode==RESULT_CANCELED){

        }

    }
}

