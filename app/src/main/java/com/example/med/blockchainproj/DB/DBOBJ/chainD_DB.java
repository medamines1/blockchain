package com.example.med.blockchainproj.DB.DBOBJ;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.med.blockchainproj.DB.chainD;
import com.example.med.blockchainproj.Interfaces.ChainDao;

import static com.example.med.blockchainproj.MainContole.DATABASE_NAME;
import static com.example.med.blockchainproj.MainContole.DATABASE_VERSIOM;

/**
 * Created by med on 11/23/2018.
 */

@Database(entities = {chainD.class}, version = DATABASE_VERSIOM)
public abstract class chainD_DB extends RoomDatabase {
    public static chainD_DB chain_db;

    public abstract ChainDao daoAccess() ;

    public static chainD_DB getchain_db(Context context) {
        if (chain_db == null)
            chain_db = Room.databaseBuilder(context,
                    chainD_DB.class, DATABASE_NAME + "_chain")
                    .fallbackToDestructiveMigration()
                    .build();
        return chain_db;
    }
}