package com.example.med.blockchainproj.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;


/**
 * Created by med on 10/19/2018.
 */

@Entity
public class mykeys implements Serializable{
    @NonNull
    @PrimaryKey
    private String prik;
    @NonNull
    private String pubk;

    private String label;



    public mykeys(@NonNull String prik, @NonNull String pubk) {
        this.prik = prik;
        this.pubk = pubk;
    }

    @NonNull
    public String getPrik() {
        return prik;
    }

    public void setPrik(@NonNull String prik) {
        this.prik = prik;
    }

    @NonNull
    public String getPubk() {
        return pubk;
    }

    public void setPubk(@NonNull String pubk) {
        this.pubk = pubk;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
