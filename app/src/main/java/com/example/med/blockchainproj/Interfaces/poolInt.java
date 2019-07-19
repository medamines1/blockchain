package com.example.med.blockchainproj.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.med.blockchainproj.DB.pool_elm;

import java.util.List;

/**
 * Created by med on 11/19/2018.
 */

@Dao
public interface poolInt {
    @Insert
    void add(pool_elm elm);

    @Delete
    void delete(pool_elm e);

    @Query("select * from pool_elm where id_hash=:id and name=:name ")
    pool_elm getById(String id,String name);

    @Query("select * from pool_elm")
    List<pool_elm> getAllPool();

    @Query("delete from pool_elm where id_hash=:id_hash")
    void deleteByHashid(String id_hash);
}
