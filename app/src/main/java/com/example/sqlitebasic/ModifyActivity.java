package com.example.sqlitebasic;

import   android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ModifyActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1000;
    private static final int RESULT_OK_MODIFY = 2002;


    private EditText mTitleEditText;
    private EditText mContentsEditText;

    private ImageView mImageView;

    private Button btn_photoRegister;
    private Button btn_save;
    private Button btn_cancle;
    public MemoDbHelper memoDbHelper;
    private int read_id = 0;
    public Bitmap bitImg;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    bitImg = BitmapFactory.decodeStream(in);
                    in.close();
                    mImageView.setImageBitmap(bitImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        mContentsEditText = findViewById(R.id.modify_et_contents);
        mTitleEditText = findViewById(R.id.modify_et_title);

        //// readItem에서 쏘아준 데이터를 modify에서 출력해주기.
        Bundle extras = getIntent().getExtras();
        read_id = extras.getInt("read_id");
        Log.d("모디파이 액티비티", String.valueOf(read_id));
        final String read_title = extras.getString("read_title");
        final String read_contents = extras.getString("read_contents");
        final int position = extras.getInt("read_position");
        Log.d("모디파이 액티비티", read_title);
        mTitleEditText.setText(read_title);
        mContentsEditText.setText(read_contents);
        Log.d("모디파이 액티비티", read_contents);
        memoDbHelper = MemoDbHelper.getInstance(this);


        btn_photoRegister = findViewById(R.id.modify_btn_photoRegister);
        btn_save = findViewById(R.id.modify_btn_save);
//갤러리에 접근 하기
        mImageView = findViewById(R.id.modify_write_iv_first);

        btn_photoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });


//                    btn_save.setOnLongClickListener(new View.OnLongClickListener() {
//                        @Override
//                        public boolean onLongClick(View v) {
//                            String title = mTitleEditText.getText().toString();
//                            String contents = mContentsEditText.getText().toString();
//                            //임시
//
//                            memoDbHelper.insert(title,contents,null);
//
//                            return false;
//                        }
//                    });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleEditText.getText().toString();
                String contents = mContentsEditText.getText().toString();
                // 비트맵 이미지를 바이트 타입으로 저장. SQLite가 이미지를 이런식으로 저장하기 때문.
                ContentValues contentValues = new ContentValues();  // DB에 정보를 넘겨주는 것.
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (bitImg == null) {

                } else {
                    bitImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] data = stream.toByteArray();


                    contentValues.put(Contract.MemoEntry.COLUMN_NAME_IMAGE, data);
                }

                contentValues.put(Contract.MemoEntry.COLUMN_NAME_TITLE, title);
                contentValues.put(Contract.MemoEntry.COLUMN_NAME_CONTENTS, contents);


                SQLiteDatabase db = MemoDbHelper.getInstance(ModifyActivity.super.getApplicationContext()).getWritableDatabase(); // DB에 삽입하기 전에 객체를 먼저 얻어 놓는다.

                String read_id_toString = String.valueOf(read_id); // 이 수정방법은 적어 놓아야 겠다.... 좀 어색해. 익숙하지 않아. 낯설어. 근데 해결했네. 미지의 문제를 .. 의구심만 들었던 문제를..

                String idArr[] = {read_id_toString};
                long newRowId = db.update(Contract.MemoEntry.TABLE_NAME, contentValues, "_id=?", idArr); // 위에서 설정한 contentValues를 이용한다.

                if (newRowId == -1) {
                    Toast.makeText(ModifyActivity.super.getApplicationContext(), "수정 실패!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyActivity.super.getApplicationContext(), "수정에 성공했습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                    setResult(RESULT_OK_MODIFY);
                    intent.putExtra("position", position);
                    intent.putExtra("modify_title", title);
                    intent.putExtra("modify_content", contents);
                    startActivity(intent);

                }

                finish();
            }

        });


        btn_cancle = findViewById(R.id.modify_btn_cancle);
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


//    public void onBackPressed() { // 설명이 없다.. 메모 작성 도중 뒤로가기 눌렀을 때 저장되도록 하는 것.
//
//        final String title1 = mTitleEditText.getText().toString();
//        final String contents1 = mContentsEditText.getText().toString();
//
//
//            if (title1.equals("") && contents1.equals("")) {
//                super.onBackPressed();
//            } else {
//                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
//                View view = getLayoutInflater().inflate(R.layout.custom_dialog1, null);
//
//                Button btn_dialog_ok = (Button) view.findViewById(R.id.btn_dialog_ok);
//                Button btn_dialog_cancel = (Button) view.findViewById(R.id.btn_dialog_cancel);
//                Button btn_dialog_notSave = (Button) view.findViewById(R.id.btn_dialog_notSave);
//
//                alert.setView(view);
//                final AlertDialog alertDialog = alert.create();
//                alertDialog.setCanceledOnTouchOutside(false); //
//
//                btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                btn_dialog_notSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                        finish();
//                    }
//                });
//
//
//                btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ContentValues contentValues = new ContentValues();  // DB에 정보를 넘겨주는 것.
//                        contentValues.put(Contract.MemoEntry.COLUMN_NAME_TITLE, title1);
//                        contentValues.put(Contract.MemoEntry.COLUMN_NAME_CONTENTS, contents1);
//
//                        SQLiteDatabase db = MemoDbHelper.getInstance(ModifyActivity.super.getApplicationContext()).getWritableDatabase(); // DB에 삽입하기 전에 객체를 먼저 얻어 놓는다.
//
//                        long newRowId = db.insert(Contract.MemoEntry.TABLE_NAME, null, contentValues); // 위에서 설정한 contentValues를 이용한다.
//
//                        if (newRowId == -1) {
//                            Toast.makeText(ModifyActivity.super.getApplicationContext(), "저장 실패!", Toast.LENGTH_SHORT).show();
//                            alertDialog.dismiss();
//                        } else {
//                            Toast.makeText(ModifyActivity.super.getApplicationContext(), "저장에 성공했습니다.", Toast.LENGTH_SHORT).show();
//                            setResult(RESULT_OK);
//                            alertDialog.dismiss();
//                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                            finish();
//                        }
//                    }
//                });
//                alertDialog.show();
//
//            }

        }




