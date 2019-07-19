package com.example.med.blockchainproj.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.med.blockchainproj.DAPP.utils;
import com.example.med.blockchainproj.DB.DBOBJ.BlockD_DB;

import java.io.Serializable;

/**
 * Created by med on 11/23/2018.
 *
 * foreignKeys=@ForeignKey(entity = BlockD.class,
 parentColumns = "trans_id",
 childColumns = "previous_BlockD_id",
 onDelete = CASCADE))
 */

@Entity
public class chainD  implements Serializable{
    @PrimaryKey
    @NonNull
    private String name;
    @NonNull
    private long previous_BlockD_id;

    @NonNull
    private float diff = 1;

    @NonNull
    private String timestamp;

    public chainD(@NonNull  String name, @NonNull long previous_BlockD_id, @NonNull float diff) {
        this.name = name;
        this.previous_BlockD_id = previous_BlockD_id;
        this.diff = diff;
        this.timestamp = utils.getTimeStamp().toString();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrevious_BlockD_id() {
        return previous_BlockD_id;
    }

    public void setPrevious_BlockD_id(long previous_BlockD_id) {
        this.previous_BlockD_id = previous_BlockD_id;
    }

    @NonNull
    public float getDiff() {
        return diff;
    }

    public void setDiff(@NonNull float diff) {
        this.diff = diff;
    }

    @NonNull
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean check_BlockD(BlockD b) {
        b.setDiff(this.diff);
        String v = BlockD.hash_this_data(b.getProper());
        return b.diff_test(v);
    }

    public boolean add(Context context, BlockD b) {
        if (check_BlockD(b)) {
              previous_BlockD_id = BlockD_DB.save_and_get_id(context,b);
            return true;
        }
        return false;
    }

    public void add_genesis(Context context,BlockD b) {
        previous_BlockD_id = BlockD_DB.save_and_get_id(context,b);
    }





    public float Update_difficulty(Context context,BlockD previous_BlockD) {
        //update difficulty

        if (previous_BlockD.getTrans_id() >= 2016 ) { // check if we passed 2016 BlockD
            int sign,perc;
            float value =  ((previous_BlockD.getDiff() * 20160 ) /this.getLast2016TimeInSec(context,previous_BlockD));

            if (value > diff) {
                if (value > 4 ) {
                    return previous_BlockD.getDiff()+ 2; //can't go up more than a factor of 4
                }
                else if ( (int)value ==2) {
                    return previous_BlockD.getDiff()+ 2;
                }
                else if ( (int)value ==1) {
                    return previous_BlockD.getDiff()+ 1;
                }

                return value;
            }else if (value < diff) {
                if (value > utils.get_percent_of_value(previous_BlockD.getDiff(), 25))
                    return previous_BlockD.getDiff() +utils.get_percent_of_value(previous_BlockD.getDiff(), 25);
                else
                    return previous_BlockD.getDiff() + value;


            }
        }
        return  diff; //no update were made
    }


    private int getLast2016TimeInSec(Context context,BlockD previous_BlockD){
        BlockD_DB block_DB = BlockD_DB.getBlock_db(context);
        return ((int) (block_DB.daoAccess().getTimestamp(previous_BlockD.getTrans_id()) - block_DB.daoAccess().getTimestamp(previous_BlockD.getTrans_id() - 2016)));

    }

}
