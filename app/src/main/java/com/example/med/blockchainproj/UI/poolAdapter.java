package com.example.med.blockchainproj.UI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.med.blockchainproj.DB.pool_elm;
import com.example.med.blockchainproj.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by med on 11/20/2018.
 */

public class poolAdapter extends BaseAdapter {
    private final Context context;
    private final List<pool_elm> pool;

    public poolAdapter(Context context, List<pool_elm> pool) {
        this.context = context;
        this.pool = pool;
    }

    @Override
    public int getCount() {
        return pool.size();
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
        Log.w("f-----------","error");
        final pool_elm mpool = pool.get(position);


        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.pool_lay, null);
        }


        TextView from = convertView.findViewById(R.id.from);
        TextView to = convertView.findViewById(R.id.identifier);
        TextView amount = convertView.findViewById(R.id.amount);



        try {
            Log.w("rece",mpool.getJson().toString());
            JSONObject tmp = new JSONObject(mpool.getJson().toString());
            from.setText(tmp.getString("from"));
            to.setText(tmp.getString("sendto"));
            amount.setText(tmp.getString("amount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    public void deleteItem(int position) {
        pool.remove(position);
    }
}
