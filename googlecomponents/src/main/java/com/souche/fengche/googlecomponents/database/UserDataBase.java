package com.souche.fengche.googlecomponents.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class},version = 1,exportSchema = false)
public abstract class UserDataBase extends RoomDatabase{

    private static final String USER_DB_NAME = "userDb.db";

    private static volatile  UserDataBase instance ;

    public synchronized static UserDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static UserDataBase create(Context context) {
        return Room.databaseBuilder(context,UserDataBase.class,USER_DB_NAME).build();
    }

    public abstract UserDao getUserDao();
}
