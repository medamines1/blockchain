package com.example.med.blockchainproj.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.med.blockchainproj.DB.node;

import java.util.List;

/**
 * Created by med on 10/19/2018.
 */
@Dao
public interface nodeInt {
    @Insert
    void insertNode(node mynode);

    @Insert
    void insertMultipleNodes(List<node> mynodes);

    @Query("select * from node")
    List<node> getAllNodes();

    @Query("select * from node where ip=:ip")
    node getNodeByIp(String ip);

    @Delete
    void deleteNode(node myNode);

    @Query("select sum(1) from node ")
    long LenNodes();
}
