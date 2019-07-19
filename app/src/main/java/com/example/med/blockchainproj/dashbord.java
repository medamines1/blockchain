package com.example.med.blockchainproj;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.med.blockchainproj.DB.DBOBJ.Keys_DB;
import com.example.med.blockchainproj.fragments.DashbordFrag;
import com.example.med.blockchainproj.fragments.SettingsFrag;
import com.example.med.blockchainproj.fragments.Transaction;
import com.example.med.blockchainproj.fragments.chainFrag;

/**
 * Created by med on 11/9/2018.
 */

public class dashbord extends AppCompatActivity {

    private BottomNavigationView mainView;
    private FrameLayout frame;

    private chainFrag chainf;
    private DashbordFrag dashf;
    private SettingsFrag settf;
    private Transaction tr;
    static Keys_DB keys_db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashbord);

        mainView = findViewById(R.id.main_navigation);
        frame = findViewById(R.id.main_frame);
        chainf = new chainFrag();
        dashf = new DashbordFrag();
        settf = new SettingsFrag();
        tr = new Transaction();

        setFrag(dashf);
        mainView.setItemBackgroundResource(R.color.holo_blue);
        mainView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        mainView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));



        mainView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.navigation_dashboard :
                        setFrag(dashf);
                        return true;

                    case R.id.navigation_chaine :
                        setFrag(chainf);
                        return true;

                    case R.id.navigation_settings :
                        setFrag(settf);
                        return true;
                    case R.id.navigation_transaction:
                        setFrag(tr);
                        return  true;
                    default:
                        return false;
                }
            }
        });

    }

    private  void  setFrag(Fragment frag){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_frame, frag);
        transaction.commit();

    }



}
