package com.m3gv.news.business.homepage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.m3gv.news.R;
import com.m3gv.news.business.video.VideoDetailActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main_activity);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoDetailActivity.start(MainActivity.this, "");
            }
        });

    }

}