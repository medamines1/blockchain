package com.example.med.blockchainproj.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.med.blockchainproj.DAPP.RequestObj;
import com.example.med.blockchainproj.DAPP.network;
import com.example.med.blockchainproj.DB.BlockD;
import com.example.med.blockchainproj.DB.DBOBJ.Pool_DB;
import com.example.med.blockchainproj.DB.pool_elm;
import com.example.med.blockchainproj.R;
import com.example.med.blockchainproj.UI.poolAdapter;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.med.blockchainproj.MainContole.BRODCAST_SUCCESS_MINING;

/**
 * Created by med on 11/14/2018.
 */

public class DashbordFrag extends Fragment {
    TextView txt;
    GridView gridView;


    public DashbordFrag(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashf, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        txt = getActivity().findViewById(R.id.dis_title);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                final List<pool_elm> tmp = Pool_DB.getAll(getActivity());
                gridView = getActivity().findViewById(R.id.ourGrid);



                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final poolAdapter poolAdapter = new poolAdapter(getActivity(), tmp);
                        gridView.setAdapter(poolAdapter);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                                final pool_elm p = tmp.get(position);


                                Thread t = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            BlockD b = p.mine(getActivity(), p.getId_hash(), p.getName());
                                            Map<String,String> m = new HashMap<>();
                                            Gson g= new Gson();

                                            m.put("id",String.valueOf(b.getTrans_id()));
                                            m.put("nonce",b.getNonce());
                                            m.put("name",b.getChain_name());
                                            m.put("id_hash",p.getId_hash());
                                            m.put("previous_hash",b.getPrevious_Hash());
                                            m.put("diff",String.valueOf(b.getDiff()));
                                            m.put("data",b.getData());
                                            m.put("timestamp",b.getTimestamp());


                                            network.brodcast(getActivity(),new RequestObj(BRODCAST_SUCCESS_MINING,g.toJson(m)));
                                            Pool_DB.getPool_db(getActivity()).daoAccess().deleteByHashid(p.getId_hash());


                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    poolAdapter.deleteItem(position);
                                                    poolAdapter.notifyDataSetChanged();
                                                    SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                    pDialog.setTitleText("finished mining ! ");
                                                    pDialog.setCancelable(false);
                                                    pDialog.show();

                                                }
                                            });
                                        }catch (NullPointerException e){

                                            SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                            pDialog.setTitleText("chain is emppty ! ");
                                            pDialog.setCancelable(false);
                                            pDialog.show();

                                        }


                                    }
                                });
                                t.start();



                            }
                        });
                    }
                });

            }
        });
        t.start();
      //  this.chain = dashbord.getChain(getActivity());
       // if (chain !=null)
        //    txt.setText(String.valueOf(chain.getPrevious_block().getTrans_id()));
    }

}
