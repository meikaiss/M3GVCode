package com.m3gv.news.business.homepage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by meikai on 16/12/26.
 */
public class CategoryEntity implements Parcelable {

    public String categoryName;
    public String tableName;

    public CategoryEntity(String categoryName, String tableName) {
        this.categoryName = categoryName;
        this.tableName = tableName;
    }

    protected CategoryEntity(Parcel in) {
        categoryName = in.readString();
        tableName = in.readString();
    }

    public static final Creator<CategoryEntity> CREATOR = new Creator<CategoryEntity>() {
        @Override
        public CategoryEntity createFromParcel(Parcel in) {
            return new CategoryEntity(in);
        }

        @Override
        public CategoryEntity[] newArray(int size) {
            return new CategoryEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryName);
        dest.writeString(tableName);
    }

}
