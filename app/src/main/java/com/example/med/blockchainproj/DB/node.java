package com.example.med.blockchainproj.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

import com.example.med.blockchainproj.DB.DBOBJ.NodeDB;


/**
 * Created by med on 10/19/2018.
 */

@Entity
public class node implements Serializable{
    @NonNull
    @PrimaryKey
    private String ip;
    private String username;



    public node(String ip) {
        this.ip = ip;
    }

    @NonNull
    public String getIp() {
        return ip;
    }

    public void setIp(@NonNull String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static  void check_and_save_user(String remote_ip,NodeDB node){
        try {
            node n =node.daoAccess().getNodeByIp(remote_ip);
            if (n==null)
                node.daoAccess().insertNode(new node(remote_ip));

        }catch (Exception e){
            e.getMessage();

        }


    }


}
