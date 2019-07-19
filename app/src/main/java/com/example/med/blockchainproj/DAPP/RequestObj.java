package com.example.med.blockchainproj.DAPP;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by med on 11/8/2018.
 */

public class RequestObj {
    private String action;
    private String param;

    public RequestObj(){

    }

    public  RequestObj(String action,String param) {
        this.action = action;
        this.param = param;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public static RequestObj toObj(String data){

        try {
            JSONObject rq = new JSONObject(data);
            String action = rq.getString("action");
            Log.w("testing : ",data + " :  " + rq);
            try {
                String param = (String)rq.get("param");
                return  new RequestObj(action, param);
            }catch (Exception e){
               Log.w("","[!] no param were included ... ");
            }
            return  new RequestObj(action, null);
        } catch (JSONException e) {
            e.printStackTrace();
            return  new RequestObj();
        }

    }

    public String toString(){
        Gson gson = new Gson();

        Map<String,String> m = new HashMap<>();
        Map<String,String> d = new HashMap<>();
        if(param !=null)
            d.put("data", param);
        else
            d.put("data", "");


        m.put("action",action);
        m.put("param", gson.toJson(d));

        return  gson.toJson(m);
    }

}
