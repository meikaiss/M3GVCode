package com.m3gv.news.business.article;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVObject;

/**
 * Created by meikai on 16/12/18.
 */

public class ArticleNewsEntity implements Parcelable {

    public long articleId;
    public String articleTitle;
    public String content;
    public String thumbnail;
    public int playCount;
    public int zanCount;
    public int caiCount;

    public ArticleNewsEntity(){

    }

    protected ArticleNewsEntity(Parcel in) {
        articleId = in.readLong();
        articleTitle = in.readString();
        content = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<ArticleNewsEntity> CREATOR = new Creator<ArticleNewsEntity>() {
        @Override
        public ArticleNewsEntity createFromParcel(Parcel in) {
            return new ArticleNewsEntity(in);
        }

        @Override
        public ArticleNewsEntity[] newArray(int size) {
            return new ArticleNewsEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(articleId);
        parcel.writeString(articleTitle);
        parcel.writeString(content);
        parcel.writeString(thumbnail);
        parcel.writeInt(playCount);
        parcel.writeInt(zanCount);
        parcel.writeInt(caiCount);
    }

    public static ArticleNewsEntity parse(AVObject avObject) {
        if (avObject == null) {
            return null;
        }
        ArticleNewsEntity articleNewsEntity = new ArticleNewsEntity();
        articleNewsEntity.articleTitle = avObject.getString("articleTitle");
        articleNewsEntity.articleId = avObject.getInt("articleId");
        articleNewsEntity.content = avObject.getString("content");
        articleNewsEntity.playCount = avObject.getInt("playCount");
        articleNewsEntity.zanCount = avObject.getInt("zanCount");
        articleNewsEntity.caiCount = avObject.getInt("caiCount");
        if (avObject.getAVFile("thumbnail") != null) {
            articleNewsEntity.thumbnail = avObject.getAVFile("thumbnail").getUrl();
        }

        return articleNewsEntity;
    }
}
