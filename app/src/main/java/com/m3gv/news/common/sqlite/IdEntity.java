package com.m3gv.news.common.sqlite;

import java.io.Serializable;

/**
 * Created by meikai on 17/3/9.
 */
public class IdEntity implements Serializable {

    private static final long serialVersionUID = 20140710L;
    protected Long _id;

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        this._id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IdEntity other = (IdEntity) obj;
        return isEquals(this._id, other._id);
    }

    @Override
    public int hashCode() {
        if (this._id != null) {
            int hash = 5;
            hash = 31 * hash + _id.hashCode();
            return hash;
        } else {
            return super.hashCode();
        }
    }

    public boolean isEquals(Object obj1, Object obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else if (obj2 != null) {
            return obj2.equals(obj1);
        } else {
            return true;
        }
    }

}
