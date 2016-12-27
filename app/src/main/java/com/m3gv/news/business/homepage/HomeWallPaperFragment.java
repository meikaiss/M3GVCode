package com.m3gv.news.business.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseFragment;
import com.m3gv.news.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meikai on 16/12/27.
 */
public class HomeWallPaperFragment extends M3gBaseFragment {

    public static HomeWallPaperFragment newInstance() {

        Bundle args = new Bundle();

        HomeWallPaperFragment fragment = new HomeWallPaperFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return rootView = inflater.inflate(R.layout.home_wall_paper_fragment, container, false);
    }

    List<byte[]> ram = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        f(R.id.btn_ex_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    byte[] b = new byte[1024 * 1024 * 20];
                    ram.add(b);
                } catch (OutOfMemoryError e) {
                    LogUtil.e("ExceptionTest", e.getMessage());
                } finally {
                    LogUtil.e("ExceptionTest", "finally");
                }
            }
        });
    }
}
