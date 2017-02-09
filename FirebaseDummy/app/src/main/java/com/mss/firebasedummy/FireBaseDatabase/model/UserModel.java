package com.mss.firebasedummy.FireBaseDatabase.model;

import java.io.Serializable;

/**
 * Created by deepakgupta on 9/2/17.
 */

public class UserModel implements Serializable {
    String name;
    String desc;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
