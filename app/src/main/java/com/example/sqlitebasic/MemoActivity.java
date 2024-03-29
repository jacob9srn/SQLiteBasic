package com.example.sqlitebasic;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitebasic.adapter.MemoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.example.sqlitebasic.database.Contract;
import com.example.sqlitebasic.adapter.MemoData;
import com.example.sqlitebasic.database.MemoDbHelper;

public class MemoActivity extends AppCompatActivity {

    //깃허브 반영
    //푸시어딨냐
    static final String TAG = "메모 액티비티";

    private ArrayList<MemoData> arrayList;
    private MemoAdapter memoAdapter;

    private MemoDbHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;


    private static final int REQUEST_CODE_INSERT = 1000;
    private static final int REQUEST_CODE_READ = 1001;


    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);



        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MemoActivity.this, WriteActivity.class), REQUEST_CODE_INSERT); // WriteAcitvity에서 setResult
            }
        });


//리사이클러뷰를 리니어 레이아웃으로 설정

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // DB
        Log.i(TAG, "Before MemoDbHelper dbHelper = MemoDbHelper.getInstance(this)");

        dbHelper = MemoDbHelper.getInstance(this);
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + Contract.MemoEntry.TABLE_NAME, null);

        // 어뎁터뷰 생성
        arrayList = new ArrayList<>();
        Log.i(TAG, "Before memoAdapter = new MemoAdapter(this,arrayList;  arrayList.size : " + arrayList.size());
        memoAdapter = new MemoAdapter(this, arrayList);
        Log.i(TAG, "Before recyclerView.setAdapter(memoAdapter)");
        recyclerView.setAdapter(memoAdapter);


        Comparator<MemoData> cmpDsc = new Comparator<MemoData>() {
            @Override
            public int compare(MemoData item1, MemoData item2) {
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


        Log.i(TAG, "Before if(cursor.moveToFirst())");
        if (cursor.moveToFirst()) {
            int item_id = cursor.getInt(0);

            String item_title = cursor.getString(1);
            String item_contents = cursor.getString(2);

            if (null != cursor.getBlob(3)) {
                byte[] image = cursor.getBlob(3);
                MemoData memoData = new MemoData(item_title, item_contents, item_id, image);
                arrayList.add(memoData);
            } else {
                MemoData memoData = new MemoData(item_title, item_contents, item_id, null);
                arrayList.add(memoData);
            }


        }
        while (cursor.moveToNext()) {
            Log.i(TAG, "In cursor.moveToNext()");
            int item_id = cursor.getInt(0);
            String item_title = cursor.getString(1);
            String item_contents = cursor.getString(2);

            if (null != cursor.getBlob(3)) {
                byte[] image = cursor.getBlob(3);
                MemoData memoData = new MemoData(item_title, item_contents, item_id, image);
                arrayList.add(memoData);
            } else {
                MemoData memoData = new MemoData(item_title, item_contents, item_id, null);
                arrayList.add(memoData);
            }

        }
        Log.i(TAG, "Before memoAdapter.notifyDataSetChanged()");


        memoAdapter.setOnItemClickListener(new MemoAdapter.OnItemClickListener() {  //한개의 게시글 클릭하기.
            @Override
            public void onItemClick(View v, int position) {
                Log.i(TAG, "in memoAdapter.setOnItemClickListener");
                Intent intent = new Intent(getBaseContext(), ReadItemActivity.class);

                Log.d(TAG, "id : " + String.valueOf(arrayList.get(position).getItem_id()) + "사이즈 = " + arrayList.size() + "제목" + arrayList.get(position).getItem_title());
                Log.d(TAG, "포지션 : " + String.valueOf(position));
                intent.putExtra("id", arrayList.get(position).getItem_id());
                intent.putExtra("title", arrayList.get(position).getItem_title());
                intent.putExtra("contents", arrayList.get(position).getItem_contens());
                intent.putExtra("position", position);
                if(null!=arrayList.get(position).getItem_image()) {
                    intent.putExtra("image", arrayList.get(position).getItem_image());
                }
                startActivityForResult(intent, REQUEST_CODE_READ);

            }
        });

        Collections.sort(arrayList, cmpDsc);
        memoAdapter.notifyDataSetChanged();

        //테이블삭제
        //db.execSQL("drop table "+Contract.MemoEntry.TABLE_NAME);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Comparator<MemoData> cmpDsc = new Comparator<MemoData>() {
            @Override
            public int compare(MemoData item1, MemoData item2) {
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

        Log.i(TAG, "in onActivityResult");
        if (requestCode == REQUEST_CODE_INSERT && resultCode == RESULT_OK) {// 한개의 게시물을 만들고 표시되도록 하기.
            Log.i(TAG, "in onActivityResult INSERT");
            cursor = db.rawQuery("SELECT * FROM " + Contract.MemoEntry.TABLE_NAME, null);
            cursor.moveToLast();
            //Toast.makeText(this, "삭제를해봐야아나 = " + cursor.getInt(0), Toast.LENGTH_SHORT).show();
            int item_id = cursor.getInt(0);
            String item_title = cursor.getString(1);
            String item_contents = cursor.getString(2);

            if(null != cursor.getBlob(3)){
                byte[] image = cursor.getBlob(3);
                MemoData memoData = new MemoData(item_title, item_contents, item_id, image);
                arrayList.add(memoData);
            }else{
                MemoData memoData = new MemoData(item_title, item_contents, item_id, null);   // 이것 때문에 글쓰고 나서 id값이 자꾸 null(0)이 나왔다. 그럼 어딘가에서 id값이 안들어갔다는 것을 유추 했어야 했다./                // .. 왜 오류가 안났지?????????????????????????????????????????????????????????????????????????????????? 생성자의 인자 갯수가 다른데.. 생성자가 여러개 였다ㅣ
                arrayList.add(memoData);
            }


        Log.i(TAG, "Before memoAdapter.notifyDataSetChanged()");

        Collections.sort(arrayList, cmpDsc);
        memoAdapter.notifyDataSetChanged();
    }
        if (requestCode == REQUEST_CODE_INSERT && resultCode == RESULT_CANCELED) {

        }
        if (requestCode == REQUEST_CODE_READ && resultCode == 2000) { // 2000 은 DELETE코드
            Log.i(TAG, "in onActivityResult DELETE");
            int position = data.getIntExtra("position", 0);
            Toast.makeText(this, position+ "포지션 삭제 했습니다. 2000", Toast.LENGTH_SHORT).show();
//           for (int i = 0; i == arrayList.size(); i++) {
//                if (arrayList.get(i).getItem_id() == read_id) {
//                   arrayList.remove(i);
//                    Toast.makeText(this, read_id + " 삭제 했습니다.", Toast.LENGTH_SHORT).show();
//                }
                arrayList.remove(position);
               memoAdapter.notifyItemRemoved(position);
               memoAdapter.notifyItemRangeChanged(position,arrayList.size());

           }          // 어레이 리스트에서 몇번째를 삭제할까? id값으로 삭제해선 안된다. 몇번째 인지 만이 중요하다.

        if (requestCode == 2001 && resultCode == 2002) { // 2001Modify 2002는 RESULT_OK_Modify_코드
            Log.i(TAG, "in onActivityResult Modify");

            int position = data.getIntExtra("position",0);
            memoAdapter.notifyItemChanged(position);

        }

    }

    }


