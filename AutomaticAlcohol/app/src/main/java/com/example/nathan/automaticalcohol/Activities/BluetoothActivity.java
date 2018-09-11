package com.example.nathan.automaticalcohol.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity{

    private TextView mBluetoothStatus;
    private TextView mReadBuffer;

    private ConnectedThread mConnectedThread;           // bluetooth - sends data back and fourth
    private BluetoothSocket bluetoothSocket = null;
    private BluetoothAdapter bluetoothAdapter;
    private Handler mHandler;
    private ListView mDevicesListView;

    Set<BluetoothDevice> pairedDevices;

    private ArrayAdapter<String> bluetoothArrayAdapter;

    private Boolean toggle_led = false;

    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);


        // initializers for GUI items
        CheckBox mLedCheckBox = findViewById(R.id.ledToggle_CheckBox);
        mBluetoothStatus = findViewById(R.id.bluetoothStatus_textView);
        mReadBuffer = findViewById(R.id.readBuffer_textView);
        Button mScanBtn = findViewById(R.id.scan_button);
        Button mOffBtn = findViewById(R.id.off_button);
        Button mDiscoverBtn = findViewById(R.id.discover_button);
        Button mListPairedDevicesBtn = findViewById(R.id.paired_button);

        bluetoothArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mDevicesListView = findViewById(R.id.devices_listView);
        mDevicesListView.setAdapter(bluetoothArrayAdapter); // assign model to view
        mDevicesListView.setOnItemClickListener(deviceClickListener);


        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mReadBuffer.setText(readMessage);
                }

                if(msg.what == CONNECTING_STATUS){
                    if(msg.arg1 == 1)
                        mBluetoothStatus.setText("Connected to Device: " + (String)(msg.obj));
                    else
                        mBluetoothStatus.setText("Connection Failed");
                }
            }
        };

        if (bluetoothAdapter == null) {
            //Device does not support Bluetooth
            Toast.makeText(getApplicationContext(), "Bluetooth device not found", Toast.LENGTH_LONG).show();
        } else {
            mLedCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TODO: change to actually toggle
                    if(mConnectedThread != null) {
                        if (toggle_led) {
                            mConnectedThread.write("0");
                            toggle_led = false;
                        } else if (!toggle_led) {
                            mConnectedThread.write("1");
                            toggle_led = true;
                        }
                    }
                }
            });

            mScanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothOn(v);
                }
            });

            mOffBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    bluetoothOff(v);
                }
            });

            mListPairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    listPairedDevices(v);
                }
            });
            mDiscoverBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    discover(v);
                }
            });
        }
    }

    private void bluetoothOn(View view) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Toast.makeText(getApplicationContext(), "Bluetooth On", Toast.LENGTH_LONG).show();
        }
    }

    private void bluetoothOff(View view) {
        bluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Bluetooth Off", Toast.LENGTH_LONG).show();
    }

    //    TODO:  this is questionably needed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                mBluetoothStatus.setText("Enabled");
            }
        }
    }


    private void discover(View view) {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(), "Discovery stopped", Toast.LENGTH_LONG).show();
        } else {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothArrayAdapter.clear();
                Toast.makeText(getApplicationContext(), "Discovery Started", Toast.LENGTH_LONG).show();
                registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
        }
    }

    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                bluetoothArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private void listPairedDevices(View view) {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                bluetoothArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    private AdapterView.OnItemClickListener deviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_LONG).show();
                return;
            }
            mBluetoothStatus.setText("Connecting...");
            // Connecting device's MAC address is last 17 chars in the View
            String data = ((TextView) view).getText().toString();
            final String address = data.substring(data.length() - 17);
            final String name = data.substring(0, data.length() - 17);


            // Spawns new thread to avoid blocking GUI Thread
            new Thread() {
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

                    try {
                        bluetoothSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                    // Make bluetooth socket connection
                    try {
                        bluetoothSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            bluetoothSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                        } catch (IOException e2) {
                            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (!fail) {
                        mConnectedThread = new ConnectedThread(bluetoothSocket);
                        mConnectedThread.start();
                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name).sendToTarget();
                    }
                }
            }.start();
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, uuid);
        } catch (Exception e) {
            Log.e("createBluetoothSocket", "Could not create Insecure RFComm Connection", e);
        }
        return device.createInsecureRfcommSocketToServiceRecord(uuid);
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {
                        buffer = new byte[1024];
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }









}

