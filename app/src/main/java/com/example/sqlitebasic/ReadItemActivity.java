package com.example.sqlitebasic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ReadItemActivity  extends AppCompatActivity {


    private TextView readItem_title;
    private TextView readItem_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readitem);

        readItem_title = (TextView)findViewById(R.id.tv_readItem_title);
        readItem_content = (TextView) findViewById(R.id.tv_readItem_contents);

            Bundle extras = getIntent().getExtras();

            String read_title = extras.getString("title");
        String read_contents = extras.getString("contents");
        readItem_title.setText(read_title);
        readItem_content.setText(read_contents);




    }
}
