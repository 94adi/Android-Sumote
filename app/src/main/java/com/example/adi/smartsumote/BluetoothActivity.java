package com.example.adi.smartsumote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BluetoothActivity extends AppCompatActivity {

    static final String BUTTON_VISIBLE = "BUTTON_VISIBLE";
    static final String BUTTON_INVISIBLE = "BUTTON_INVISIBLE";
    private final String address = "00:06:66:77:79:33";

    BluetoothCommunication bluetooth_com;
    BluetoothDevice mydevice;
    ToggleButton tb;
    Button btn_connect;
    Button btn_up;
    Button btn_down;
    Button btn_left;
    Button btn_right;
    Button btn_rot_left;
    Button btn_rot_right;
    Button btn_inc_speed;
    Button btn_dec_speed;
    Button btn_stop_car;
    Boolean tb_send_once = false; //false = tb_off ; true = tb_on
    TextView tv;
    ArrayList<Button> all_buttons;

    @Override
    protected void onDestroy(){
        super.onDestroy();
       // unregisterReceiver(mBroadcastReceiver);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        tv = (TextView) findViewById(R.id.tv_sumo);
        all_buttons = new ArrayList<>();
        tb = (ToggleButton) findViewById(R.id.toggleButton);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_up = (Button) findViewById(R.id.btn_up);
        all_buttons.add(btn_up);
        btn_down = (Button) findViewById(R.id.btn_down);
        all_buttons.add(btn_down);
        btn_left = (Button) findViewById(R.id.btn_left);
        all_buttons.add(btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        all_buttons.add(btn_right);
        btn_rot_left = (Button) findViewById(R.id.btn_rot_left);
        all_buttons.add(btn_rot_left);
        btn_rot_right = (Button) findViewById(R.id.btn_rot_right);
        all_buttons.add(btn_rot_right);
        btn_stop_car = (Button) findViewById(R.id.btn_stop);
        all_buttons.add(btn_stop_car);
        btn_inc_speed = (Button) findViewById(R.id.btn_inc);
        all_buttons.add(btn_inc_speed);
        btn_dec_speed = (Button) findViewById(R.id.btn_dec);
        all_buttons.add(btn_dec_speed);
        controls_init();
        tv.setVisibility(View.INVISIBLE);
        BT_init();
        btn_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String command = "W";
                byte[] bytes = command.getBytes(Charset.defaultCharset());
                bluetooth_com.write(bytes);
            }
        });
        btn_down.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String command = "S";
                byte[] bytes = command.getBytes(Charset.defaultCharset());
                bluetooth_com.write(bytes);
            }
        });
        btn_left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String command = "A";
                byte[] bytes = command.getBytes(Charset.defaultCharset());
                bluetooth_com.write(bytes);
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String command = "D";
                byte[] bytes = command.getBytes(Charset.defaultCharset());
                bluetooth_com.write(bytes);
            }
        });
        btn_rot_right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String command = "Q";
                byte[] bytes = command.getBytes(Charset.defaultCharset());
                bluetooth_com.write(bytes);
            }
        });
        btn_rot_left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String command = "E";
                byte[] bytes = command.getBytes(Charset.defaultCharset());
                bluetooth_com.write(bytes);
            }
        });
        btn_inc_speed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String command = "+";
                byte[] bytes = command.getBytes(Charset.defaultCharset());
                bluetooth_com.write(bytes);
            }
        });
        btn_dec_speed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String command = "-";
                byte[] bytes = command.getBytes(Charset.defaultCharset());
                bluetooth_com.write(bytes);
            }
        });
        btn_stop_car.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String command = "B";
                byte[] bytes = command.getBytes(Charset.defaultCharset());
                bluetooth_com.write(bytes);
            }
        });
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButton_handler();
            }
        });
        btn_connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnconnect_handler();
            }
        });
    }


    private void btnconnect_handler(){
        bluetooth_com = new BluetoothCommunication(address);
        bluetooth_com.start();
        sumobot_control_buttons(BUTTON_VISIBLE);
        tb.setEnabled(true);
        tb.setActivated(true);
        tb.setVisibility(View.VISIBLE);
        button_disappear(btn_connect);
    }
    private void btn_Discoverable(){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(discoverableIntent);
    }
    private void BT_init(){

        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        if(!bt.isEnabled())
        {
            Intent enableBTIntent = new Intent (BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

        }
        btn_Discoverable(); //make device discoverable for other devices
       if(bt.isDiscovering()){
            bt.cancelDiscovery();
            bt.startDiscovery();
        }
        if(!bt.isDiscovering()){
            bt.startDiscovery();
        }


    }

    private void toggleButton_handler(){
       if(tb.getText().equals(tb.getTextOff())){
           sumobot_control_buttons(BUTTON_VISIBLE);
           tv.setVisibility(View.INVISIBLE);
           if(tb_send_once == false){
               String command = "1";
               byte[] bytes = command.getBytes(Charset.defaultCharset());
               bluetooth_com.write(bytes);
                tb_send_once = true;
           }

       }
       else if(tb.getText().equals(tb.getTextOn())){
           sumobot_control_buttons(BUTTON_INVISIBLE);
           tv.setVisibility(View.VISIBLE);
           if(tb_send_once == true){
               String command = "2";
               byte[] bytes = command.getBytes(Charset.defaultCharset());
               bluetooth_com.write(bytes);
               tb_send_once = false;
           }

       }
    }

    private void button_disappear(Button b){
        b.setActivated(false);
        b.setEnabled(false);
        b.setVisibility(View.INVISIBLE);
    }

    private void button_appear(Button b){
        b.setActivated(true);
        b.setEnabled(true);
        b.setVisibility(View.VISIBLE);
    }

    private void sumobot_control_buttons(String s){
        if(s.equals(BUTTON_VISIBLE)){
            for(Button i : all_buttons){
                button_appear(i);
            }
        }
        else if(s.equals(BUTTON_INVISIBLE)){
            for(Button i : all_buttons){
                button_disappear(i);
            }
        }
    }
    private void controls_init(){
        //only the text view will be visible
        sumobot_control_buttons(BUTTON_INVISIBLE);
        tb.setEnabled(false);
        tb.setActivated(false);
        tb.setVisibility(View.INVISIBLE);
    }
}
