package com.m3gv.news.business.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.common.view.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meikai on 16/12/3.
 */

public class HomePageActivity extends M3gBaseActivity {

    private ViewGroup emptyViewGroup;
    private ViewGroup loadingViewGroup;
    private ViewGroup noNetViewGroup;
    private XRecyclerView xRecyclerView;

    private List<VideoNewsEntity> dataList = new ArrayList<>();
    private VideoNewsAdapter videoNewsAdapter;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, HomePageActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main_activity);

        xRecyclerView = f(R.id.base_x_recycler_view);

        emptyViewGroup = f(R.id.empty_view);
        loadingViewGroup = f(R.id.loading_view);
        noNetViewGroup = f(R.id.net_error_view);

        xRecyclerView.setVisibility(View.GONE);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        loadingViewGroup.setVisibility(View.VISIBLE);

        AVQuery<AVObject> avQuery = new AVQuery<>("VideoNews");
        avQuery.orderByDescending("updatedAt").whereEqualTo("enable", true);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (HomePageActivity.this == null
                        || HomePageActivity.this.isDestroyed()
                        || list == null) {
                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    dataList.add(VideoNewsEntity.parse(list.get(i)));
                }

                videoNewsAdapter = new VideoNewsAdapter(HomePageActivity.this, dataList);
                xRecyclerView.setAdapter(videoNewsAdapter);

                xRecyclerView.setVisibility(View.VISIBLE);
                loadingViewGroup.setVisibility(View.GONE);

            }
        });
    }


}
