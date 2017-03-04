package com.m3gv.news.common.db;

import com.m3gv.news.common.util.LogUtil;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by meikai on 17/1/24.
 */

public class RealmUpdateMigration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        LogUtil.e("RealmUpdateMigration", "migrate, oldVersion=" + oldVersion + ",newVersion=" + newVersion);

        RealmSchema schema = realm.getSchema();
        if (oldVersion == 1) {
            initFirstVersion(schema);
        }

        if (newVersion == 2) {
            initVersion_2(schema);
        }
    }

    private void initVersion_2(RealmSchema schema) {
//        schema.get("User").addField("description", String.class);
    }

    private void initFirstVersion(RealmSchema schema) {

    }

}
