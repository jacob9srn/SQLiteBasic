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
import com.example.sqlitebasic.database.MemoDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class GreatManModifyActivity extends AppCompatActivity {

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

    private int read_id = 0;

    private ImageView mImageView;


    public Bitmap bitImg;
    private static final int PICK_IMAGE= 1011;
    private static final int RESULT_OK_Great_MODIFY= 1040;
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
        setContentView(R.layout.activity_greatman_modify);

        init();

        //// readItem에서 쏘아준 데이터를 modify에서 출력해주기.
        Bundle extras = getIntent().getExtras();
        read_id = extras.getInt("read_id");
        final String read_name = extras.getString("read_name");
        final String read_content = extras.getString("read_content");
        final String read_mot = extras.getString("read_mot");
        final String read_nationality = extras.getString("read_nationality");
        final String read_birthDay = extras.getString("read_birthDay");

        final int position = extras.getInt("read_position");
        if(null!=extras.getByteArray("read_image")) {
            final byte[] image = extras.getByteArray("read_image");
            mImageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }

        mEditTextName.setText(read_name);
        mEdiTextContent.setText(read_content);
        mEdiTextMot.setText(read_mot);
        mEdiTextNationality.setText(read_nationality);
        mEdiTextBirthYear.setText(read_birthDay);


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


            SQLiteDatabase db = GreatManDBHelper.getInstance(GreatManModifyActivity.super.getApplicationContext()).getWritableDatabase(); // DB에 삽입하기 전에 객체를 먼저 얻어 놓는다.

            String read_id_toString = String.valueOf(read_id); // 이 수정방법은 적어 놓아야 겠다.... 좀 어색해. 익숙하지 않아. 낯설어. 근데 해결했네. 미지의 문제를 .. 의구심만 들었던 문제를..

            String idArr[] = {read_id_toString};
            long newRowId = db.update(GreatManContract.GreatManEntry.TABLE_NAME, cv, "_id=?", idArr); // 위에서 설정한 contentValues를 이용한다.

        if(newRowId==-1){
            Toast.makeText(this, "수정 실패", Toast.LENGTH_SHORT).show();
        }else{
            setResult(RESULT_OK_Great_MODIFY);
            Toast.makeText(this, "수정했습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,GreatManActivity.class);
            startActivity(intent);

        }

            finish();

        }
        private void cancel() {
            setResult(RESULT_CANCELED);
            finish();

        }
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


