package com.m3gv.news.base;

/**
 * Created by meikai on 17/3/5.
 */

public class M3bBaseEntity {

    public EntityType entityType = EntityType.Normal;


    public enum EntityType {
        Normal,
        Banner
    }
}
