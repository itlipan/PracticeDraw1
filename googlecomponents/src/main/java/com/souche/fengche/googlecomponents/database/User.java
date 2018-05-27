package com.souche.fengche.googlecomponents.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {

    /**
     * 对于非基础类型需要注解 @NonNull
     */
    @PrimaryKey
    @NonNull
    public final String id;

    public final String name;
    public final int age;

    public User(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
