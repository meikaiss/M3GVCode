package com.m3gv.news.business.db;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.m3gv.news.common.sqlite.Db;
import com.m3gv.news.common.sqlite.DbBuilder;
import com.m3gv.news.common.sqlite.IdEntity;
import com.m3gv.news.common.sqlite.Sql;
import com.m3gv.news.common.util.AssetsUtil;
import com.m3gv.news.common.util.LogUtil;
import com.m3gv.news.common.util.StringUtil;

import java.io.File;
import java.util.List;

/**
 * Created by meikai on 17/3/9.
 */
public class M3DB implements Db.DbUpgradeCallback {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "m3g.db";
    private static M3DB instance;

    private Db db;

    public synchronized static M3DB getInstance() {
        if (instance == null) {
            instance = new M3DB();
        }
        return instance;
    }

    public void init() {
        DbBuilder dbBuilder = new DbBuilder();
        dbBuilder.setDbName(DB_NAME);
        dbBuilder.setDbVersion(DB_VERSION);
        dbBuilder.setCreateSqlAssetName("db" + File.separator + "m3g.sql");
        dbBuilder.setCallback(this);

        db = dbBuilder.build();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.beginTransaction();
            for (int i = oldVersion + 1; i <= newVersion; i++) {
                String assetSqlName = "db/upgrade/m3g_upgrade_" + i + ".sql";
                String content = AssetsUtil.readAssetFileContent(assetSqlName);
                if (StringUtil.isEmpty(content)) {
                    continue;
                }
                String[] ss = content.split(";");
                for (String sql : ss) {
                    sql = sql.trim();
                    if (TextUtils.isEmpty(sql)) {
                        continue;
                    }
                    try {
                        db.execSQL(sql);
                    } catch (Exception e) {
                    }
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            db.endTransaction();
        }
    }

    public boolean insert(IdEntity entity) {
        return db.insert(entity) != -1;
    }

    public <T extends IdEntity> T select(String sql, Class<T> cls) {
        return db.findBySql(cls, Sql.from(sql));
    }

    public <T extends IdEntity> List<T> selectList(String sql, Class<T> cls) {
        return db.listBySql(cls, Sql.from(sql));
    }

}
