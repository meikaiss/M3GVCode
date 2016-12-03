package com.m3gv.news.business.video;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by meikai on 16/12/3.
 */

public class VideoEntity implements Parcelable {

    public String title;
    public String url;
    public VideoDurationEntity durationEntity;

    public VideoEntity() {
    }

    protected VideoEntity(Parcel in) {
        title = in.readString();
        url = in.readString();
        durationEntity = in.readParcelable(VideoDurationEntity.class.getClassLoader());
    }

    public static final Creator<VideoEntity> CREATOR = new Creator<VideoEntity>() {
        @Override
        public VideoEntity createFromParcel(Parcel in) {
            return new VideoEntity(in);
        }

        @Override
        public VideoEntity[] newArray(int size) {
            return new VideoEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeParcelable(durationEntity, flags);
    }
}
