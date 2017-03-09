package com.m3gv.news.common.sqlite;

import com.m3gv.news.common.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meikai on 17/3/9.
 */
public class Sql {

    private String sql;
    private List<String> params = new ArrayList<>();

    public Sql(String sql) {
        this.sql = sql;
    }

    public static Sql from(String sql, String... params) {
        Sql s = new Sql(sql);
        for (String obj : params) {
            s.addParam(obj);
        }
        return s;
    }

    @Override
    public Sql clone() {
        Sql other = new Sql(sql);
        other.params = new ArrayList<>(this.params);
        return other;
    }

    /**
     * 重置所有参数，从头开始
     */
    public Sql reset() {
        params.clear();
        return this;
    }

    public Sql count() {
        String s = String.valueOf(sql).toLowerCase();
        int index = s.indexOf(" from ");
        if (index != -1) {
            String temp = "select count(*) " + s.substring(index);
            Sql other = new Sql(temp);
            other.params = new ArrayList(this.params);
            return other;
        } else {
            throw new IllegalStateException("当前不是一条查询语句:" + s);
        }
    }

    public Sql addAllParams(List<String> params) {
        this.params.addAll(params);
        return this;
    }

    public Sql addParam(String obj) {
        params.add(obj);
        return this;
    }

    public String getRawSql() {
        return sql;
    }

    public String[] getParams() {
        return params.toArray(new String[params.size()]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        if (CollectionUtil.isNotEmpty(params)) {
            sb.append(" | ").append(params);
        }
        return sb.toString();
    }

}
