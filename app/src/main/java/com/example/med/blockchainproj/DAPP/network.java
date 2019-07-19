package com.example.med.blockchainproj.DAPP;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.med.blockchainproj.DB.DBOBJ.NodeDB;
import com.example.med.blockchainproj.DB.DBOBJ.Pool_DB;
import com.example.med.blockchainproj.DB.node;

import java.util.List;

/**
 * Created by med on 11/6/2018.
 */

public class network {
;

    //search for one ip address
    //we only need to find one user to synchronize to the chain
    public static String look_for_any_node(final Context context,final String subnet) {
        String host;
        for (int i = 98; i < 255; i++) {
            host = subnet + i;
            try {
                MyClientTask myClientTask = new MyClientTask(
                        context,
                        NodeDB.getNode_db(context),
                        host,
                        2121,
                        new RequestObj("HeartBeat", null).toString());
                String rs = myClientTask.sendData();

                if (rs.contains("HeartBeat")){
                    return host;
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  null;

    }



        public static String get_my_local_ip( WifiManager wifiMgr){

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));

        }

        public static void connect_to_network(final Context context, final String ip){

               Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                MyClientTask myClientTask = new MyClientTask(
                        context,
                        NodeDB.getNode_db(context),
                        ip,
                        2121,
                         new RequestObj("meet_sync",null).toString());
                myClientTask.sendData();

                MyClientTask mc2 = new MyClientTask(
                        context,
                        NodeDB.getNode_db(context),
                        ip,
                        2121,
                        new RequestObj("async",null).toString());
                mc2.sendData();
                NodeDB.getNode_db(context).daoAccess().insertNode(new node(ip));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    thread.start();


        }


    public static void synchronize(Context context,  String host) {
        MyClientTask mct = new MyClientTask(context
                ,NodeDB.getNode_db(context)
                ,Pool_DB.getPool_db(context)
                ,host
                ,2121
                ,new RequestObj("synch",null).toString());
        mct.sendData();

        }
    public static void brodcast(final Context context,final RequestObj rq){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //create nodeDB
                NodeDB nodedb = NodeDB.getNode_db(context);
                MyClientTask t;
                List<node> li = nodedb.daoAccess().getAllNodes();
                for (node e: li){
                     t = new MyClientTask(context
                             ,nodedb
                             ,e.getIp()
                             ,2121
                             ,rq.toString());
                    Log.w("sending :: ",rq.toString());
                    t.sendData();
                }
            }

        });
        t.start();


    }



 /*   Thread thread = new Thread() {
        @Override
        public void run() {

            nodeDataB = Room.databaseBuilder(getApplicationContext(),
                    NodeDB.class, DATABASE_NAME)
                    .build();


            //node a = new node("192.168.1.2");
            //node b = new node("192.168.1.9");

            //nodeDataB.daoAccess().insertNode(a);
            //nodeDataB.daoAccess().insertNode(b);

            //Log.w("database == > ",nodeDataB.daoAccess().getAllNodes().toString());

        }
    };
    thread.start();*/
}
