package com.poc.paphoscafe.room.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.poc.paphoscafe.room.model.UserConfig;

@Dao
public interface UserConfigDao {

    @Insert
    void insert(UserConfig userConfig);


    @Update
    void update(UserConfig userConfig);


    @Delete
    void delete(UserConfig userConfig);

    @Query("select pushy_token from user_config limit 1")
    String getUserToken();

}
