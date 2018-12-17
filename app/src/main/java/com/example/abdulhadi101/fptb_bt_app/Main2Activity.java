package com.example.abdulhadi101.fptb_bt_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import static com.example.abdulhadi101.fptb_bt_app.R.layout.content_main2;
import static com.example.abdulhadi101.fptb_bt_app.R.string.bluetooth;

public class Main2Activity extends AppCompatActivity {
   public BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_PAIRED_DEVICE = 2;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private final String DEVICE_ADDRESS = "98:D3:31:90:82:9A"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private OutputStream outputStream;
    private InputStream inputStream;
    public static String EXTRA_ADDRESS = "device_address";
    Thread thread;
    byte buffer[];

    boolean stopThread;
    boolean connected = false;
    String command;


    private Set<BluetoothDevice> pairedDevices;
    ImageButton btnTurnOnBT;
    TextView stateBluetooth;
    Button btnPairedDeviceList;
    ListView deviceListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnPairedDeviceList = (Button) content_main2.find
        btnTurnOnBT = (ImageButton) findViewById(R.id.btnTurnOnBT);
        deviceListView = (ListView) findViewById(R.id.listView) ;
        stateBluetooth = (TextView) findViewById(R.id.textView8);


        btnTurnOnBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BTinit()) {
                    BTconnect();
                    beginListenForData();

                    // The code below sends the number 3 to the Arduino asking it to send the current state of the door lock so the lock state icon can be updated accordingly

                    command = "3";

                    try {
                        outputStream.write(command.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        /** lock_state_btn.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v){

        if(connected == false)
        {
        Toast.makeText(getApplicationContext(), "Please establish a connection with the bluetooth servo door lock first", Toast.LENGTH_SHORT).show();
        }
        else
        {
        command = "1";

        try
        {
        outputStream.write(command.getBytes()); // Sends the number 1 to the Arduino. For a detailed look at how the resulting command is handled, please see the Arduino Source Code
        }
        catch (IOException e)
        {
        e.printStackTrace();
        }
        }
        }
        }**/
        btnPairedDeviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevices();
            }
        });
    }


    void beginListenForData() // begins listening for any incoming data from the Arduino
    {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024]; }
 /**
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopThread) {
                    try {
                        int byteCount = inputStream.available();

                       if (byteCount > 0) {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string = new String(rawBytes, "UTF-8");

                            handler.post(new Runnable() {
                                 public void run()
                               {
                                 if(string.equals("3"))
                                 {
                                 lock_state_text.setText("Lock State: LOCKED"); // Changes the lock state text
                                 lock_state_img.setImageResource(R.drawable.locked_icon); //Changes the lock state icon
                                 }
                                 else if(string.equals("4"))
                                 {
                                 lock_state_text.setText("Lock State: UNLOCKED");
                                 lock_state_img.setImageResource(R.drawable.unlocked_icon);
                                 }
                                 }
                            });
                        }
                    } catch (IOException ex) {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }
**/
    //Initializes bluetooth module
    public boolean BTinit() {
        boolean found = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }



        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();


        if (bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                if (iterator.getAddress().equals(DEVICE_ADDRESS)) {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    public boolean BTconnect() {

        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();

            Toast.makeText(getApplicationContext(),
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }

        if (connected) {
            // Make an intent to start next activity.
            Intent intent = new Intent(Main2Activity.this, MainActivity.class);

            //Change the activity.
            //  intent.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(intent);
            try {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                inputStream = socket.getInputStream(); //gets the input stream of the socket
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return connected;
    }
    private void pairedDevices()
    {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        deviceListView.setAdapter(adapter);
        deviceListView.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity.
            Intent i = new Intent(Main2Activity.this, MainActivity.class);

            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };


}