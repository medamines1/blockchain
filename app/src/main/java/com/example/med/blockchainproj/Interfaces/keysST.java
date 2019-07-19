package com.example.med.blockchainproj.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.med.blockchainproj.DB.mykeys;

import java.util.List;

/**
 * Created by med on 10/19/2018.
 */
@Dao
public interface keysST {
    @Insert
    void insertSTK(mykeys mk);

    @Insert
    void insertMultipleSTK(List<mykeys> mk);

    @Query("select * from mykeys")
    List<mykeys> getAllSTK();

    @Query("select * from mykeys where prik=:prik")
    mykeys getSTKByPRIK(String prik);

    @Query("select * from mykeys where label=:label")
    List<mykeys> getSTKByLabel(String label);


}
