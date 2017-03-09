package com.m3gv.news.common.sqlite;

import android.database.Cursor;

/**
 * Created by meikai on 17/3/9.
 */
public interface RowMapper<T> {
    T mapper(Cursor cursor);
}
