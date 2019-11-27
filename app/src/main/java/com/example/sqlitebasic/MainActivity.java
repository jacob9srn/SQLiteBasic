package com.example.sqlitebasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private Button main_btn_first;
    private Button main_btn_great;


    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.main_iv_first);

        Glide.with(this).load("http://wtimg.webtooninsight.co.kr/webtoonthumbnail/wi_131405132336691626.jpg").into(imageView);

        main_btn_first = (Button) findViewById(R.id.main_btn_first);
        main_btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getApplicationContext() , MemoActivity.class );
              startActivity(intent);

            }
        });

        main_btn_great = (Button) findViewById(R.id.main_btn_greatMan);
        main_btn_great.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , GreatManActivity.class);
                startActivity(intent);
            }
        });

    }
}

