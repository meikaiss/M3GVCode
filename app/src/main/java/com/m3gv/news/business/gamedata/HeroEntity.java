package com.m3gv.news.business.gamedata;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVObject;
import com.m3gv.news.business.youmiAd.ItemType;
import com.m3gv.news.common.sqlite.IdEntity;

/**
 * Created by meikai on 17/1/12.
 */
public class HeroEntity extends IdEntity implements Parcelable {

    public long heroId;
    public String heroName;
    public String UNationType;
    public String heroIcon;
    public String Fdifficulty;
    public String Fbattle;
    public String Fposition;
    public String Type1;
    public String Type2;
    public Integer Difficulty;
    public Integer Survial;
    public Integer Physics;
    public Integer Technology;

    public String heroDesc;
    public String attack;
    public String armor;
    public String speed;
    public String strength;
    public String agility;
    public String intelligence;

    public String skill1Name;
    public String skill1Icon;
    public String skill1Scope;
    public String skill1Effect;
    public String skill1Magic;
    public String skill1CD;
    public String skill1Level1Effect;
    public String skill1Level2Effect;
    public String skill1Level3Effect;
    public String skill1Level4Effect;

    public String skill2Name;
    public String skill2Icon;
    public String skill2Scope;
    public String skill2Effect;
    public String skill2Magic;
    public String skill2CD;
    public String skill2Level1Effect;
    public String skill2Level2Effect;
    public String skill2Level3Effect;
    public String skill2Level4Effect;

    public String skill3Name;
    public String skill3Icon;
    public String skill3Scope;
    public String skill3Effect;
    public String skill3Magic;
    public String skill3CD;
    public String skill3Level1Effect;
    public String skill3Level2Effect;
    public String skill3Level3Effect;
    public String skill3Level4Effect;

    public String skill4Name;
    public String skill4Icon;
    public String skill4Scope;
    public String skill4Effect;
    public String skill4Magic;
    public String skill4CD;
    public String skill4Level1Effect;
    public String skill4Level2Effect;
    public String skill4Level3Effect;
    public String skill4Level4Effect;

    public transient ItemType itemType = ItemType.REAL_DATA;

    public transient String[] skillName;
    public transient String[] skillIcon;
    public transient String[] skillScope;
    public transient String[] skillEffect;
    public transient String[] skillMagic;
    public transient String[] skillCD;
    public transient String[] skillLevel1Effect;
    public transient String[] skillLevel2Effect;
    public transient String[] skillLevel3Effect;
    public transient String[] skillLevel4Effect;

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
        heroDesc = in.readString();
        attack = in.readString();
        armor = in.readString();
        speed = in.readString();
        strength = in.readString();
        agility = in.readString();
        intelligence = in.readString();
        skill1Name = in.readString();
        skill1Icon = in.readString();
        skill1Scope = in.readString();
        skill1Effect = in.readString();
        skill1Magic = in.readString();
        skill1CD = in.readString();
        skill1Level1Effect = in.readString();
        skill1Level2Effect = in.readString();
        skill1Level3Effect = in.readString();
        skill1Level4Effect = in.readString();
        skill2Name = in.readString();
        skill2Icon = in.readString();
        skill2Scope = in.readString();
        skill2Effect = in.readString();
        skill2Magic = in.readString();
        skill2CD = in.readString();
        skill2Level1Effect = in.readString();
        skill2Level2Effect = in.readString();
        skill2Level3Effect = in.readString();
        skill2Level4Effect = in.readString();
        skill3Name = in.readString();
        skill3Icon = in.readString();
        skill3Scope = in.readString();
        skill3Effect = in.readString();
        skill3Magic = in.readString();
        skill3CD = in.readString();
        skill3Level1Effect = in.readString();
        skill3Level2Effect = in.readString();
        skill3Level3Effect = in.readString();
        skill3Level4Effect = in.readString();
        skill4Name = in.readString();
        skill4Icon = in.readString();
        skill4Scope = in.readString();
        skill4Effect = in.readString();
        skill4Magic = in.readString();
        skill4CD = in.readString();
        skill4Level1Effect = in.readString();
        skill4Level2Effect = in.readString();
        skill4Level3Effect = in.readString();
        skill4Level4Effect = in.readString();

        this.assembleInnerData();
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
        dest.writeString(heroDesc);
        dest.writeString(attack);
        dest.writeString(armor);
        dest.writeString(speed);
        dest.writeString(strength);
        dest.writeString(agility);
        dest.writeString(intelligence);
        dest.writeString(skill1Name);
        dest.writeString(skill1Icon);
        dest.writeString(skill1Scope);
        dest.writeString(skill1Effect);
        dest.writeString(skill1Magic);
        dest.writeString(skill1CD);
        dest.writeString(skill1Level1Effect);
        dest.writeString(skill1Level2Effect);
        dest.writeString(skill1Level3Effect);
        dest.writeString(skill1Level4Effect);
        dest.writeString(skill2Name);
        dest.writeString(skill2Icon);
        dest.writeString(skill2Scope);
        dest.writeString(skill2Effect);
        dest.writeString(skill2Magic);
        dest.writeString(skill2CD);
        dest.writeString(skill2Level1Effect);
        dest.writeString(skill2Level2Effect);
        dest.writeString(skill2Level3Effect);
        dest.writeString(skill2Level4Effect);
        dest.writeString(skill3Name);
        dest.writeString(skill3Icon);
        dest.writeString(skill3Scope);
        dest.writeString(skill3Effect);
        dest.writeString(skill3Magic);
        dest.writeString(skill3CD);
        dest.writeString(skill3Level1Effect);
        dest.writeString(skill3Level2Effect);
        dest.writeString(skill3Level3Effect);
        dest.writeString(skill3Level4Effect);
        dest.writeString(skill4Name);
        dest.writeString(skill4Icon);
        dest.writeString(skill4Scope);
        dest.writeString(skill4Effect);
        dest.writeString(skill4Magic);
        dest.writeString(skill4CD);
        dest.writeString(skill4Level1Effect);
        dest.writeString(skill4Level2Effect);
        dest.writeString(skill4Level3Effect);
        dest.writeString(skill4Level4Effect);
    }

    @Override
    public int describeContents() {
        return 0;
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

        heroEntity.heroDesc = avObject.getString("heroDesc");

        heroEntity.skill1Name = avObject.getString("skill1Name");
        if (avObject.getAVFile("skill1Icon") != null) {
            heroEntity.skill1Icon = avObject.getAVFile("skill1Icon").getUrl();
        }
        heroEntity.skill1Scope = avObject.getString("skill1Scope");
        heroEntity.skill1Effect = avObject.getString("skill1Effect");
        heroEntity.skill1Magic = avObject.getString("skill1Magic");
        heroEntity.skill1CD = avObject.getString("skill1CD");
        heroEntity.skill1Level1Effect = avObject.getString("skill1Level1Effect");
        heroEntity.skill1Level2Effect = avObject.getString("skill1Level2Effect");
        heroEntity.skill1Level3Effect = avObject.getString("skill1Level3Effect");
        heroEntity.skill1Level4Effect = avObject.getString("skill1Level4Effect");

        heroEntity.skill2Name = avObject.getString("skill2Name");
        if (avObject.getAVFile("skill2Icon") != null) {
            heroEntity.skill2Icon = avObject.getAVFile("skill2Icon").getUrl();
        }
        heroEntity.skill2Scope = avObject.getString("skill2Scope");
        heroEntity.skill2Effect = avObject.getString("skill2Effect");
        heroEntity.skill2Magic = avObject.getString("skill2Magic");
        heroEntity.skill2CD = avObject.getString("skill2CD");
        heroEntity.skill2Level1Effect = avObject.getString("skill2Level1Effect");
        heroEntity.skill2Level2Effect = avObject.getString("skill2Level2Effect");
        heroEntity.skill2Level3Effect = avObject.getString("skill2Level3Effect");
        heroEntity.skill2Level4Effect = avObject.getString("skill2Level4Effect");

        heroEntity.skill3Name = avObject.getString("skill3Name");
        if (avObject.getAVFile("skill3Icon") != null) {
            heroEntity.skill3Icon = avObject.getAVFile("skill3Icon").getUrl();
        }
        heroEntity.skill3Scope = avObject.getString("skill3Scope");
        heroEntity.skill3Effect = avObject.getString("skill3Effect");
        heroEntity.skill3Magic = avObject.getString("skill3Magic");
        heroEntity.skill3CD = avObject.getString("skill3CD");
        heroEntity.skill3Level1Effect = avObject.getString("skill3Level1Effect");
        heroEntity.skill3Level2Effect = avObject.getString("skill3Level2Effect");
        heroEntity.skill3Level3Effect = avObject.getString("skill3Level3Effect");
        heroEntity.skill3Level4Effect = avObject.getString("skill3Level4Effect");

        heroEntity.skill4Name = avObject.getString("skill4Name");
        if (avObject.getAVFile("skill4Icon") != null) {
            heroEntity.skill4Icon = avObject.getAVFile("skill4Icon").getUrl();
        }
        heroEntity.skill4Scope = avObject.getString("skill4Scope");
        heroEntity.skill4Effect = avObject.getString("skill4Effect");
        heroEntity.skill4Magic = avObject.getString("skill4Magic");
        heroEntity.skill4CD = avObject.getString("skill4CD");
        heroEntity.skill4Level1Effect = avObject.getString("skill4Level1Effect");
        heroEntity.skill4Level2Effect = avObject.getString("skill4Level2Effect");
        heroEntity.skill4Level3Effect = avObject.getString("skill4Level3Effect");
        heroEntity.skill4Level4Effect = avObject.getString("skill4Level4Effect");

        heroEntity.assembleInnerData();
        return heroEntity;
    }

    private void assembleInnerData() {
        skillName = new String[]{skill1Name, skill2Name, skill3Name, skill4Name};
        skillIcon = new String[]{skill1Icon, skill2Icon, skill3Icon, skill4Icon};
        skillScope = new String[]{skill1Scope, skill2Scope, skill3Scope, skill4Scope};
        skillEffect = new String[]{skill1Effect, skill2Effect, skill3Effect, skill4Effect};
        skillMagic = new String[]{skill1Magic, skill2Magic, skill3Magic, skill4Magic};
        skillCD = new String[]{skill1CD, skill2CD, skill3CD, skill4CD};
        skillLevel1Effect = new String[]{skill1Level1Effect, skill2Level1Effect, skill3Level1Effect,
                skill4Level1Effect};
        skillLevel2Effect = new String[]{skill1Level2Effect, skill2Level2Effect, skill3Level2Effect,
                skill4Level2Effect};
        skillLevel3Effect = new String[]{skill1Level3Effect, skill2Level3Effect, skill3Level3Effect,
                skill4Level3Effect};
        skillLevel4Effect = new String[]{skill1Level4Effect, skill2Level4Effect, skill3Level4Effect,
                skill4Level4Effect};
    }

}
