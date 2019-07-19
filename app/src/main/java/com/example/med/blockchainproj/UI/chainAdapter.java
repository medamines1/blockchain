package com.example.med.blockchainproj.UI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.med.blockchainproj.DB.BlockD;
import com.example.med.blockchainproj.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by med on 11/20/2018.
 */

public class chainAdapter extends BaseAdapter {
    private final Context context;
    private final List<BlockD> blocks;
    int from = 5;
    List<BlockD> data;
    public chainAdapter(Context context, List<BlockD> blocks) {
        this.context = context;
        this.blocks = blocks;
        this.data = new CopyOnWriteArrayList<BlockD>();
        try {
            if (blocks.size()>5)
                this.data.addAll(blocks.subList(0, 5));
            else
                this.data.addAll(blocks.subList(0, blocks.size()));
        }catch(Exception e ){
            if (blocks!=null)
                this.data.addAll(blocks);
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BlockD mblock = data.get(position);


        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.blocks_lay, null);
        }

        TextView id = convertView.findViewById(R.id.id);
        TextView name = convertView.findViewById(R.id.identifier);
        TextView amount = convertView.findViewById(R.id.amount);



        try {
            id.setText(String.valueOf(mblock.getTrans_id()));
            name.setText(mblock.getChain_name());
            Log.w("data : ",mblock.getData());
            try {//fix this later it's horrible
                amount.setText(new JSONObject(mblock.getData()).get("amount").toString());
            }catch (Exception e){
                amount.setText(new JSONObject(new JSONObject(mblock.getData()).get("data").toString()).get("amount").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public void deleteItem(int position) {
        blocks.remove(position);
    }
    public boolean ShowMore(){
        try {
            int to=from +5;
            if (to > blocks.size())
                to = from + (to - (blocks.size()-1)) ;

            Log.w("tes : ",from + " :: "+ to  + " :: "+ blocks.size());
            List<BlockD> tdata = blocks.subList(from,to);
            for (BlockD e : tdata)
                data.add(e);

        }catch (Exception e) {
            return false;
        }




        if (from >= blocks.size()-1)
            return false;
        from +=5;
            return true;
        }
}
