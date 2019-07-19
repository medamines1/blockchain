package com.example.med.blockchainproj;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.med.blockchainproj.DAPP.SocketServerThread;
import com.example.med.blockchainproj.DAPP.network;
import com.example.med.blockchainproj.UI.chain_creator;

import com.example.med.blockchainproj.DB.DBOBJ.Keys_DB;
import com.example.med.blockchainproj.DB.DBOBJ.NodeDB;
import com.example.med.blockchainproj.DB.DBOBJ.Pool_DB;

public class MainContole extends AppCompatActivity {
    public static final String DATABASE_NAME = "database";
    public static final String LABEL_MAIN = "user_keys";
    public static final String BRODCAST_SUCCESS_MINING = "BRODCAST_SUCCESS_MINING ";
    public static final int  DATABASE_VERSIOM = 1;
    NodeDB nodedb;
    Button button;
    Button chain_button;
    public static WifiManager wifiMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contole);
        wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final String myip = network.get_my_local_ip(wifiMgr);
        TextView ip = findViewById(R.id.my_ip);

        if (myip.length()>0)
            ip.setText(myip);
        else
            ip.setText("localhost");

        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"started ... ",Toast.LENGTH_LONG);
                button.setEnabled(false);

                Thread thread = new Thread() {
                    @Override
                    public void run() {


                        TextView ip =  findViewById(R.id.my_ip);


                        nodedb = NodeDB.getNode_db(getApplicationContext());

                        Log.w("dtbase : : ",nodedb.daoAccess().getAllNodes().toString());

                       Keys_DB keys_db = Keys_DB.getKeys_db(getApplicationContext());

                        Pool_DB  pool_db = Pool_DB.getPool_db(getApplicationContext());



                      //test for the keys
                        keys_db.CheckUserKeys(getApplicationContext(),keys_db);


                       Thread socketServerThread = new Thread(new SocketServerThread(getApplicationContext(),ip.getText().toString(), nodedb,pool_db));
                       socketServerThread.start();


                        Intent dash = new Intent(getBaseContext(), dashbord.class);
                        startActivity(dash);

                    }
                };
                thread.start();
            }
        });

        chain_button = findViewById(R.id.create_chain);
        chain_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent ch = new Intent(getBaseContext(), chain_creator.class);
                startActivity(ch);
            }
        });
    }

}
