package com.example.sqlitebasic;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mContentsEditText;

    private Button btn_save;
    private Button btn_cancle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);


        mContentsEditText = findViewById(R.id.et_contents);
        mTitleEditText = findViewById(R.id.et_title);


        btn_save = findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String title =mTitleEditText.getText().toString();
              String contents = mContentsEditText.getText().toString();

                ContentValues contentValues = new ContentValues();  // DB에 정보를 넘겨주는 것.
                contentValues.put(Contract.MemoEntry.COLUMN_NAME_TITLE, title);
                contentValues.put(Contract.MemoEntry.COLUMN_NAME_CONTENTS, contents);

                SQLiteDatabase db = MemoDbHelper.getInstance(WriteActivity.super.getApplicationContext()).getWritableDatabase(); // DB에 삽입하기 전에 객체를 먼저 얻어 놓는다.

                long newRowId = db.insert(Contract.MemoEntry.TABLE_NAME,null,contentValues); // 위에서 설정한 contentValues를 이용한다.

                if(newRowId==-1){
                    Toast.makeText(WriteActivity.super.getApplicationContext(), "저장 실패!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(WriteActivity.super.getApplicationContext(), "저장에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                }

                finish(); }

            });

        btn_cancle = findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }


    //이벤트공부
    //


    public void onBackPressed() { // 설명이 없다.. 메모 작성 도중 뒤로가기 눌렀을 때 저장되도록 하는 것.

        final String title1 = mTitleEditText.getText().toString();
        final String contents1 = mContentsEditText.getText().toString();


            if (title1.equals("") && contents1.equals("")) {
                super.onBackPressed();
            } else {
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.custom_dialog1, null);

                Button btn_dialog_ok = (Button) view.findViewById(R.id.btn_dialog_ok);
                Button btn_dialog_cancel = (Button) view.findViewById(R.id.btn_dialog_cancel);
                Button btn_dialog_notSave = (Button) view.findViewById(R.id.btn_dialog_notSave);

                alert.setView(view);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false); //

                btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btn_dialog_notSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        finish();
                    }
                });


                btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();  // DB에 정보를 넘겨주는 것.
                        contentValues.put(Contract.MemoEntry.COLUMN_NAME_TITLE, title1);
                        contentValues.put(Contract.MemoEntry.COLUMN_NAME_CONTENTS, contents1);

                        SQLiteDatabase db = MemoDbHelper.getInstance(WriteActivity.super.getApplicationContext()).getWritableDatabase(); // DB에 삽입하기 전에 객체를 먼저 얻어 놓는다.

                        long newRowId = db.insert(Contract.MemoEntry.TABLE_NAME, null, contentValues); // 위에서 설정한 contentValues를 이용한다.

                        if (newRowId == -1) {
                            Toast.makeText(WriteActivity.super.getApplicationContext(), "저장 실패!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(WriteActivity.super.getApplicationContext(), "저장에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            alertDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            finish();
                        }
                    }
                });
                alertDialog.show();

            }

        }

    }


