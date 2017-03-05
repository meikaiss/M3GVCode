package com.m3gv.news.business.cartoon;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVObject;

/**
 * Created by meikai on 16/12/18.
 */
public class CartoonEntity implements Parcelable {

    public long cartoonId;
    public String cartoonTitle;
    public String source;
    public String content;
    public String thumbnail;
    public int readCount;
    public int zanCount;
    public int caiCount;

    public CartoonEntity() {

    }

    protected CartoonEntity(Parcel in) {
        cartoonId = in.readLong();
        cartoonTitle = in.readString();
        source = in.readString();
        content = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<CartoonEntity> CREATOR = new Creator<CartoonEntity>() {
        @Override
        public CartoonEntity createFromParcel(Parcel in) {
            return new CartoonEntity(in);
        }

        @Override
        public CartoonEntity[] newArray(int size) {
            return new CartoonEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(cartoonId);
        parcel.writeString(cartoonTitle);
        parcel.writeString(source);
        parcel.writeString(content);
        parcel.writeString(thumbnail);
        parcel.writeInt(readCount);
        parcel.writeInt(zanCount);
        parcel.writeInt(caiCount);
    }

    public static CartoonEntity parse(AVObject avObject) {
        if (avObject == null) {
            return null;
        }
        CartoonEntity articleNewsEntity = new CartoonEntity();
        articleNewsEntity.cartoonTitle = avObject.getString("cartoonTitle");
        articleNewsEntity.source = avObject.getString("source");
        articleNewsEntity.cartoonId = avObject.getInt("cartoonId");
        articleNewsEntity.content = avObject.getString("content");
        articleNewsEntity.readCount = avObject.getInt("readCount");
        articleNewsEntity.zanCount = avObject.getInt("zanCount");
        articleNewsEntity.caiCount = avObject.getInt("caiCount");
        if (avObject.getAVFile("thumb") != null) {
            articleNewsEntity.thumbnail = avObject.getAVFile("thumb").getUrl();
        }

        return articleNewsEntity;
    }
}
