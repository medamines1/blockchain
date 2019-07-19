package com.example.med.blockchainproj.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.med.blockchainproj.DAPP.network;
import com.example.med.blockchainproj.DB.DBOBJ.Keys_DB;
import com.example.med.blockchainproj.DB.mykeys;
import com.example.med.blockchainproj.MainContole;
import com.example.med.blockchainproj.R;
import com.example.med.blockchainproj.loading_class;

import java.util.List;

/**
 * Created by med on 11/14/2018.
 */

public class SettingsFrag extends Fragment {
    TextView pubkV;
    TextView prikV;
    Button entryp;
    Button autod;

    public SettingsFrag(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settingfrag, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        pubkV = getActivity().findViewById(R.id.pubk);
        prikV = getActivity().findViewById(R.id.prik);
        entryp = getActivity().findViewById(R.id.entryp);
        autod =getActivity().findViewById(R.id.autod);

        entryp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent load = new Intent(getActivity(),loading_class.class);
                startActivity(load);
            }
        });

        autod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final String ip = network.get_my_local_ip(MainContole.wifiMgr);
                        String host = network.look_for_any_node(getActivity()
                                ,ip.substring(0
                                        ,ip.lastIndexOf(".")+1));
                        network.synchronize(getActivity(),host);
                    }
                });
                t.start();
            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

             try{

                 Keys_DB keys_db = Keys_DB.getKeys_db(getActivity());
                Looper.prepare();
                List<mykeys> mk = keys_db.daoAccess().getSTKByLabel("user_keys");
                Log.w("database : ", mk.toString());
                pubkV.setText(mk.get(0).getPubk());
                pubkV.setMovementMethod(new ScrollingMovementMethod());
                prikV.setText(mk.get(0).getPrik());
                prikV.setMovementMethod(new ScrollingMovementMethod());
                Looper.loop();
            }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        if (pubkV.getText().toString().equals("None"))
            t.start();
    }
}
