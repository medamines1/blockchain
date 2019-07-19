package com.example.med.blockchainproj.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.med.blockchainproj.BlockChain;
import com.example.med.blockchainproj.DAPP.RequestObj;
import com.example.med.blockchainproj.DAPP.network;
import com.example.med.blockchainproj.DAPP.utils;
import com.example.med.blockchainproj.DB.BlockD;
import com.example.med.blockchainproj.DB.DBOBJ.Keys_DB;
import com.example.med.blockchainproj.DB.DBOBJ.Pool_DB;
import com.example.med.blockchainproj.R;
import com.example.med.blockchainproj.generater;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by med on 11/19/2018.
 */

public class Transaction extends Fragment {

    BlockChain chain ;
    Button send;
    EditText sto;
    EditText amount;
    EditText cname;


    public Transaction(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transaction_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        send = getActivity().findViewById(R.id.send_but);

        amount = getActivity().findViewById(R.id.amount);
        sto = getActivity().findViewById(R.id.identifier);
        cname = getActivity().findViewById(R.id.cname);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson g = new Gson();
                        Map<String,String> m = new HashMap<>();
                        m.put("sendto",sto.getText().toString());
                        m.put("amount",amount.getText().toString());
                        m.put("from", Keys_DB.getMypub(getActivity()));
                        m.put("timestamp",utils.getTimeStamp().toString());

                        String o =  g.toJson(m);
                        String timestamp = utils.getTimeStamp().toString();
                        m.clear();
                        m.put("trans",o);
                        m.put("timestamp",timestamp);
                        m.put("name",cname.getText().toString());
                        String hash_id = BlockD.hash_this_data(g.toJson(m).toString());
                        Pool_DB.addP(getActivity(),hash_id ,cname.getText().toString(),o,timestamp);
                        try {
                            m.put("signutare", generater.getSig(o,Keys_DB.getpriv(getActivity())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        o =  g.toJson(m);

                        RequestObj rq = new RequestObj("brodcast_transaction",o);
                        network.brodcast(getActivity(),rq);

                        Looper.prepare();
                        SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("sent");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        Looper.loop();
                    }
                });
               t.start();

            }
        });
    }
}
