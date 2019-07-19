package com.example.med.blockchainproj.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.med.blockchainproj.DB.BlockD;

import java.util.List;

/**
 * Created by med on 11/23/2018.
 */

@Dao
public interface BlockDao {
    @Insert
    void insert(BlockD blockD);

    @Update
    void update(BlockD blockD);

    @Delete
    void delete(BlockD blockD);

    @Query("SELECT * FROM BlockD where chain_name=:name ")
    List<BlockD> getAllBlocks(String name);

    @Query("SELECT * FROM BlockD WHERE trans_id=:trans_id")
    List<BlockD> findBlockByTran(final long trans_id);

    @Query("SELECT * FROM BlockD WHERE chain_name=:name order by trans_id desc  LIMIT 1 ")
    BlockD getLast(String name);



    @Query("select timestamp from BlockD where trans_id=:trans_id")
    long getTimestamp(long trans_id);

    @Query("SELECT sum(1) FROM BlockD WHERE chain_name=:name")
    Long LenBlocks(String name);

    @Query("SELECT* FROM BlockD WHERE chain_name=:name and trans_id=:trans_id")
    BlockD geBlockWithID(long trans_id,String name);
}
