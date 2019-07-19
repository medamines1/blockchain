package com.example.med.blockchainproj.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import com.example.med.blockchainproj.DB.chainD;

/**
 * Created by med on 11/23/2018.
 */
@Dao
public interface ChainDao {

    @Insert
    void insertChain(chainD chain);

}
