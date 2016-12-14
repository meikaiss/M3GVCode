package com.m3gv.news.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by meikai on 16/12/3.
 */
public abstract class M3gBaseFragment extends Fragment {

    public boolean isDestroyed;

    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return rootView = super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }


    public <T extends View> T f(@IdRes int viewId) {
        if (rootView == null) {
            return null;
        }
        return (T) rootView.findViewById(viewId);
    }

    public <T extends View> T f(View view, @IdRes int viewResId) {
        return (T) view.findViewById(viewResId);
    }

}
