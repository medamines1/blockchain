package com.example.med.blockchainproj.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.med.blockchainproj.DAPP.utils;
import com.example.med.blockchainproj.DB.DBOBJ.BlockD_DB;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by med on 11/23/2018.
 */

@Entity
public class BlockD implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private  long trans_id;

    @NonNull
    private String chain_name;

    @NonNull
    private String previous_Hash;

    private String nonce="";

    @NonNull
    private String data;
    @NonNull
    private String timestamp ;
    @NonNull
    private float diff = 1;

    @Ignore
    public BlockD(pool_elm p,long trans_id,String previous_hash) {
        this.trans_id = trans_id;
        this.data = p.getTransaction();
        this.chain_name = p.getName();
        this.timestamp = p.getTimestamp();
        this.previous_Hash = previous_hash;

    }

    public BlockD(@NonNull String previous_Hash,  @NonNull String data, @NonNull float diff) {
        this.previous_Hash = previous_Hash;
        this.data = data;
        this.diff = diff;
        this.timestamp = utils.getTimeStamp().toString();
    }


    @NonNull
    public long getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(@NonNull long trans_id) {
        this.trans_id = trans_id;
    }

    @NonNull
    public String getChain_name() {
        return chain_name;
    }

    public void setChain_name(@NonNull String chain_name) {
        this.chain_name = chain_name;
    }

    @NonNull
    public String getPrevious_Hash() {
        return previous_Hash;
    }

    public void setPrevious_Hash(@NonNull String previous_Hash) {
        this.previous_Hash = previous_Hash;
    }


    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    @NonNull
    public String getData() {
        return data;
    }

    public void setData(@NonNull String data) {
        this.data = data;
    }

    @NonNull
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull String timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public float getDiff() {
        return diff;
    }

    public void setDiff(@NonNull float diff) {
        this.diff = diff;
    }

    public static String hash_this_data(String data) {
        return new String(Hex.encodeHex(DigestUtils.sha256(data.getBytes())));
    }
    public boolean diff_test(String data) {
        if ( data.substring(0, (int)this.diff).equals(n_zero((int)this.diff)) )
            return true;
        return false;
    }

    private String n_zero(int diff) {
        String tmp = "";
        for (int i=0;i<diff;i++)
            tmp += "0";
        return tmp;
    }

    public void mine() {
        int ne = 0 ;
        String ha ="";
        //System.out.println("mining started");
        while (true){
            this.nonce = String.valueOf(ne);
            ha = hash_this_data(getProper());
            if (diff_test(ha)) {
                this.nonce =  String.valueOf(ne);
                break;
            }
            ne += 1;
        }
    }


    public String getProper() {
        Map<String,String> map = new HashMap<>();
        map.put("id", String.valueOf(trans_id));
        map.put("data", data);
        map.put("chain_name",chain_name);
        map.put("nonce", nonce);
        map.put("diff", String.valueOf(diff));
        map.put("timestamp",String.valueOf(timestamp));
        if (previous_Hash !=null)
            try {
                map.put("previous_hash", previous_Hash);
            } catch (Exception e) {
                System.out.println("[?] you must set the previous hash !");
            }

        return new JSONObject(map).toString();
    }

    public static String getLastPreviousHash(Context context, String name) {
        BlockD_DB b = BlockD_DB.getBlock_db(context);
        return  BlockD.hash_this_data(b.daoAccess().getLast(name).getProper());
    }
}
