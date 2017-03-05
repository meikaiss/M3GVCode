package com.m3gv.news.business.banner;

import com.avos.avoscloud.AVObject;
import com.m3gv.news.base.M3bBaseEntity;

/**
 * Created by meikai on 17/3/5.
 */

public class BannerEntity extends M3bBaseEntity {

    public int newsId;
    public String channelName;
    public String thumbnailUrl;
    public String title;


    public static BannerEntity parse(AVObject avObject) {
        if (avObject == null) {
            return null;
        }
        BannerEntity bannerEntity = new BannerEntity();
        bannerEntity.newsId = avObject.getInt("newsId");
        bannerEntity.channelName = avObject.getString("channelName");
        bannerEntity.title = avObject.getString("title");
        if (avObject.getAVFile("thumbnail") != null) {
            bannerEntity.thumbnailUrl = avObject.getAVFile("thumbnail").getUrl();
        }

        bannerEntity.entityType = EntityType.Banner;

        return bannerEntity;
    }

}
