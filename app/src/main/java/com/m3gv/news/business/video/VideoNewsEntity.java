package com.m3gv.news.business.video;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVObject;

/**
 * Created by meikai on 16/12/3.
 */
public class VideoNewsEntity implements Parcelable {

    public AVObject avObject;

    public int videoDuration;
    public String videoTitle;
    public int playCount;
    public int zanCount;
    public int caiCount;
    public String videoUrl;
    public String thumbnail;
    public int videoResolution;
    public int videoId;
    public int categoryId;

    public VideoNewsEntity() {
    }

    protected VideoNewsEntity(Parcel in) {
        videoDuration = in.readInt();
        videoTitle = in.readString();
        playCount = in.readInt();
        zanCount = in.readInt();
        caiCount = in.readInt();
        videoUrl = in.readString();
        thumbnail = in.readString();
        videoResolution = in.readInt();
        videoId = in.readInt();
        categoryId = in.readInt();
        avObject = in.readParcelable(AVObject.class.getClassLoader());
    }

    public static final Creator<VideoNewsEntity> CREATOR = new Creator<VideoNewsEntity>() {
        @Override
        public VideoNewsEntity createFromParcel(Parcel in) {
            return new VideoNewsEntity(in);
        }

        @Override
        public VideoNewsEntity[] newArray(int size) {
            return new VideoNewsEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(videoDuration);
        parcel.writeString(videoTitle);
        parcel.writeInt(playCount);
        parcel.writeInt(zanCount);
        parcel.writeInt(caiCount);
        parcel.writeString(videoUrl);
        parcel.writeString(thumbnail);
        parcel.writeInt(videoResolution);
        parcel.writeInt(videoId);
        parcel.writeInt(categoryId);
        parcel.writeParcelable(avObject, 0);
    }

    public static VideoNewsEntity parse(AVObject avObject) {
        if (avObject == null) {
            return null;
        }
        VideoNewsEntity videoNewsEntity = new VideoNewsEntity();
        videoNewsEntity.videoTitle = avObject.getString("videoTitle");
        videoNewsEntity.videoDuration = avObject.getInt("videoDuration");
        videoNewsEntity.playCount = avObject.getInt("playCount");
        videoNewsEntity.zanCount = avObject.getInt("zanCount");
        videoNewsEntity.caiCount = avObject.getInt("caiCount");
        if (avObject.getAVFile("videoUrl") != null) {
            videoNewsEntity.videoUrl = avObject.getAVFile("videoUrl").getUrl();
        }
        if (avObject.getAVFile("thumbnail") != null) {
            videoNewsEntity.thumbnail = avObject.getAVFile("thumbnail").getUrl();
        }
        videoNewsEntity.videoResolution = avObject.getInt("videoResolution");
        videoNewsEntity.videoId = avObject.getInt("videoId");
        videoNewsEntity.categoryId = avObject.getInt("categoryId");

        videoNewsEntity.avObject = avObject;

        return videoNewsEntity;
    }
}
