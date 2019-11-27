package com.example.sqlitebasic;

import android.content.ContentValues;
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
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitebasic.database.Contract;
import com.example.sqlitebasic.database.GreatManContract;
import com.example.sqlitebasic.database.GreatManDBHelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class GreatManWriteActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;

    public GreatManDBHelper greatManDBHelper;

    private EditText mEditTextName;
    private EditText mEdiTextBirthYear;
    private EditText mEdiTextContent;
    private EditText mEdiTextNationality;
    private EditText mEdiTextMot;
    private Button btn_photoRegister;
    private Button btn_save;
    private Button btn_cancel;


    private ImageView mImageView;
    public Bitmap bitImg;
    private static final int PICK_IMAGE= 1011;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE){
            if(resultCode == RESULT_OK){
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

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greatman_write);

        init();



        greatManDBHelper = GreatManDBHelper.getInstance(this);

        Button btn_photoRegister = findViewById(R.id.modify_btn_great_photoRegister);
        Button btn_save = findViewById(R.id.modify_btn_great_save);
        Button btn_cancel = findViewById(R.id.modify_btn_great_cancel);

        GreatManDBHelper dbHelper = new GreatManDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    save();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel();
                }
            });

            btn_photoRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoRegister();
                }
            });
    }

        private void save() {

        if(mEditTextName.getText().toString().trim().length()==0 ){
            Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mEdiTextBirthYear.getText().toString().trim().length()==0 ) {
            Toast.makeText(this, "생몰연도를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mEdiTextNationality.getText().toString().trim().length()==0 ){
                Toast.makeText(this, "국적을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
        }



        String name = mEditTextName.getText().toString();
            String birthDay = mEdiTextBirthYear.getText().toString();
            String nationality = mEdiTextNationality.getText().toString();
            String content = mEdiTextContent.getText().toString();
            String mot = mEdiTextMot.getText().toString();
            String date = new SimpleDateFormat("YYYY. MM. DD. HH:mm:ss").format(System.currentTimeMillis());
            Log.d("date : ", date);

        ContentValues cv = new ContentValues();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if(bitImg==null){

            }else{
                bitImg.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] data = stream.toByteArray();


                cv.put(Contract.MemoEntry.COLUMN_NAME_IMAGE, data);
            }

        cv.put(GreatManContract.GreatManEntry.COLUMN_NAME, name);
            cv.put(GreatManContract.GreatManEntry.COLUMN_BIRTHYEAR, birthDay);
            cv.put(GreatManContract.GreatManEntry.COLUMN_CONTENT, content);
            cv.put(GreatManContract.GreatManEntry.COLUMN_MOT, mot);
            cv.put(GreatManContract.GreatManEntry.COLUMN_NATIONALITY, nationality);
            cv.put(GreatManContract.GreatManEntry.COLUMN_DATE, date);
            mDatabase = greatManDBHelper.getWritableDatabase();

            Long newRow = mDatabase.insert(GreatManContract.GreatManEntry.TABLE_NAME, null, cv);
        if(newRow==-1){
            Toast.makeText(this, "저장 실패", Toast.LENGTH_SHORT).show();
        }else{
            setResult(RESULT_OK);
            Toast.makeText(this, "저장했습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,GreatManActivity.class);

        }

            finish();

        }
        private void cancel() {}
        private void photoRegister(){
            mImageView.setVisibility(View.VISIBLE);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,PICK_IMAGE);

        }




        private void init(){

            mEditTextName = findViewById(R.id.modify_itemlist_great_name);
            mEdiTextBirthYear = findViewById(R.id.modify_item_great_birthYear);
            mEdiTextContent  =  findViewById(R.id.modify_item_great_content);
            mEdiTextNationality = findViewById(R.id.modify_item_great_nationality);
            mEdiTextMot = findViewById(R.id.modify_item_great_mot);
            mImageView = findViewById(R.id.modify_item_great_imageView);
        }


}


