package com.m3gv.news.common.db;

import android.content.Context;

import com.m3gv.news.common.util.LogUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by meikai on 17/1/24.
 */
public class RealmDbHelper {

    private static RealmDbHelper instance = new RealmDbHelper();

    private Realm realm;

    public static RealmDbHelper getInstance() {
        return instance;
    }

    private RealmDbHelper() {

    }

    public void init(Context context) {
        Realm.init(context);
        //realm数据库文件保存中"Context.getFilesDir()"目录中，即：/data/data/<packagename>/files/m3gNews.realm
        //所以当在应用管理里面给当前app"清除数据"，realm数据库的数据会丢失
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .name("m3gNews.realm")
                .schemaVersion(1)
                .migration(new RealmUpdateMigration());
        Realm.setDefaultConfiguration(builder.build());

        realm = Realm.getDefaultInstance();
    }

    public boolean save(RealmModel realmModel) {
        try {
            realm.beginTransaction();
            realm.insert(realmModel);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            LogUtil.e("RealmDbHelper", "save:" + e.getMessage());
            return false;
        }
    }

    public boolean saveList(List<? extends RealmModel> realmObjectList) {
        try {
            realm.beginTransaction();
            realm.insert(realmObjectList);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            LogUtil.e("RealmDbHelper", "saveList:" + e.getMessage());
            return false;
        }
    }

    public RealmResults<? extends RealmModel> findAll(Class<? extends RealmModel> clazz) {
        try {
            return Realm.getDefaultInstance().where(clazz).findAll();
        } catch (Exception e) {
            LogUtil.e("RealmDbHelper", "findAll:" + e.getMessage());
            return null;
        }
    }

}
