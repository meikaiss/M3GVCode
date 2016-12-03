package com.m3gv.news;

import android.os.Bundle;
import android.view.View;

import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.business.video.VideoDetailActivity;

public class TestMainActivity extends M3gBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main_activity);

        f(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoDetailActivity.start(TestMainActivity.this, "");
            }
        });

    }

}