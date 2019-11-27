package com.example.sqlitebasic;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitebasic.database.MemoDbHelper;

public class ReadItemActivity extends AppCompatActivity {


    private TextView readItem_title;
    private TextView readItem_content;

    private Button readItem_btn_modify;
    private Button readItem_btn_delete;

    private ImageView readItem_image;

    public MemoDbHelper memoDbHelper;
    private Context context;


    private static final int RESULT_CODE_DEALETE = 2000;
    private static final int RESULT_CODE_MODIFY = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readitem);
        context = this;
        readItem_title = (TextView)findViewById(R.id.tv_readItem_title);
        readItem_content = (TextView) findViewById(R.id.tv_readItem_contents);
        readItem_btn_delete = (Button) findViewById(R.id.btn_readItem_delete);

        readItem_btn_modify = (Button) findViewById(R.id.btn_readItem_modification);

        readItem_image = (ImageView) findViewById(R.id.iv_readItem_first);


        memoDbHelper = MemoDbHelper.getInstance(this);

            Bundle extras = getIntent().getExtras();

            final int read_id = extras.getInt("id");
            Log.d("리드 액티비티", String.valueOf(read_id));
            final String read_title = extras.getString("title");
        final String read_contents = extras.getString("contents");
            final int position = extras.getInt("position");
           if(null!=extras.getByteArray("image")) {
               final byte[] image = extras.getByteArray("image");
               readItem_image.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
           }
        readItem_title.setText(read_title);
        readItem_content.setText(read_contents);


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

                        memoDbHelper.delete(read_id);
                        Toast.makeText(ReadItemActivity.this, "삭제 했습니다.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                        intent.putExtra("read_id",read_id);
                        intent.putExtra("position",position);
                        setResult(RESULT_CODE_DEALETE,intent);
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
                Intent intent = new Intent(getApplicationContext(),ModifyActivity.class);
                intent.putExtra("read_id", read_id);
                intent.putExtra("read_title", read_title);
                intent.putExtra("read_contents", read_contents);
                intent.putExtra("read_position",position);
                setResult(RESULT_CODE_MODIFY,intent);
                startActivity(intent);
                finish();
            }
        });

    }
}
