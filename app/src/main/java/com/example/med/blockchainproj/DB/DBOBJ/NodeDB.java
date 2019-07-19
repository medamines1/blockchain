package com.example.med.blockchainproj.DB.DBOBJ;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

import com.example.med.blockchainproj.DB.node;
import com.example.med.blockchainproj.Interfaces.nodeInt;

import static com.example.med.blockchainproj.MainContole.DATABASE_NAME;

/**
 * Created by med on 10/19/2018.
 */

@Database(entities = {node.class}, version = 2, exportSchema = false)
public abstract class NodeDB extends RoomDatabase {
    public static  NodeDB node_db=null;
    public abstract nodeInt daoAccess() ;

    public static void addNodeWithIp(NodeDB nodedb,String ip) {
        nodedb.daoAccess().insertNode(new node(ip));
    }

    public static NodeDB getNode_db(Context context) {
        Log.w("database node : ",(node_db==null )+" : " + node_db);
        if (node_db==null)
            node_db = Room.databaseBuilder(
                    context,
                    NodeDB.class,
                    DATABASE_NAME + " _node")
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2)
                    .build();
        return node_db;
    }
    public static void closedb(){
        if (node_db!=null)
        node_db.close();
        Log.w("datab :","closed");
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };
}
