package com.m3gv.news.business.homepage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by meikai on 16/12/26.
 */
public class CategoryEntity implements Parcelable {

    public String name;
    public int categoryId;

    public CategoryEntity(String name, int categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }

    protected CategoryEntity(Parcel in) {
        name = in.readString();
        categoryId = in.readInt();
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
        dest.writeString(name);
        dest.writeInt(categoryId);
    }

}
