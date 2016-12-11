package com.m3gv.news.business.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.business.video.VideoListFragment;

/**
 * Created by meikai on 16/12/10.
 */

public class HomeActivity extends M3gBaseActivity implements View.OnClickListener {

    private TextView tvMenu1;
    private TextView tvMenu2;
    private TextView tvMenu3;
    private TextView tvMenu4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_frame_container, VideoListFragment.newInstance()).commitAllowingStateLoss();

        tvMenu1 = f(R.id.home_menu_tv_1);
        tvMenu2 = f(R.id.home_menu_tv_2);
        tvMenu3 = f(R.id.home_menu_tv_3);
        tvMenu4 = f(R.id.home_menu_tv_4);

        tvMenu1.setOnClickListener(this);
        tvMenu2.setOnClickListener(this);
        tvMenu3.setOnClickListener(this);
        tvMenu4.setOnClickListener(this);

        tvMenu1.setSelected(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_menu_tv_1:

                break;
            case R.id.home_menu_tv_2:

                break;
            case R.id.home_menu_tv_3:

                break;
            case R.id.home_menu_tv_4:

                break;
            default:
                break;
        }
    }

}
