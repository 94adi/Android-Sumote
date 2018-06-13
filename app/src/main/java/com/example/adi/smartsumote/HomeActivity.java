package com.example.adi.smartsumote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button btn_start;
    Intent intent_to_bt_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start_handler();
            }
        });
    }

    public void btn_start_handler(){

        intent_to_bt_activity = new Intent(this,BluetoothActivity.class);
        startActivity(intent_to_bt_activity);
    }

}
