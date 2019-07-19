package com.example.med.blockchainproj.fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.med.blockchainproj.DAPP.utils;
import com.example.med.blockchainproj.DB.BlockD;
import com.example.med.blockchainproj.DB.DBOBJ.BlockD_DB;
import com.example.med.blockchainproj.R;
import com.example.med.blockchainproj.UI.chainAdapter;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by med on 11/14/2018.
 */

public class chainFrag extends Fragment {
    TextView txt;
    GridView gridView;
    Button l_more;
    public chainFrag(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chainf, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        txt = getActivity().findViewById(R.id.dis_title);
        l_more =getActivity().findViewById(R.id.load_more);



        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                final List<BlockD> tmp = BlockD_DB.getBlock_db(getActivity()).daoAccess().getAllBlocks("main");//only for main chain for now
                gridView = getActivity().findViewById(R.id.ourGrid);



                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final chainAdapter chainAdapter = new chainAdapter(getActivity(), tmp);
                        gridView.setAdapter(chainAdapter);
                        l_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                l_more.setEnabled(chainAdapter.ShowMore());
                                gridView.invalidate();
                                chainAdapter.notifyDataSetChanged();

                            }
                        });
                        txt.setText(String.valueOf(tmp.size()));
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                                final BlockD p = tmp.get(position);




                                try {
                                    SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                                    pDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface Dialog) {
                                            SweetAlertDialog alertDialog = (SweetAlertDialog) Dialog;

                                            TextView text = alertDialog.findViewById(R.id.content_text);
                                            text.setMovementMethod(new ScrollingMovementMethod());
                                            text.setMinLines(4);
                                            text.setMaxLines(9);
                                        }
                                    });

                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                    pDialog.setTitleText("Block #"+ p.getTrans_id());
                                    pDialog.setContentText(utils.formatString(p.getProper()));
                                    pDialog.setCancelable(false);
                                    pDialog.show();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

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

