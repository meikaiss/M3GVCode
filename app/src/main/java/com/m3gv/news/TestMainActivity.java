package com.m3gv.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.business.news.ArticleNewsDetailActivity;

public class TestMainActivity extends M3gBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main_activity);


        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestMainActivity.this, ArticleNewsDetailActivity.class);
                startActivity(intent);

            }
        });


    }

}