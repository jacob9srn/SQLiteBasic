package com.example.sqlitebasic;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitebasic.adapter.GreatManAdapter;
import com.example.sqlitebasic.adapter.GreatManData;
import com.example.sqlitebasic.adapter.MemoAdapter;
import com.example.sqlitebasic.adapter.MemoData;
import com.example.sqlitebasic.database.Contract;
import com.example.sqlitebasic.database.GreatManContract;
import com.example.sqlitebasic.database.GreatManDBHelper;
import com.example.sqlitebasic.database.MemoDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;


public class GreatManActivity extends AppCompatActivity {

    private ArrayList<GreatManData> arrayList;
    private GreatManAdapter mAdapter;
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Cursor cursor;

    private TextView mTextViewName;
    private TextView mTextViewBirthYear;
    private TextView mTextViewContent;
    private TextView mTextViewNationality;
    private TextView mTextViewMot;

    private FloatingActionButton floatingActionButton;

    private static final int REQUEST_CODE_GREAT_INSERT = 1010;
    private static final int REQUEST_CODE_GREAT_READ = 1020;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greatman);



        floatingActionButton = (FloatingActionButton) findViewById(R.id.great_floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(GreatManActivity.this, GreatManWriteActivity.class), REQUEST_CODE_GREAT_INSERT);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.great_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList = new ArrayList<>();
        mAdapter = new GreatManAdapter(this, arrayList);
        recyclerView.setAdapter(mAdapter);

        dbHelper = GreatManDBHelper.getInstance(this);
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + GreatManContract.GreatManEntry.TABLE_NAME, null);

        Comparator<GreatManData> cmpDsc = new Comparator<GreatManData>() {
            @Override
            public int compare(GreatManData item1, GreatManData item2) {
                int ret = 0;

                if (item1.getItem_id() < item2.getItem_id())
                    ret = 1;
                else if (item1.getItem_id() == item2.getItem_id())
                    ret = 0;
                else
                    ret = -1;
                return ret;
            }
        };

        if (cursor.moveToFirst()) {
            int item_id = cursor.getInt(0);
            String item_name = cursor.getString(1);
            String item_content = cursor.getString(4);
            String item_birthDay = cursor.getString(3);
            String item_nationality = cursor.getString(7);
            String item_date = cursor.getString(6);
            String item_mot = cursor.getString(5);
            if (null != cursor.getBlob(2)) {
                byte[] image = cursor.getBlob(2);
                GreatManData greatManData = new GreatManData(item_name, item_content, item_birthDay, item_nationality, item_date, item_mot, item_id, image);
                arrayList.add(greatManData);
            } else {
                GreatManData greatManData = new GreatManData(item_name, item_content, item_birthDay, item_nationality, item_date, item_mot, item_id, null);
                arrayList.add(greatManData);
            }

        }
        while (cursor.moveToNext()){
        int item_id = cursor.getInt(0);
        String item_name = cursor.getString(1);
        String item_content = cursor.getString(4);
        String item_birthDay = cursor.getString(3);
        String item_nationality = cursor.getString(7);
        String item_date = cursor.getString(6);
        String item_mot = cursor.getString(5);
        if (null != cursor.getBlob(2)) {
            byte[] image = cursor.getBlob(2);
            GreatManData greatManData = new GreatManData(item_name, item_content, item_birthDay, item_nationality, item_date, item_mot, item_id, image);
            arrayList.add(greatManData);
        } else {
            GreatManData greatManData = new GreatManData(item_name, item_content, item_birthDay, item_nationality, item_date, item_mot, item_id, null);
            arrayList.add(greatManData);
        }


    }
        Collections.sort(arrayList, cmpDsc);
       mAdapter.notifyDataSetChanged();


          mAdapter.setOnItemClickListener(new MemoAdapter.OnItemClickListener(){
              @Override
              public void onItemClick(View v, int position) {
                  Intent intent = new Intent(getBaseContext(), GreatManReadItemActivity.class);


                  intent.putExtra("id", arrayList.get(position).getItem_id());
                  intent.putExtra("position", position);
                  intent.putExtra("name", arrayList.get(position).getItem_name());

                  intent.putExtra("birthDay", arrayList.get(position).getItem_birthDay());
                  intent.putExtra("content", arrayList.get(position).getItem_content());
                  intent.putExtra("date", arrayList.get(position).getItem_date());
                  intent.putExtra("mot", arrayList.get(position).getItem_mot());
                  intent.putExtra("nationality", arrayList.get(position).getItem_nationality());



                  if(null!=arrayList.get(position).getItem_image()) {
                      intent.putExtra("image", arrayList.get(position).getItem_image());
                  }
                  startActivityForResult(intent, REQUEST_CODE_GREAT_READ);

              }
          });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Comparator<GreatManData> cmpDsc = new Comparator<GreatManData>() {
            @Override
            public int compare(GreatManData item1, GreatManData item2) {
                int ret = 0;

                if (item1.getItem_id() < item2.getItem_id())
                    ret = 1;
                else if (item1.getItem_id() == item2.getItem_id())
                    ret = 0;
                else
                    ret = -1;
                return ret;
            }
        };


        if (requestCode == REQUEST_CODE_GREAT_INSERT && resultCode == RESULT_OK) {// 한개의 게시물을 만들고 표시되도록 하기.

            cursor = db.rawQuery("SELECT * FROM " + GreatManContract.GreatManEntry.TABLE_NAME, null);
            cursor.moveToLast();
            //Toast.makeText(this, "삭제를해봐야아나 = " + cursor.getInt(0), Toast.LENGTH_SHORT).show();

            int item_id = cursor.getInt(0);
            String item_name = cursor.getString(1);
            String item_content = cursor.getString(4);
            String item_birthDay = cursor.getString(3);
            String item_nationality = cursor.getString(7);
            String item_date = cursor.getString(6);
            String item_mot = cursor.getString(5);

            if (null != cursor.getBlob(2)) {
                byte[] image = cursor.getBlob(2);
                GreatManData greatManData = new GreatManData(item_name, item_content, item_birthDay, item_nationality, item_date, item_mot, item_id, image);
                arrayList.add(greatManData);
            } else {
                GreatManData greatManData = new GreatManData(item_name, item_content, item_birthDay, item_nationality, item_date, item_mot, item_id, null);
                arrayList.add(greatManData);
            }

            Collections.sort(arrayList, cmpDsc);
            mAdapter.notifyDataSetChanged();
        }
        if (requestCode == REQUEST_CODE_GREAT_INSERT && resultCode == RESULT_CANCELED) {

        }
        if (requestCode == REQUEST_CODE_GREAT_READ && resultCode == 1012) { // 1012 은 DELETE코드

            int position = data.getIntExtra("position", 0);
            Toast.makeText(this, position+ "포지션 삭제 했습니다. ", Toast.LENGTH_SHORT).show();
//           for (int i = 0; i == arrayList.size(); i++) {
//                if (arrayList.get(i).getItem_id() == read_id) {
//                   arrayList.remove(i);
//                    Toast.makeText(this, read_id + " 삭제 했습니다.", Toast.LENGTH_SHORT).show();
//                }
            arrayList.remove(position);
            mAdapter.notifyItemRemoved(position);
            mAdapter.notifyItemRangeChanged(position,arrayList.size());

        }          // 어레이 리스트에서 몇번째를 삭제할까? id값으로 삭제해선 안된다. 몇번째 인지 만이 중요하다.

        if (requestCode == 1013 && resultCode == 1040) { // 1013Modify 1040는 RESULT_OK_Modify_코드
            int position = data.getIntExtra("position",0);
            mAdapter.notifyItemChanged(position);
        }
    }


//    private Cursor getAllItems(){
//        return mDatabase.query(
//                GreatManContract.GreatManEntry.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                GreatManContract.GreatManEntry._ID + " DESC"
//        );

 //   }



}