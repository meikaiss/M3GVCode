package com.m3gv.news.business.banner;

import com.avos.avoscloud.AVObject;
import com.m3gv.news.base.M3bBaseEntity;
import com.m3gv.news.common.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meikai on 17/3/5.
 */

public class BannerEntity extends M3bBaseEntity {

    public List<Integer> newsId = new ArrayList<>();
    public List<String> channelName = new ArrayList<>();
    public List<String> thumbnailUrl = new ArrayList<>();
    public List<String> title = new ArrayList<>();

    public static BannerEntity parse(List<AVObject> list) {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }

        BannerEntity bannerEntity = new BannerEntity();
        for (int i = 0; i < list.size(); i++) {
            bannerEntity.newsId.add(list.get(i).getInt("newsId"));
            bannerEntity.channelName.add(list.get(i).getString("ChannelName"));
            bannerEntity.title.add(list.get(i).getString("bannerTitle"));
            bannerEntity.thumbnailUrl.add(list.get(i).getAVFile("thumbnail").getUrl());
        }

        bannerEntity.entityType = EntityType.Banner;

        return bannerEntity;
    }

}
