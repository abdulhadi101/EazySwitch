package com.example.abdulhadi101.fptb_bt_app;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity   {
    ImageButton bulb1, bulb2, bulb3, switch1, switch2, doorlock;
    TextView bulb1_state, bulb2_state, bulb3_state, switch1_state, switch2_state, door_state, bluetoothbtn_state;


    Thread thread;
    byte buffer[];

    boolean stopThread;
    boolean connected = false;
    String command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);





        /**
         * casting the above declared ImageButton Variable with their corresponding i.d in the xml design file
         */
        bulb1 = (ImageButton) findViewById(R.id.bulb1_btn);
        bulb2 = (ImageButton) findViewById(R.id.bulb2_btn);
        bulb3 = (ImageButton) findViewById(R.id.bulb3_btn);
        switch1 = (ImageButton) findViewById(R.id.switch1_btn);
        switch2 = (ImageButton) findViewById(R.id.switch2_btn);
        doorlock = (ImageButton) findViewById(R.id.door_btn);


        /**
         * casting the above declared TextView Variable with their corresponding i.d in the xml design file
         */
        bulb1_state = (TextView) findViewById(R.id.textView1);
        bulb2_state = (TextView) findViewById(R.id.textView2);
        bulb3_state = (TextView) findViewById(R.id.textView3);
        switch1_state = (TextView) findViewById(R.id.textView4);
        switch2_state = (TextView) findViewById(R.id.textView5);
        door_state = (TextView) findViewById(R.id.textView6);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
