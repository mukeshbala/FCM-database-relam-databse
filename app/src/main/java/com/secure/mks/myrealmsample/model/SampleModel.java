package com.secure.mks.myrealmsample.model;

import io.realm.RealmObject;

public class SampleModel extends RealmObject {

    private String name;
    private String  phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
