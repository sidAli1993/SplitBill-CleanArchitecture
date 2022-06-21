package com.poc.paphoscafe.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.poc.paphoscafe.room.Dao.UserConfigDao;
import com.poc.paphoscafe.room.model.UserConfig;


@Database(entities = {UserConfig.class},
        version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase dbInstance;

    public abstract UserConfigDao userConfigDao();

    private static AppDatabase createInstance(Context context) {
        dbInstance = Room.databaseBuilder(context,
                AppDatabase.class,
                "paphos_cafe")
                .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
                .build();
        return dbInstance;
    }

    public static AppDatabase getDbInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = createInstance(context);
        }
        return dbInstance;
    }
}
