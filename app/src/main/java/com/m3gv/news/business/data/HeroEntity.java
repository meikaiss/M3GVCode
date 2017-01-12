package com.m3gv.news.business.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVObject;
import com.m3gv.news.business.article.ArticleNewsEntity;

/**
 * Created by meikai on 17/1/12.
 */

public class HeroEntity implements Parcelable {

    public long heroId;
    public String heroName;
    public String heroIcon;
    public String heroDesc;
    public String country;

    public HeroEntity() {
    }

    protected HeroEntity(Parcel in) {
        heroId = in.readLong();
        heroName = in.readString();
        heroIcon = in.readString();
        heroDesc = in.readString();
        country = in.readString();
    }

    public static final Creator<HeroEntity> CREATOR = new Creator<HeroEntity>() {
        @Override
        public HeroEntity createFromParcel(Parcel in) {
            return new HeroEntity(in);
        }

        @Override
        public HeroEntity[] newArray(int size) {
            return new HeroEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(heroId);
        dest.writeString(heroName);
        dest.writeString(heroIcon);
        dest.writeString(heroDesc);
        dest.writeString(country);
    }

    public static HeroEntity parse(AVObject avObject) {
        if (avObject == null) {
            return null;
        }
        HeroEntity heroEntity = new HeroEntity();
        heroEntity.heroId = avObject.getLong("heroId");
        heroEntity.heroName = avObject.getString("heroName");
        heroEntity.heroDesc = avObject.getString("heroDesc");
        if (avObject.getAVFile("heroIcon") != null) {
            heroEntity.heroIcon = avObject.getAVFile("heroIcon").getUrl();
        }

        return heroEntity;
    }
}
