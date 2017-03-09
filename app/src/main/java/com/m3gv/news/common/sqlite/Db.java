package com.m3gv.news.common.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.m3gv.news.base.M3Config;
import com.m3gv.news.common.util.AssetsUtil;
import com.m3gv.news.common.util.CollectionUtil;
import com.m3gv.news.common.util.IOUtil;
import com.m3gv.news.common.util.LogUtil;
import com.m3gv.news.common.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meikai on 17/3/9.
 */
public class Db {

    private M3DbHelper helper;
    private Map<Class<? extends IdEntity>, EntityDesc> cacheMap;
    private String dbName;
    private String createSqlAssetName;
    private int dbVersion;
    private DbUpgradeCallback callback;

    public Db(String dbName, String createSqlAssetName, int dbVersion, DbUpgradeCallback callback) {
        init(dbName, createSqlAssetName, dbVersion, callback, M3Config.getContext());
    }

    private void init(String dbName, String createSqlAssetName, int dbVersion, DbUpgradeCallback callback,
            Context context) {
        this.dbName = dbName;
        this.createSqlAssetName = createSqlAssetName;
        this.dbVersion = dbVersion;
        this.callback = callback;
        helper = new M3DbHelper(context);
        cacheMap = new HashMap<>();
    }

    public static boolean isValidId(IdEntity entity) {
        if (entity == null) {
            return false;
        }
        return entity.getId() != null && entity.getId() > 0L;
    }

    public synchronized <T extends IdEntity> void insertBatch(List<T> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            for (T entity : list) {
                EntityDesc<T> desc = getByEntity(entity);
                ContentValues cv = desc.toContentValues(entity);
                long id = db.insert(desc.getTableName(), null, cv);
                if (id != -1) {
                    entity.setId(id);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            endTransaction(db);
            closeDB(db);
        }

    }

    public synchronized <T extends IdEntity> long insert(T entity) {
        SQLiteDatabase db = null;
        long result = 0;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            EntityDesc<T> desc = getByEntity(entity);
            ContentValues cv = desc.toContentValues(entity);
            long id = db.insert(desc.getTableName(), null, cv);
            if (id != -1) {
                entity.setId(id);
            }
            result = id;
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            endTransaction(db);
            closeDB(db);
        }
        return result;
    }

    private void closeDB(SQLiteDatabase db) {
        try {
            if (db != null && db.isOpen()) {
                db.close();
            }
        } catch (Exception e) {
            LogUtil.d("", e.getMessage());
        }
    }

    public synchronized <T extends IdEntity> void insertOrUpdate(T entity) {
        if (!isValidId(entity)) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    public synchronized <T extends IdEntity> void update(T entity) {
        updateAndReturnAffectsNumber(entity);
    }

    public synchronized <T extends IdEntity> int updateAndReturnAffectsNumber(T entity) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            EntityDesc<T> desc = getByEntity(entity);
            ContentValues cv = desc.toContentValues(entity);
            int i = db.update(desc.getTableName(), cv, "_id=?", new String[]{String.valueOf(entity.getId())});
            db.setTransactionSuccessful();
            return i;
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            endTransaction(db);
            closeDB(db);
        }
        return -1;
    }

    public synchronized <T extends IdEntity> int update(Class<T> cls, ContentValues cv, long id) {
        EntityDesc<T> desc = getByClass(cls);
        return update(desc.getTableName(), cv, id);
    }

    public synchronized int update(String table, ContentValues cv, long id) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            int i = db.update(table, cv, "_id=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return i;
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            endTransaction(db);
            closeDB(db);
        }
        return -1;
    }

    /**
     * 计算给定表中的行数
     *
     * @param cls 用于计算表名 例如 logEventEntity 会对应的表名为： t_log_event
     */
    public synchronized <T extends IdEntity> long count(Class<T> cls) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        EntityDesc<T> desc = getByClass(cls);
        try {
            db = helper.getReadableDatabase();
            cursor = db.rawQuery("select count(*) from " + desc.getTableName(), null);
            cursor.moveToFirst();
            return cursor.getLong(0);
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            IOUtil.close(cursor);
            closeDB(db);
        }
        return -1;
    }

    public synchronized <T extends IdEntity> int update(Class<T> cls, ContentValues cv, String where, String[] args) {
        EntityDesc<T> desc = getByClass(cls);
        return update(desc.getTableName(), cv, where, args);
    }

    public synchronized int update(String table, ContentValues cv, String where, String[] args) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            int i = db.update(table, cv, where, args);
            db.setTransactionSuccessful();
            return i;
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            endTransaction(db);
            closeDB(db);
        }
        return -1;
    }

    public synchronized <T extends IdEntity> int deleteById(Class<T> cls, long id) {
        EntityDesc<T> desc = getByClass(cls);
        return deleteById(desc.getTableName(), id);
    }

    public synchronized int deleteById(String table, long id) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            int i = db.delete(table, "_id=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return i;
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            endTransaction(db);
            closeDB(db);
        }
        return -1;
    }

    public synchronized <T extends IdEntity> int deleteBySql(Class<T> cls, String where, String[] args) {
        EntityDesc<T> desc = getByClass(cls);
        return deleteBySql(desc.getTableName(), where, args);
    }

    public synchronized int deleteBySql(String table, String where, String[] args) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            int i = db.delete(table, where, args);
            db.setTransactionSuccessful();
            return i;
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            endTransaction(db);
            closeDB(db);
        }
        return -1;
    }

    public synchronized <T extends IdEntity> T findById(Class<T> cls, long id) {
        EntityDesc<T> desc = getByClass(cls);
        Sql sql = new Sql("select * from " + desc.getTableName() + " where _id=" + id);
        return findBySql(cls, sql);
    }

    public synchronized <T> T findBySql(RowMapper<T> mapper, Sql sql) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            cursor = db.rawQuery(sql.getRawSql(), sql.getParams());
            if (cursor.moveToNext()) {
                return mapper.mapper(cursor);
            }
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            IOUtil.close(cursor);
            closeDB(db);
        }
        return null;
    }

    public synchronized <T extends IdEntity> T findBySql(Class<T> cls, Sql sql) {
        EntityDesc<T> desc = getByClass(cls);
        return findBySql(desc, sql);
    }

    public synchronized <T> List<T> listBySql(RowMapper<T> mapper, Sql sql) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            List<T> list = new ArrayList<T>();
            db = helper.getReadableDatabase();
            cursor = db.rawQuery(sql.getRawSql(), sql.getParams());
            while (cursor.moveToNext()) {
                list.add(mapper.mapper(cursor));
            }
            return list;
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            IOUtil.close(cursor);
            closeDB(db);
        }
        return Collections.emptyList();
    }

    public synchronized <T extends IdEntity> List<T> listBySql(Class<T> cls, Sql sql) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        EntityDesc<T> desc = getByClass(cls);
        try {
            db = helper.getReadableDatabase();
            cursor = db.rawQuery(sql.getRawSql(), sql.getParams());
            return desc.toEntityList(cursor);
        } catch (Exception ex) {
            LogUtil.d("", ex);
        } finally {
            IOUtil.close(cursor);
            closeDB(db);
        }
        return Collections.emptyList();
    }

    private <T extends IdEntity> EntityDesc<T> getByEntity(T entity) {
        //为了配合android-studio的错误提示，所以加了多一层到Object的转换
        Class<T> cls = (Class<T>) ((Object) entity).getClass();
        EntityDesc<T> desc = cacheMap.get(cls);
        if (desc == null) {
            desc = new EntityDesc<T>(cls);
            cacheMap.put(cls, desc);
        }
        return desc;
    }

    private <T extends IdEntity> EntityDesc<T> getByClass(Class<T> cls) {
        EntityDesc<T> desc = cacheMap.get(cls);
        if (desc == null) {
            desc = new EntityDesc<T>(cls);
            cacheMap.put(cls, desc);
        }
        return desc;
    }

    /**
     * 数据库升级回调接口
     */
    public interface DbUpgradeCallback {

        void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }

    private static class EntityDesc<T extends IdEntity> implements RowMapper<T> {

        private static final String ID_NAME = "_id";
        private final Class<T> clazz;
        private String tableName;
        private List<Field> fieldList;

        public EntityDesc(Class<T> cls) {
            this.clazz = cls;
            initOther();
        }

        private void initOther() {
            fieldList = new ArrayList<Field>();
            Class<?> cls = clazz;
            while (cls != Object.class) {
                Field[] fs = cls.getDeclaredFields();
                for (Field f : fs) {

                    // 再看看是不是static or final or transient的变量，如果是，也不管
                    if (Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers()) || Modifier
                            .isTransient(f.getModifiers())) {
                        continue;
                    }
                    fieldList.add(f);
                    f.setAccessible(true);
                }
                cls = cls.getSuperclass();
            }
            // 初始化表名
            tableName = convertToTableName(clazz.getSimpleName());
        }

        private String convertToColumnName(String fieldName) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < fieldName.length(); i++) {
                char c = fieldName.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append("_");
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        private String convertToTableName(String className) {
            if (className.endsWith("Entity")) {
                className = className.substring(0, className.length() - 6);
            }
            return "t" + convertToColumnName(className);
        }

        public T toEntity(Cursor cursor) throws InstantiationException, IllegalAccessException {
            return toEntity(cursor, null);
        }

        private T toEntity(Cursor cursor, Map<String, Integer> cache)
                throws InstantiationException, IllegalAccessException {
            T t = clazz.newInstance();
            for (Field f : fieldList) {
                String fieldName = f.getName();
                Integer columnIndex;
                if (cache != null) {
                    columnIndex = cache.get(fieldName);
                    if (columnIndex == null) {
                        String columnName = convertToColumnName(fieldName);
                        columnIndex = cursor.getColumnIndex(columnName);
                        cache.put(fieldName, columnIndex);
                    }
                } else {
                    String columnName = convertToColumnName(fieldName);
                    columnIndex = cursor.getColumnIndex(columnName);
                }
                Class<?> type = f.getType();
                Object value = null;
                if (type == String.class) {
                    value = cursor.getString(columnIndex);
                } else if (type == int.class || type == Integer.class) {
                    value = cursor.getInt(columnIndex);
                } else if (type == long.class || type == Long.class) {
                    value = cursor.getLong(columnIndex);
                } else if (type == float.class || type == Float.class) {
                    value = cursor.getFloat(columnIndex);
                } else if (type == double.class || type == Double.class) {
                    value = cursor.getDouble(columnIndex);
                } else if (type == boolean.class || type == Boolean.class) {
                    //如果是布尔量，那么就以1为true,其他都为false
                    int i = cursor.getInt(columnIndex);
                    value = i == 1;
                } else if (type == Date.class) {
                    long theLong = cursor.getLong(columnIndex);
                    value = new Date(theLong);
                }
                if (value != null) {
                    f.set(t, value);
                }
            }
            return t;
        }

        @Override
        public T mapper(Cursor cursor) {
            try {
                return toEntity(cursor);
            } catch (Exception ex) {
                LogUtil.d("", ex);
            }
            return null;
        }

        public List<T> toEntityList(Cursor cursor) throws InstantiationException, IllegalAccessException {
            Map<String, Integer> columnIndexMap = new HashMap<>();
            List<T> list = new ArrayList<T>();
            while (cursor.moveToNext()) {
                T t = toEntity(cursor, columnIndexMap);
                if (t != null) {
                    list.add(t);
                }
            }
            return list;
        }

        public ContentValues toContentValues(IdEntity entity) throws IllegalArgumentException, IllegalAccessException {
            ContentValues cv = new ContentValues();
            for (Field f : fieldList) {
                String fieldName = f.getName();
                if (ID_NAME.equals(fieldName)) {
                    continue;
                }
                String columnName = convertToColumnName(fieldName);
                Object value = f.get(entity);
                Class<?> type = f.getType();
                if (type == String.class) {
                    cv.put(columnName, (String) value);
                } else if (type == int.class || type == Integer.class) {
                    cv.put(columnName, (Integer) value);
                } else if (type == long.class || type == Long.class) {
                    cv.put(columnName, (Long) value);
                } else if (type == float.class || type == Float.class) {
                    cv.put(columnName, (Float) value);
                } else if (type == double.class || type == Double.class) {
                    cv.put(columnName, (Double) value);
                } else if (type == boolean.class || type == Boolean.class) {
                    boolean b = Boolean.parseBoolean(String.valueOf(value));
                    cv.put(columnName, b ? 1 : 0);
                } else if (type == Date.class) {
                    Date date = (Date) value;
                    if (date != null) {
                        cv.put(columnName, date.getTime());
                    } else {
                        cv.put(columnName, 0L);
                    }
                } else {
                    throw new IllegalArgumentException("非法的实体类型,fieldName=" + fieldName + ",type=" + type);
                }
            }
            return cv;
        }

        public String getTableName() {
            return tableName;
        }
    }

    public static List<String> splitSql(String content) {
        List<String> result = new ArrayList<>();
        String[] ss = content.split(";");
        if (ss == null) {
            return result;
        }
        for (String sql : ss) {
            if (!StringUtil.isEmpty(sql)) {
                result.add(sql);
            }
        }
        return result;
    }

    private void endTransaction(SQLiteDatabase db) {
        try {
            if (db != null && db.isOpen()) {
                db.endTransaction();
            }
        } catch (Exception e) {
            LogUtil.d("", e);
        }
    }

    private class M3DbHelper extends SQLiteOpenHelper {

        private M3DbHelper(Context context) {
            super(context, dbName, null, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                String content = AssetsUtil.readAssetFileContent(createSqlAssetName);
                List<String> list = splitSql(content);
                if (CollectionUtil.isEmpty(list)) {
                    return;
                }
                for (String sql : list) {
                    db.execSQL(sql);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                LogUtil.d("", e);
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (callback != null) {
                callback.onUpgrade(db, oldVersion, newVersion);
            }
        }
    }

}
