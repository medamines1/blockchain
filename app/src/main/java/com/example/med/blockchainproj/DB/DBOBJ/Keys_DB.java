package com.example.med.blockchainproj.DB.DBOBJ;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

import com.example.med.blockchainproj.DB.mykeys;
import com.example.med.blockchainproj.Interfaces.keysST;
import com.example.med.blockchainproj.generater;

import java.util.List;

import static com.example.med.blockchainproj.MainContole.DATABASE_NAME;
import static com.example.med.blockchainproj.MainContole.LABEL_MAIN;

/**
 * Created by med on 11/19/2018.
 */

@Database(entities = {mykeys.class}, version = 3)
public abstract class Keys_DB extends RoomDatabase {
    public static Keys_DB keys_db;

    public abstract keysST daoAccess() ;

    public void save_keys(final Keys_DB keys_db, final String prik, final String pubk,final String label) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mykeys tmp = new mykeys(prik,pubk);
                tmp.setLabel(label);
                keys_db.daoAccess().insertSTK(tmp);
            }
        });
        t.start();
    }


    public  static  void CheckUserKeys(final Context context  ,final Keys_DB keys_db){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // generating our keys and save in them to db
                List<mykeys> a = keys_db.daoAccess().getSTKByLabel(LABEL_MAIN);
                Log.w("data ::",a + " :" +  a.isEmpty());
                if(a.isEmpty())
                    generater.operations_generate(context,keys_db);

            }
        });
        t.start();

    }


    public  static  String getMypub(final Context context){
                // generating our keys and save in them to db
                List<mykeys> a = keys_db.daoAccess().getSTKByLabel(LABEL_MAIN);
                if(a.isEmpty())
                    generater.operations_generate(context,keys_db);
                mykeys m = keys_db.daoAccess().getSTKByLabel(LABEL_MAIN).get(0);
        if (m.getPubk() !=null)
                return m.getPubk();
        return  null;
    }


    public  static  String getpriv(final Context context){
        // generating our keys and save in them to db
        List<mykeys> a = keys_db.daoAccess().getSTKByLabel(LABEL_MAIN);
        if(a.isEmpty())
            generater.operations_generate(context,keys_db);
        mykeys m = keys_db.daoAccess().getSTKByLabel(LABEL_MAIN).get(0);
        if (m.getPrik() !=null)
            return m.getPrik();
        return  null;
    }

    public static Keys_DB getKeys_db(Context context) {
        if (keys_db==null)
             keys_db = Room.databaseBuilder(context,
                    Keys_DB.class, DATABASE_NAME +"_keys")
                     .fallbackToDestructiveMigration()
                     .addMigrations(MIGRATION_1_2)
                    .build();
        return keys_db;
    }


    public static void closedb(){
        if (keys_db!=null)
            keys_db.close();
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };
}

