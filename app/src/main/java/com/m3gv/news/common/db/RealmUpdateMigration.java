package com.m3gv.news.common.db;

import com.m3gv.news.common.util.LogUtil;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by meikai on 17/1/24.
 */

public class RealmUpdateMigration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        LogUtil.e("RealmUpdateMigration", "migrate, oldVersion=" + oldVersion + ",newVersion=" + newVersion);
    }

}
