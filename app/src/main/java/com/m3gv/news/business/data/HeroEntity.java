package com.m3gv.news.business.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVObject;

/**
 * Created by meikai on 17/1/12.
 */

public class HeroEntity implements Parcelable {

    public long heroId;
    public String heroName;
    public String UNationType;
    public String heroIcon;
    public String Fdifficulty;
    public String Fbattle;
    public String Fposition;
    public String Type1;
    public String Type2;
    public int Difficulty;
    public int Survial;
    public int Physics;
    public int Technology;

    public String attack;
    public String armor;
    public String speed;
    public String strength;
    public String agility;
    public String intelligence;

    public String desc;


    public static HeroEntity parse(AVObject avObject) {
        if (avObject == null) {
            return null;
        }
        HeroEntity heroEntity = new HeroEntity();
        heroEntity.heroId = avObject.getLong("heroId");
        heroEntity.heroName = avObject.getString("heroName");
        heroEntity.UNationType = avObject.getString("country");
        heroEntity.Fdifficulty = avObject.getString("Fdifficulty");
        heroEntity.Fbattle = avObject.getString("Fbattle");
        heroEntity.Fposition = avObject.getString("Fposition");
        heroEntity.Type1 = avObject.getString("Type1");
        heroEntity.Type2 = avObject.getString("Type2");
        heroEntity.Difficulty = avObject.getInt("Difficulty");
        heroEntity.Survial = avObject.getInt("Survial");
        heroEntity.Physics = avObject.getInt("Physics");
        heroEntity.Technology = avObject.getInt("Technology");
        heroEntity.attack = avObject.getString("attack");
        heroEntity.armor = avObject.getString("armor");
        heroEntity.speed = avObject.getString("speed");
        heroEntity.strength = avObject.getString("strength");
        heroEntity.agility = avObject.getString("agility");
        heroEntity.intelligence = avObject.getString("intelligence");

        if (avObject.getAVFile("heroIcon") != null) {
            heroEntity.heroIcon = avObject.getAVFile("heroIcon").getUrl();
        }

        heroEntity.desc = avObject.getString("heroDesc");

        return heroEntity;
    }

    public HeroEntity() {
    }

    protected HeroEntity(Parcel in) {
        heroId = in.readLong();
        heroName = in.readString();
        UNationType = in.readString();
        heroIcon = in.readString();
        Fdifficulty = in.readString();
        Fbattle = in.readString();
        Fposition = in.readString();
        Type1 = in.readString();
        Type2 = in.readString();
        Difficulty = in.readInt();
        Survial = in.readInt();
        Physics = in.readInt();
        Technology = in.readInt();
        attack = in.readString();
        armor = in.readString();
        speed = in.readString();
        strength = in.readString();
        agility = in.readString();
        intelligence = in.readString();

        desc = in.readString();
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
        dest.writeString(UNationType);
        dest.writeString(heroIcon);
        dest.writeString(Fdifficulty);
        dest.writeString(Fbattle);
        dest.writeString(Fposition);
        dest.writeString(Type1);
        dest.writeString(Type2);
        dest.writeInt(Difficulty);
        dest.writeInt(Survial);
        dest.writeInt(Physics);
        dest.writeInt(Technology);
        dest.writeString(attack);
        dest.writeString(armor);
        dest.writeString(speed);
        dest.writeString(strength);
        dest.writeString(agility);
        dest.writeString(intelligence);

        dest.writeString(desc);
    }
}
