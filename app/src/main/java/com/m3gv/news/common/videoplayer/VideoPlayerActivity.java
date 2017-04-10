package com.m3gv.news.common.videoplayer;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.common.util.LogUtil;
import com.m3gv.news.common.videoplayer.exoplayer.ExoVideoPlayerFragment;

import java.lang.ref.WeakReference;

/**
 * Created by meikai on 16/12/14.
 */
public class VideoPlayerActivity extends M3gBaseActivity {

    private WeakReference<ExoVideoPlayerFragment> videoPlayerFragmentRef;

    private SensorManager sensorManager;
    private Sensor sensor;

    private SensorState currentSensorState = SensorState.UNKNOWN;

    public void bindVideoPlayerFragment(ExoVideoPlayerFragment videoPlayerFragment) {
        if (videoPlayerFragmentRef == null || videoPlayerFragmentRef.get() != videoPlayerFragment) {
            videoPlayerFragmentRef = new WeakReference<>(videoPlayerFragment);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onBackPressed() {
        if (videoPlayerFragmentRef != null) {
            ExoVideoPlayerFragment videoPlayerFragment = videoPlayerFragmentRef.get();
            if (videoPlayerFragment != null) {
                if (videoPlayerFragment.onBackPressed()) {
                    return;
                } else {
                    super.onBackPressed();
                }
            }
        }
    }


    protected void onFullScreenToggle(SensorState sensorState, int landScapeDir) {
        ExoVideoPlayerFragment videoPlayerFragment = videoPlayerFragmentRef.get();
        if (videoPlayerFragment != null) {
            videoPlayerFragment.onFullScreenToggle(sensorState, landScapeDir);
        }
    }

    /**
     * 根据 设置的屏幕状态 来设置 Activity的窗口属性
     *
     * @param newFullScreenState 期望设置的 屏幕状态
     */
    public void onFullScreenChange(boolean newFullScreenState, SensorState sensorState, int landScapeDir) {
        if (newFullScreenState) {
            //设置为 横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            if (sensorState == SensorState.LANDSCAPE) {
                if (landScapeDir > 0) {
                    getWindow().getDecorView().setRotation(0);
                } else {
//                    getWindow().getDecorView().setRotation(45);
                }
            } else if (sensorState == SensorState.PORTRAIT) {
                getWindow().getDecorView().setRotation(0);
            }
        } else {
            //设置为 竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(params);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            LogUtil.e("onSensorChanged", "x=" + event.values[0] + ",y=" + event.values[1] + ",z=" + event.values[2]);
            if (currentSensorState == SensorState.UNKNOWN) {
                if (Math.abs(event.values[0]) > Math.abs(event.values[1])) {
                    // 横屏
                    currentSensorState = SensorState.LANDSCAPE;
                } else {
                    // 竖屏
                    currentSensorState = SensorState.PORTRAIT;
                }
            } else {
                if (Math.abs(event.values[2]) > SensorManager.GRAVITY_EARTH / 2) {
                    // 手机屏幕平面 与 水平面 平行或接近平行 的 情况不检测 重力感应
                    return;
                }

                SensorState newsSensorState;
                if (Math.abs(event.values[0]) > Math.abs(event.values[1])) {
                    // 横屏
                    newsSensorState = SensorState.LANDSCAPE;
                } else {
                    // 竖屏
                    newsSensorState = SensorState.PORTRAIT;
                }

                if (currentSensorState != newsSensorState) {
                    onFullScreenToggle(newsSensorState, event.values[0] > 0 ? 1 : -1);
                    currentSensorState = newsSensorState;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
