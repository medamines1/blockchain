package com.example.med.blockchainproj.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.med.blockchainproj.DAPP.utils;
import com.example.med.blockchainproj.DB.DBOBJ.BlockD_DB;
import com.example.med.blockchainproj.DB.DBOBJ.Pool_DB;
import com.google.gson.Gson;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by med on 11/19/2018.
 */
@Entity
public class pool_elm implements Serializable{
    @PrimaryKey
    @NonNull
    private String id_hash;

    @NonNull
    private String name;

    @NonNull
    private String transaction;

    private String timestamp;
    @Ignore
    public pool_elm(String transaction) {
        this.id_hash = new String(Hex.encodeHex(DigestUtils.sha256(transaction.getBytes())));
        this.transaction = transaction;
        this.timestamp = utils.getTimeStamp().toString();
    }

    public pool_elm(@NonNull String id_hash,@NonNull String name, @NonNull String transaction,String timestamp) {
        this.id_hash = id_hash;
        this.transaction = transaction;
        this.name = name;
        this.timestamp = timestamp;
    }

    @Ignore
    public pool_elm(@NonNull String id_hash,@NonNull String name, @NonNull String transaction) {
        this.id_hash = id_hash;
        this.transaction = transaction;
        this.timestamp = utils.getTimeStamp().toString();
    }

    @NonNull
    public String getId_hash() {
        return id_hash;
    }

    public void setId_hash(@NonNull String id_hash) {
        this.id_hash = id_hash;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        pool_elm pool_elm = (pool_elm) o;
        return (pool_elm.transaction == this.transaction) &&
                (this.timestamp == pool_elm.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction, timestamp);
    }

    public String getProepr() {
        Gson g= new Gson();
        Map<String,String> m = new HashMap<>();
        m.put("transaction",transaction);
        m.put("timestamp",timestamp);
        m.put("id_hash",id_hash);


        return  g.toJson(m);
    }

    public JSONObject getJson() {
        try {
            return  new JSONObject(transaction);
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public String toString() {
        return "pool_elm{" +
                "id_hash='" + id_hash + '\'' +
                ", transaction='" + transaction + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public BlockD mine(Context context,String hash_id,String name) {
        Pool_DB pool_db = Pool_DB.getPool_db(context);
        pool_elm p = pool_db.daoAccess().getById(hash_id,name);
        BlockD rs = BlockD_DB.getBlock_db(context).daoAccess().getLast(name);
        long id=0;
        if (rs!=null)
            id=rs.getTrans_id();
        BlockD b = new BlockD(p,id+1,BlockD.getLastPreviousHash(context,name));
        b.mine();
        BlockD_DB.getBlock_db(context).daoAccess().insert(b);

        return b;

    }
}
