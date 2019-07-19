package com.example.med.blockchainproj.DB.DBOBJ;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.med.blockchainproj.DB.BlockD;
import com.example.med.blockchainproj.Interfaces.BlockDao;


import static com.example.med.blockchainproj.MainContole.DATABASE_NAME;

/**
 * Created by med on 11/19/2018.
 */

@Database(entities = {BlockD.class}, version = 1)
public abstract class BlockD_DB extends RoomDatabase {
    public static BlockD_DB block_db;

    public abstract BlockDao daoAccess() ;

    public static BlockD_DB getBlock_db(Context context) {
        if (block_db==null)
            block_db = Room.databaseBuilder(context,
                    BlockD_DB.class, DATABASE_NAME +"_block")
                     .fallbackToDestructiveMigration()
                    .build();
        return block_db;
    }


    public static long save_and_get_id(final Context context, final BlockD b) {
                BlockD_DB blockD_db = getBlock_db(context);
                blockD_db.daoAccess().insert(b);

        return  block_db.daoAccess().getLast(b.getChain_name()).getTrans_id();
    }


}

