package com.m3gv.news.business.zanCai;

import android.os.Parcel;
import android.os.Parcelable;

import com.m3gv.news.common.sqlite.IdEntity;

/**
 * Created by meikai on 17/3/4.
 */
public class ZanCaiRecordEntity extends IdEntity implements Parcelable, Cloneable {

    public String newsType;
    public String newsId;
    public int hasZan;
    public int hasCai;

    public ZanCaiRecordEntity() {
    }

    protected ZanCaiRecordEntity(Parcel in) {
        newsType = in.readString();
        newsId = in.readString();
        hasZan = in.readInt();
        hasCai = in.readInt();
    }

    public static final Creator<ZanCaiRecordEntity> CREATOR = new Creator<ZanCaiRecordEntity>() {
        @Override
        public ZanCaiRecordEntity createFromParcel(Parcel in) {
            return new ZanCaiRecordEntity(in);
        }

        @Override
        public ZanCaiRecordEntity[] newArray(int size) {
            return new ZanCaiRecordEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(newsType);
        dest.writeString(newsId);
        dest.writeInt(hasZan);
        dest.writeInt(hasCai);
    }
}
