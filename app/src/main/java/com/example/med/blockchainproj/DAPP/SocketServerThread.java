package com.example.med.blockchainproj.DAPP;

import android.content.Context;
import android.util.Log;

import com.example.med.blockchainproj.DB.BlockD;
import com.example.med.blockchainproj.DB.DBOBJ.BlockD_DB;
import com.example.med.blockchainproj.DB.DBOBJ.NodeDB;
import com.example.med.blockchainproj.DB.DBOBJ.Pool_DB;
import com.example.med.blockchainproj.DB.node;
import com.example.med.blockchainproj.DB.pool_elm;
import com.example.med.blockchainproj.generater;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.med.blockchainproj.MainContole.BRODCAST_SUCCESS_MINING;

/**
 * Created by med on 11/8/2018.
 */

public class SocketServerThread extends Thread {

    static final int SocketServerPORT = 2121;
    ServerSocket serverSocket;
    NodeDB nodedb;
    Pool_DB pool_db;
    Context contexte;

    public SocketServerThread(Context context,String ip,NodeDB nodedb,Pool_DB pool_db){
        this.contexte = context;
        this.nodedb = nodedb;
        this.pool_db = pool_db;

    }

    @Override
    public void run() {
        Socket socket = null;
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;

        try {
            serverSocket = new ServerSocket(SocketServerPORT);


            while (true) {
                socket = serverSocket.accept();
                dataInputStream = new DataInputStream(
                        socket.getInputStream());
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());

                String request = "";

                //If no message sent from client, this code will block the program
                request = dataInputStream.readUTF();

                String remote_ip =(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");

                dataOutputStream.writeUTF(Request_Mapping(request,remote_ip,nodedb,pool_db).toString());


            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            final String errMsg = e.toString();
            Log.w("err msg : ",errMsg);

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
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
        }
    }

    private ResponseObj Request_Mapping(String request,String remote_ip,NodeDB nodedb,Pool_DB pool_db) throws JSONException {
        RequestObj rs = RequestObj.toObj(request);
        switch (rs.getAction()){
            case "meet_sync":
                node.check_and_save_user(remote_ip,nodedb);
                return  new ResponseObj("aprv_sync",null);

            case "async" :
                try {

                    Map<String,String> m = new HashMap<>();

                    Map<String,String> d = new HashMap<>();

                    List<BlockD> data =BlockD_DB.getBlock_db(contexte).daoAccess().getAllBlocks("main");
                    Map<String,String>  mn = new HashMap<>();
                    int i = 0;
                    for (BlockD e : data) {
                        mn.put(String.valueOf(i),e.getProper());
                        i++;
                    }
                    d.put("chain",new JSONObject(mn).toString());

                    List<pool_elm> tmp1 =pool_db.daoAccess().getAllPool();
                   mn.clear();

                    for (pool_elm e : tmp1) {
                        mn.put(String.valueOf(i),e.getProepr());
                        i++;
                    }

                    d.put("pools",new JSONObject(mn).toString());






                    List<node> tmp =nodedb.daoAccess().getAllNodes();
                    mn.clear();
                    i = 0;
                    for (node e : tmp) {
                        mn.put(String.valueOf(i),e.getIp());
                        i++;
                    }

                    d.put("nodes",new JSONObject(mn).toString());

                    return  new ResponseObj("sync",new JSONObject(d));

                } catch (Exception e) {
                    e.printStackTrace();
                }


            case "brodcast_transaction":

                final JSONObject data =new JSONObject(new JSONObject(rs.getParam()).get("data").toString());
                final String signutre = data.get("signutare").toString();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            String trans = (String) data.get("trans");
                            String from = (String) new JSONObject(trans).get("from");
                            generater.verify(trans,signutre,from);
                            String name = (String) data.get("name");
                            String timestamp = (String) data.get("timestamp");
                            if ( generater.verify(trans,signutre,from) )
                                Pool_DB.addP(contexte, BlockD.hash_this_data(trans),name,trans,timestamp);
                            else
                                Log.w("transaction ::", "was refused !!!");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
                return  new ResponseObj("informed");
            case BRODCAST_SUCCESS_MINING:
                final String data2 =rs.getParam();
                Thread t2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject a = new JSONObject( (String) new JSONObject(data2).get("data"));


                            long trans_id = Long.valueOf((String)a.get("id"));
                            String nonce = (String) a.get("nonce");
                            String id_hash = (String) a.get("id_hash");
                            String name = (String) a.get("name");
                            String prev = (String) a.get("previous_hash");
                            String data = (String) a.get("data");
                            String diff = (String) a.get("diff");
                            String timestamp = (String) a.get("timestamp"
                            );

//                            BlockD b = new BlockD(prev,data2,name,nonce,trans_id,);
                            BlockD_DB blockD_db = BlockD_DB.getBlock_db(contexte);
                            BlockD bl =blockD_db.daoAccess().getLast(name);

                            bl.setTrans_id(trans_id);
                            bl.setPrevious_Hash(prev);
                            bl.setNonce(nonce);
                            bl.setData(data);
                            bl.setDiff(Float.valueOf(diff));
                            bl.setTimestamp(timestamp);

                            BlockD tmp =blockD_db.daoAccess().geBlockWithID(trans_id,"main");
                            if (tmp ==null) {
                                blockD_db.daoAccess().insert(bl);
                                Pool_DB pool_db = Pool_DB.getPool_db(contexte);
                                pool_db.daoAccess().deleteByHashid(id_hash);
                            }





                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                t2.start();
        }//switch
        Log.w("response was : ",rs.getAction()+ " :: "+rs.getParam() );

      return new ResponseObj("undefined procedure");
    };

}

