package com.example.sqlitebasic;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitebasic.database.GreatManDBHelper;

public class GreatManReadItemActivity extends AppCompatActivity {

    private TextView mEditTextName;
    private TextView mEdiTextBirthYear;
    private TextView mEdiTextContent;
    private TextView mEdiTextNationality;
    private TextView mEdiTextMot;

    private Button readItem_btn_modify;
    private Button readItem_btn_delete;
    private Button readItem_btn_cancel;

    private ImageView readItem_image;

   public GreatManDBHelper greatManDBHelper;
    private Context context;

    private static final int RESULT_CODE_GREAT_DEALETE = 1012;
    private static final int RESULT_CODE_GREAT_MODIFY = 1013;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greatman_readitem);
        context=this;


        mEditTextName = findViewById(R.id.modify_itemlist_great_name);
         mEdiTextBirthYear = findViewById(R.id.modify_item_great_birthYear);;
         mEdiTextContent= findViewById(R.id.modify_item_great_content);
        mEdiTextNationality= findViewById(R.id.modify_item_great_nationality);
        mEdiTextMot= findViewById(R.id.modify_item_great_mot);

        readItem_btn_modify= findViewById(R.id.btn_great_modify);
         readItem_btn_delete= findViewById(R.id.modify_btn_great_save);
        readItem_btn_cancel = findViewById(R.id.modify_btn_great_cancel);

       readItem_image= findViewById(R.id.modify_item_great_imageView);

        greatManDBHelper = GreatManDBHelper.getInstance(this);
        Bundle extras = getIntent().getExtras();

        final int read_id = extras.getInt("id");

        final String read_name = extras.getString("name");

        final String read_content = extras.getString("content");
        final String read_date = extras.getString("date");
        final String read_mot = extras.getString("mot");
        final String read_nationality = extras.getString("nationality");
        final String read_birthDay = extras.getString("birthDay");

        final int position = extras.getInt("position");
        if(null!=extras.getByteArray("image")) {
            final byte[] image = extras.getByteArray("image");
            readItem_image.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }
        mEditTextName.setText(read_name);
        mEdiTextBirthYear.setText(read_birthDay);
        mEdiTextContent.setText(read_content);
        mEdiTextMot.setText(read_mot);
        mEdiTextNationality.setText(read_nationality);

        readItem_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                View view = getLayoutInflater().inflate(R.layout.custom_dialog_delete, null);

                Button btn_dialog_ok = (Button) view.findViewById(R.id.delete_btn_dialog_ok);
                Button btn_dialog_cancel = (Button) view.findViewById(R.id.delete_btn_dialog_cancel);

                alert.setView(view);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        greatManDBHelper.delete(read_id);
                        Toast.makeText(GreatManReadItemActivity.this, "삭제 했습니다.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), GreatManActivity.class);
                        intent.putExtra("read_id",read_id);
                        intent.putExtra("position",position);
                        setResult(RESULT_CODE_GREAT_DEALETE,intent);
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog.show();

            }
        });

        readItem_btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GreatManModifyActivity.class);
                intent.putExtra("read_id", read_id);
                intent.putExtra("read_name", read_name);
                intent.putExtra("read_content", read_content);
                intent.putExtra("read_position",position);

                intent.putExtra("read_nationality",read_nationality);
                intent.putExtra("read_birthDay",read_birthDay);
                intent.putExtra("read_mot",read_mot);
                Bundle extras = getIntent().getExtras();
                if(null!=extras.getByteArray("image")) {
                    final byte[] image = extras.getByteArray("image");
                    intent.putExtra("read_image", image);
                }


                setResult(RESULT_CODE_GREAT_MODIFY,intent);
                startActivity(intent);
                finish();
            }
        });


    }
}
