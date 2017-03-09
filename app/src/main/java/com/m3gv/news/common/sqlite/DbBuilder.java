package com.m3gv.news.common.sqlite;


/**
 * Created by meikai on 17/3/9.
 */
public class DbBuilder {

    private String dbName;
    private String createSqlAssetName;
    private int dbVersion;
    private Db.DbUpgradeCallback callback;

    public DbBuilder setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public DbBuilder setCreateSqlAssetName(String createSqlAssetName) {
        this.createSqlAssetName = createSqlAssetName;
        return this;
    }

    public DbBuilder setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
        return this;
    }

    public DbBuilder setCallback(Db.DbUpgradeCallback callback) {
        this.callback = callback;
        return this;
    }

    public Db build() {
        Db db = new Db(dbName, createSqlAssetName, dbVersion, callback);
        return db;
    }
    
}
