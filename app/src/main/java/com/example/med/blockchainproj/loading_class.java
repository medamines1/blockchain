package com.example.med.blockchainproj;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.example.med.blockchainproj.DAPP.network;

/**
 * Created by med on 11/6/2018.
 */

public class loading_class extends Activity {
    Button butt_n;
    Button butt_ok;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        AndroidNetworking.initialize(getApplicationContext());

        butt_n = findViewById(R.id.butt_no);
        butt_ok = findViewById(R.id.butt_ok);
        input = findViewById(R.id.input);
        butt_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            finish();
            }
        });

        butt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    network.connect_to_network(getBaseContext(), input.getText().toString());
                    finish();
            }
        });



    }
}
