package com.example.med.blockchainproj.DB.DBOBJ;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

import com.example.med.blockchainproj.DB.pool_elm;
import com.example.med.blockchainproj.Interfaces.poolInt;

import java.util.List;

import static com.example.med.blockchainproj.MainContole.DATABASE_NAME;

/**
 * Created by med on 11/19/2018.
 */

@Database(entities = {pool_elm.class}, version = 2, exportSchema = false)
public abstract class Pool_DB extends RoomDatabase {
    public static Pool_DB pool_db;

    public abstract poolInt daoAccess() ;

    public static void addPEWithId(Pool_DB pool_db,String tra) {
        pool_db.daoAccess().add(new pool_elm(tra));
    }

    public static void addP(Context context,String id_hash,String name , String tr,String timestamp) {
        getPool_db(context).daoAccess().add(new pool_elm(id_hash,name,tr,timestamp));
    }

    public static void addP(Context context,String id_hash,String name, String tr) {
        getPool_db(context).daoAccess().add(new pool_elm(id_hash,name,tr));
    }

    public static List<pool_elm> getAll(Context context) {

        Log.w("database  : ",(pool_db.toString())+ "");
        pool_db = getPool_db(context);
        return pool_db.daoAccess().getAllPool();
    }

    public static Pool_DB getPool_db(Context context) {
        if (pool_db==null)
             pool_db = Room.databaseBuilder(
                    context,
                    Pool_DB.class,
                    DATABASE_NAME)
                     .fallbackToDestructiveMigration()
                     .addMigrations(MIGRATION_1_2)
                    .build();
        return pool_db;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    public static void closedb(){
        if (pool_db!=null)
            pool_db.close();
    }
}


