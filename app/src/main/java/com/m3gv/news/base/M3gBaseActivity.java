package com.m3gv.news.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by meikai on 16/12/3.
 */

public class M3gBaseActivity extends AppCompatActivity {

    public boolean isDestroyed;

    public M3gBaseActivity() {
        M3Config.setCurrentActivity(this);
    }

    public <T extends View> T f(@IdRes int viewId) {
        return (T) findViewById(viewId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isDestroyed = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.clear();
        }
    }

    public boolean getDestroyedFlag() {
        return isDestroyed;
    }
}
