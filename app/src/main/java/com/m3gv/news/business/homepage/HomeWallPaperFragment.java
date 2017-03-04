package com.m3gv.news.business.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseFragment;
import com.m3gv.news.business.gamedata.HeroEntity;
import com.m3gv.news.common.db.RealmDbHelper;
import com.m3gv.news.common.util.CollectionUtil;
import com.m3gv.news.common.util.LogUtil;
import com.m3gv.news.common.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

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

    private List<byte[]> ram = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        f(R.id.btn_ex_test).setOnClickListener(onClickListener);
        f(R.id.btn_create).setOnClickListener(onClickListener);
        f(R.id.btn_save).setOnClickListener(onClickListener);
        f(R.id.btn_query).setOnClickListener(onClickListener);
        f(R.id.btn_update).setOnClickListener(onClickListener);
        f(R.id.btn_delete).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_ex_test:
                    try {
                        byte[] b = new byte[1024 * 1024 * 20];
                        ram.add(b);
                    } catch (OutOfMemoryError e) {
                        LogUtil.e("ExceptionTest", e.getMessage());
                    } finally {
                        LogUtil.e("ExceptionTest", "finally");
                    }
                    break;
                case R.id.btn_create:
                    HeroEntity heroEntity = new HeroEntity();
                    heroEntity.heroId = 123;
                    heroEntity.heroName = "meikai";
                    if (RealmDbHelper.getInstance().insert(heroEntity)) {
                        UIUtil.showToast("保存成功");
                    } else {
                        UIUtil.showToast("保存失败");
                    }
                    break;
                case R.id.btn_save:

                    break;
                case R.id.btn_query:
                    RealmResults<HeroEntity> realmResults = Realm.getDefaultInstance().where(HeroEntity.class)
                            .findAll();
                    if (CollectionUtil.isNotEmpty(realmResults)) {
                        UIUtil.showToast("查询到:" + realmResults.get(0).heroName);
                    }
                    break;
                case R.id.btn_update:

                    break;
                case R.id.btn_delete:

                    break;
            }
        }
    };
}
