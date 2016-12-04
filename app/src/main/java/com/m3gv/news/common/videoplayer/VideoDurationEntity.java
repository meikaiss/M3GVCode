package com.m3gv.news.common.videoplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by meikai on 16/12/3.
 */

public class VideoDurationEntity implements Parcelable {

    public int hour;
    public int minute;
    public int second;

    public int totalSecond;

    public VideoDurationEntity(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.totalSecond = getTotalSecond();
    }

    public VideoDurationEntity(int totalSecond) {
        updateDuration(totalSecond);
    }

    protected VideoDurationEntity(Parcel in) {
        hour = in.readInt();
        minute = in.readInt();
        second = in.readInt();
        totalSecond = getTotalSecond();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(second);
        dest.writeInt(totalSecond);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoDurationEntity> CREATOR = new Creator<VideoDurationEntity>() {
        @Override
        public VideoDurationEntity createFromParcel(Parcel in) {
            return new VideoDurationEntity(in);
        }

        @Override
        public VideoDurationEntity[] newArray(int size) {
            return new VideoDurationEntity[size];
        }
    };

    @Override
    public String toString() {
        return hour % 24 + ":" + String.format("%02d", minute % 60) + ":" + String.format("%02d", second % 60);
    }

    public void updateDuration(int newTotalSecond) {
        this.hour = newTotalSecond / (60 * 60);
        this.minute = newTotalSecond % (60 * 60) / 60;
        this.second = newTotalSecond % 60;
        this.totalSecond = newTotalSecond;
    }

    private int getTotalSecond() {
        return hour * 60 * 60 + minute * 60 + second;
    }

}
