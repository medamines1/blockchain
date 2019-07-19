package com.example.med.blockchainproj.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.med.blockchainproj.DB.BlockD;
import com.example.med.blockchainproj.DB.DBOBJ.chainD_DB;
import com.example.med.blockchainproj.DB.chainD;
import com.example.med.blockchainproj.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by med on 11/14/2018.
 */

public class chain_creator extends AppCompatActivity {

    EditText name;
    EditText diff;
    EditText datag;
    Button creater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_chain);
        name = findViewById(R.id.nameChain);
        diff = findViewById(R.id.diff);
        datag = findViewById(R.id.genesis);
        creater = findViewById(R.id.createC);

    creater.setOnClickListener(new View.OnClickListener() {
        Context context = chain_creator.this;
        @Override
        public void onClick(View view) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String cname = name.getText().toString();
                        chainD_DB chain_db = chainD_DB.getchain_db(getApplicationContext());
                        chainD chain = new chainD(cname,-1,1);
                        Gson g = new Gson();
                        chain_db.daoAccess().insertChain(chain);
                        Map<String,String> m = new HashMap<>();

                        m.put("amount","20");
                        m.put("sendto","med");
                        m.put("from","med");
                        m.put("create","med amine dahmen");
                        m.put("email","m_amine22@outlook.com");
                        String o = g.toJson(m);
                        m.clear();
                        m.put("data",o);
                        m.put("name",cname);
                        o = g.toJson(m);
                        m.clear();
                        m.put("data",o);
                        BlockD genesis = new BlockD(
                                "000000000000000000000000"
                                ,o
                                ,1
                        );

                        genesis.mine();
                        genesis.setChain_name(cname);
                        chain.add_genesis(getApplicationContext(),genesis);
                    }catch (Exception e){
                        Looper.prepare();
                        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("error " + e.getMessage());
                        pDialog.setCancelable(false);
                        pDialog.show();
                        Looper.loop();
                    }




                    Looper.prepare();
                    SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Loading");
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
