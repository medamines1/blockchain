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

public class ResponseObj {
    private String response;
    private JSONObject data;

    public ResponseObj(){

    }
    public  ResponseObj(String response){
        this.response = response;
    }

    public ResponseObj(String response, JSONObject data) {
        this.response = response;
        this.data = data;
    }



    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String toString(){
        Gson gson = new Gson();
        Map<String,String> m = new HashMap<>();
        m.put("response",response);
        if (data != null)
            m.put("data",data.toString());
        else
            m.put("data","");

        return  gson.toJson(m);
    }


    public static ResponseObj toObj(String data){

        try {
            JSONObject rq = new JSONObject(data);
            String action = rq.getString("response");
            JSONObject tmp;
            try {
                Log.w("respons obj : : data ::",rq.toString());
            tmp = new JSONObject((String)rq.get("data"));
            }catch (Exception e){
                e.printStackTrace();
                tmp=null;
            }
            return  new ResponseObj(action,tmp);
        } catch (JSONException e) {
            e.printStackTrace();
            return  new ResponseObj();
        }

    }
}
