package com.example.med.blockchainproj.DAPP;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.med.blockchainproj.DB.BlockD;
import com.example.med.blockchainproj.DB.DBOBJ.BlockD_DB;
import com.example.med.blockchainproj.DB.DBOBJ.NodeDB;
import com.example.med.blockchainproj.DB.DBOBJ.Pool_DB;
import com.example.med.blockchainproj.DB.node;
import com.example.med.blockchainproj.dashbord;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

/**
 * Created by med on 11/8/2018.
 */

public class MyClientTask extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String response = "";
    String msgToSend;
    RequestObj request;
    Context context;
    NodeDB nodedb;
    Pool_DB pool_db;
    public MyClientTask(Context context, NodeDB nodedb,Pool_DB pool_db , String addr, int port, String msgTo) {
        dstAddress = addr;
        dstPort = port;
        msgToSend = msgTo;
        this.context = context;
        this.nodedb = nodedb;
        this.pool_db = pool_db;
    }

   public MyClientTask(Context context, NodeDB nodedb,String addr, int port, String msgTo) {
        dstAddress = addr;
        dstPort = port;
        msgToSend = msgTo;
        this.context = context;
        this.nodedb = nodedb;
    }
    @Override
    protected Void doInBackground(Void... arg0) {
        return null;
    }

    public String sendData(){
        Socket socket = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;


        try {

            socket = new Socket(dstAddress, dstPort);

            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());


            if(msgToSend != null){
                dataOutputStream.writeUTF(msgToSend);
            }

            response = dataInputStream.readUTF();

            Mapping_For_Response(response,dstAddress);


        } catch (UnknownHostException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    private String Mapping_For_Response(String response,String ip) throws JSONException {
        ResponseObj rs = ResponseObj.toObj(response);
        Log.w("response :: ",rs.toString());
        switch (rs.getResponse())
        {
            case "meet_ok":
                node.check_and_save_user(ip,nodedb);
                Intent dash = new Intent(context, dashbord.class);
                context.startActivity(dash);
                break;
            case "HeartBeat":
                try {
                    nodedb.daoAccess().insertNode(new node(ip));
                }catch (Exception e){e.printStackTrace();}
                return "true";
            case "sync" :
                Gson g = new Gson();
                Log.w("sync data :: ",rs.getData().toString());
                JSONObject data = new JSONObject((String) rs.getData().get("nodes"));
                Iterator<String> keys = data.keys();
                String key;
                while(keys.hasNext()) {
                    key = keys.next();
                    try {
                        ip = (String) data.get(key);
                        NodeDB.addNodeWithIp(nodedb,ip);
                        Log.w("node added : ",ip);
                    } catch (Exception e) {
                        e.printStackTrace();
                        }
                    }

                JSONObject inner_data;
                 JSONObject chain_data = new JSONObject((String) rs.getData().get("chain"));
                keys = chain_data.keys();
                while(keys.hasNext()) {
                    key = keys.next();
                    try {
                        inner_data = new JSONObject((String)chain_data.get(key));
                        Log.w("inner_chain_data :: ",inner_data.toString());


                        long id = Long.valueOf((String) inner_data.get("id"));
                        String data2 = (String) inner_data.get("data");
                        String cname = (String) inner_data.get("chain_name");
                        String nonce = (String) inner_data.get("nonce");
                        Float diff = Float.valueOf((String) inner_data.get("diff"));
                        String times =(String) inner_data.get("timestamp");
                        String prev = (String) inner_data.get("previous_hash");
                        BlockD b = new BlockD(prev,data2,diff);
                        b.setTrans_id(id);
                        b.setChain_name(cname);
                        b.setNonce(nonce);
                        b.setTimestamp(times);
                        try {
                            BlockD_DB.getBlock_db(context).daoAccess().insert(b);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }





                String tr;
                String timestamp;
                String id_hash;
                data = new JSONObject((String) rs.getData().get("pools"));
                keys = data.keys();
                while(keys.hasNext()) {
                    key = keys.next();
                    try {
                        inner_data = new JSONObject((String)data.get(key));
                        tr  = (String) inner_data.get("transaction");
                        timestamp = (String) inner_data.get("timestamp");
                        id_hash  = (String) inner_data.get("id_hash");
                        Pool_DB.addP(context,id_hash,"main",tr,timestamp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;


        }//switch



        Log.w("response was : ",rs.toString());
        return  "undefined_response";
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

}

